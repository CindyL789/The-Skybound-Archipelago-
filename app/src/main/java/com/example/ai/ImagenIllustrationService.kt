package com.example.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

sealed class ImageGenerationResult {
  data class Success(
    val bitmap: Bitmap,
    val savedFilePath: String,
    val isLiveGeneration: Boolean,
    val note: String? = null
  ) : ImageGenerationResult()

  data class Error(
    val message: String,
    val fallbackBitmap: Bitmap? = null,
    val isMissingApiKey: Boolean = false
  ) : ImageGenerationResult()
}

data class SceneIllustration(
  val id: String,
  val title: String,
  val prompt: String,
  val style: PainterlyStyle,
  val timestamp: Long,
  val localFilePath: String? = null,
  val drawableResId: Int? = null,
  val chapterId: Int = 0,
  val isUserGenerated: Boolean = false
)

enum class PainterlyStyle(
  val id: String,
  val displayName: String,
  val promptModifier: String,
  val description: String
) {
  PAINTERLY_OIL(
    id = "PAINTERLY_OIL",
    displayName = "Painterly Impasto",
    promptModifier = "atmospheric painterly fantasy illustration with visible oil brushwork, rich impasto texture, dramatic golden hour clouds, romantic fantasy concept art style, masterwork digital painting",
    description = "Rich oil brushwork with vibrant atmospheric cloud light."
  ),
  PARCHMENT_WATERCOLOR(
    id = "PARCHMENT_WATERCOLOR",
    displayName = "Antiquarian Watercolor",
    promptModifier = "ethereal watercolor wash on aged parchment, delicate sepia inking, soft bleeding purple and golden cloud banks, antiquarian storybook plate illustration",
    description = "Delicate ink and watercolor wash on aged parchment."
  ),
  AMBER_CHIAROSCURO(
    id = "AMBER_CHIAROSCURO",
    displayName = "Amber Chiaroscuro",
    promptModifier = "dramatic chiaroscuro lighting, warm glowing amber lantern light piercing midnight indigo clouds, deep volumetric shadows, cinematic atmospheric fantasy oil painting",
    description = "Dramatic lantern glow and deep twilight shadows."
  ),
  GHIBLI_WIND(
    id = "GHIBLI_WIND",
    displayName = "Ghibli Windscape",
    promptModifier = "lush floating archipelago islands, billowing cumulus cloud castles, clear azure winds, nostalgic anime watercolor background art style, vibrant and crisp",
    description = "Crisp azure skies and billowing cumulus cloudscapes."
  );

  companion object {
    fun fromId(id: String?): PainterlyStyle {
      return values().find { it.id.equals(id, ignoreCase = true) } ?: PAINTERLY_OIL
    }
  }
}

class ImagenIllustrationService(private val context: Context) {

  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(45, TimeUnit.SECONDS)
    .readTimeout(45, TimeUnit.SECONDS)
    .writeTimeout(45, TimeUnit.SECONDS)
    .build()

  suspend fun generateSceneIllustration(
    sceneTitle: String,
    userPrompt: String,
    style: PainterlyStyle,
    aspectRatio: String = "16:9",
    chapterId: Int = 0
  ): ImageGenerationResult = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY

    val fullPrompt = "$userPrompt, ${style.promptModifier}, highly detailed, cinematic lighting"

    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      Log.w("ImagenService", "GEMINI_API_KEY is missing or default placeholder. Using archive plate fallback.")
      return@withContext provideArchiveFallback(
        sceneTitle = sceneTitle,
        chapterId = chapterId,
        reason = "GEMINI_API_KEY not configured. Set your key in the AI Studio Secrets panel to enable live Imagen 3 generation."
      )
    }

    // Try Primary: Imagen 3 (:predict)
    val imagenResult = callImagen3Predict(apiKey, fullPrompt, aspectRatio)
    if (imagenResult != null) {
      val savedFile = saveBitmapLocally(imagenResult, "scene_${System.currentTimeMillis()}")
      return@withContext ImageGenerationResult.Success(
        bitmap = imagenResult,
        savedFilePath = savedFile.absolutePath,
        isLiveGeneration = true,
        note = "Generated with Imagen 3"
      )
    }

    // Secondary fallback: Gemini 2.5 Flash Image (:generateContent)
    val geminiResult = callGeminiImageGenerate(apiKey, fullPrompt, aspectRatio)
    if (geminiResult != null) {
      val savedFile = saveBitmapLocally(geminiResult, "scene_${System.currentTimeMillis()}")
      return@withContext ImageGenerationResult.Success(
        bitmap = geminiResult,
        savedFilePath = savedFile.absolutePath,
        isLiveGeneration = true,
        note = "Generated with Gemini Flash Image"
      )
    }

    // Fallback if network or quota issue
    Log.e("ImagenService", "Image generation API calls unsuccessful. Returning fallback scene plate.")
    return@withContext provideArchiveFallback(
      sceneTitle = sceneTitle,
      chapterId = chapterId,
      reason = "Live generation reached quota or network limit. Displaying archived Skybound scene plate."
    )
  }

  private fun callImagen3Predict(apiKey: String, prompt: String, aspectRatio: String): Bitmap? {
    return try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/imagen-3.0-generate-002:predict?key=$apiKey"

      val jsonPayload = JSONObject().apply {
        val instances = JSONArray().apply {
          put(JSONObject().apply {
            put("prompt", prompt)
          })
        }
        put("instances", instances)
        put("parameters", JSONObject().apply {
          put("sampleCount", 1)
          put("aspectRatio", aspectRatio)
          put("outputOptions", JSONObject().apply {
            put("mimeType", "image/jpeg")
          })
        })
      }

      val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
      val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

      val response = okHttpClient.newCall(request).execute()
      if (!response.isSuccessful) {
        val errBody = response.body?.string()
        Log.w("ImagenService", "Imagen 3 response error ${response.code}: $errBody")
        return null
      }

      val respString = response.body?.string() ?: return null
      val root = JSONObject(respString)
      val predictions = root.optJSONArray("predictions")
      if (predictions != null && predictions.length() > 0) {
        val pred = predictions.getJSONObject(0)
        val b64 = pred.optString("bytesBase64Encoded")
        if (b64.isNotEmpty()) {
          val bytes = Base64.decode(b64, Base64.DEFAULT)
          return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
      }
      null
    } catch (e: Exception) {
      Log.e("ImagenService", "callImagen3Predict exception: ${e.message}")
      null
    }
  }

  private fun callGeminiImageGenerate(apiKey: String, prompt: String, aspectRatio: String): Bitmap? {
    return try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-image:generateContent?key=$apiKey"

      val jsonPayload = JSONObject().apply {
        val contents = JSONArray().apply {
          put(JSONObject().apply {
            val parts = JSONArray().apply {
              put(JSONObject().apply {
                put("text", prompt)
              })
            }
            put("parts", parts)
          })
        }
        put("contents", contents)
        put("generationConfig", JSONObject().apply {
          put("responseModalities", JSONArray().apply { put("IMAGE") })
          put("imageConfig", JSONObject().apply {
            put("aspectRatio", aspectRatio)
          })
        })
      }

      val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
      val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

      val response = okHttpClient.newCall(request).execute()
      if (!response.isSuccessful) {
        val errBody = response.body?.string()
        Log.w("ImagenService", "Gemini flash image response error ${response.code}: $errBody")
        return null
      }

      val respString = response.body?.string() ?: return null
      val root = JSONObject(respString)
      val candidates = root.optJSONArray("candidates")
      val parts = candidates?.optJSONObject(0)?.optJSONObject("content")?.optJSONArray("parts")
      if (parts != null) {
        for (i in 0 until parts.length()) {
          val part = parts.getJSONObject(i)
          val inlineData = part.optJSONObject("inlineData")
          val b64 = inlineData?.optString("data")
          if (!b64.isNullOrEmpty()) {
            val bytes = Base64.decode(b64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
          }
        }
      }
      null
    } catch (e: Exception) {
      Log.e("ImagenService", "callGeminiImageGenerate exception: ${e.message}")
      null
    }
  }

  private fun provideArchiveFallback(
    sceneTitle: String,
    chapterId: Int,
    reason: String
  ): ImageGenerationResult {
    val resId = when (chapterId) {
      0 -> R.drawable.img_scene_skiff
      1 -> R.drawable.img_scene_salt_cliffs
      2 -> R.drawable.img_chain_climb
      3 -> R.drawable.img_coil_dragon
      else -> R.drawable.img_hero_archipelago
    }

    return try {
      val bitmap = BitmapFactory.decodeResource(context.resources, resId)
      val savedFile = saveBitmapLocally(bitmap, "archive_scene_${chapterId}_${System.currentTimeMillis()}")
      ImageGenerationResult.Success(
        bitmap = bitmap,
        savedFilePath = savedFile.absolutePath,
        isLiveGeneration = false,
        note = reason
      )
    } catch (e: Exception) {
      ImageGenerationResult.Error(
        message = reason,
        isMissingApiKey = true
      )
    }
  }

  private fun saveBitmapLocally(bitmap: Bitmap, name: String): File {
    val dir = File(context.filesDir, "scene_illustrations").apply { mkdirs() }
    val file = File(dir, "$name.jpg")
    FileOutputStream(file).use { out ->
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file
  }
}
