package com.example.character

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.example.ai.ImagenIllustrationService
import com.example.ai.PainterlyStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CharacterAiChronicler(private val context: Context) {

  private val httpClient = OkHttpClient.Builder()
    .connectTimeout(25, TimeUnit.SECONDS)
    .readTimeout(25, TimeUnit.SECONDS)
    .writeTimeout(25, TimeUnit.SECONDS)
    .build()

  private val imagenService = ImagenIllustrationService(context)

  suspend fun chronicleCharacterWithAi(
    archetype: SkyfarerArchetype? = null,
    origin: OriginIsle? = null,
    lantern: LanternAffinity? = null,
    preferredName: String? = null
  ): SkyfarerCharacter = withContext(Dispatchers.IO) {
    val selectedArchetype = archetype ?: SkyfarerArchetype.values().random()
    val selectedOrigin = origin ?: OriginIsle.values().random()
    val selectedLantern = lantern ?: selectedArchetype.recommendedLantern
    val stats = CharacterGeneratorEngine.generateProceduralStats(selectedArchetype)

    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      Log.i("CharacterAi", "API key missing or default placeholder. Using procedural generator.")
      val base = CharacterGeneratorEngine.generateFullProceduralCharacter(
        archetype = selectedArchetype,
        origin = selectedOrigin,
        lantern = selectedLantern
      )
      return@withContext if (!preferredName.isNullOrBlank()) base.copy(name = preferredName) else base
    }

    try {
      val prompt = buildString {
        append("You are the Elder Chronicler of the Skybound Archipelago, a rich fantasy world of floating islands, deep cloud seas, salt couriers, storm-glass lanterns, and ancient slumbering sky serpents.\n")
        append("Generate a deeply evocative, original character profile in JSON format.\n")
        append("Parameters:\n")
        append("- Archetype: ${selectedArchetype.title} (${selectedArchetype.description})\n")
        append("- Origin Isle: ${selectedOrigin.isleName} (${selectedOrigin.trait})\n")
        append("- Lantern Flame Affinity: ${selectedLantern.flameName} (${selectedLantern.auraEffect})\n")
        if (!preferredName.isNullOrBlank()) {
          append("- Preferred Name: $preferredName\n")
        }
        append("\nRespond strictly in valid JSON with this exact schema:\n")
        append("{\n")
        append("  \"name\": \"Full Name (first and last or courier moniker)\",\n")
        append("  \"title\": \"Poetic epithet or title (e.g. 'Warden of the Seventh Chasm')\",\n")
        append("  \"signatureRelic\": \"A distinctive atmospheric physical relic or tool they carry\",\n")
        append("  \"personalityQuirk\": \"A unique, vivid habit or quirk related to high-altitude flight or the cloud sea\",\n")
        append("  \"motivation\": \"Why they risk their life in the sky\",\n")
        append("  \"backstory\": \"2-3 rich paragraphs detailing their upbringing, dangerous flights, and current mission\",\n")
        append("  \"customQuote\": \"A memorable, atmospheric line spoken by this character\"\n")
        append("}\n")
      }

      val requestBodyJson = JSONObject().apply {
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

        val generationConfig = JSONObject().apply {
          put("temperature", 0.75)
          put("responseMimeType", "application/json")
        }
        put("generationConfig", generationConfig)
      }

      val request = Request.Builder()
        .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
        .addHeader("Content-Type", "application/json")
        .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
        .build()

      val response = httpClient.newCall(request).execute()
      if (!response.isSuccessful) {
        Log.w("CharacterAi", "Gemini call failed with code: ${response.code}. Falling back to procedural.")
        val fallback = CharacterGeneratorEngine.generateFullProceduralCharacter(selectedArchetype, selectedOrigin, selectedLantern)
        return@withContext if (!preferredName.isNullOrBlank()) fallback.copy(name = preferredName) else fallback
      }

      val bodyText = response.body?.string().orEmpty()
      val responseJson = JSONObject(bodyText)
      val candidates = responseJson.optJSONArray("candidates")
      if (candidates == null || candidates.length() == 0) {
        val fallback = CharacterGeneratorEngine.generateFullProceduralCharacter(selectedArchetype, selectedOrigin, selectedLantern)
        return@withContext if (!preferredName.isNullOrBlank()) fallback.copy(name = preferredName) else fallback
      }

      val firstCandidate = candidates.getJSONObject(0)
      val contentObj = firstCandidate.getJSONObject("content")
      val partsArr = contentObj.getJSONArray("parts")
      val rawOutputText = partsArr.getJSONObject(0).getString("text")

      val parsed = JSONObject(rawOutputText)
      val generatedName = if (!preferredName.isNullOrBlank()) preferredName else parsed.optString("name", CharacterGeneratorEngine.rollRandomName())

      return@withContext SkyfarerCharacter(
        id = "skyfarer_ai_${System.currentTimeMillis()}",
        name = generatedName,
        title = parsed.optString("title", CharacterGeneratorEngine.rollRandomTitle(selectedArchetype, selectedOrigin)),
        archetype = selectedArchetype,
        originIsle = selectedOrigin,
        lanternAffinity = selectedLantern,
        gliderRig = selectedArchetype.recommendedRig,
        stats = stats,
        signatureRelic = parsed.optString("signatureRelic", "Sealed Brass Message Tube with amber resin lock"),
        personalityQuirk = parsed.optString("personalityQuirk", "Tastes the wind before every glider dive."),
        motivation = parsed.optString("motivation", "Delivering a promise against the weather."),
        backstory = parsed.optString("backstory", "Forged in the updrafts of the Archipelago."),
        customQuote = parsed.optString("customQuote", "The sky takes what it wants, but we keep our promises."),
        portraitDrawableRes = selectedArchetype.defaultDrawableRes,
        customPortraitPath = null,
        isAiGenerated = true,
        createdAt = System.currentTimeMillis()
      )
    } catch (e: Exception) {
      Log.w("CharacterAi", "Gemini chronicle unavailable, using procedural lore generator: ${e.message}")
      val fallback = CharacterGeneratorEngine.generateFullProceduralCharacter(selectedArchetype, selectedOrigin, selectedLantern)
      return@withContext if (!preferredName.isNullOrBlank()) fallback.copy(name = preferredName) else fallback
    }
  }

  suspend fun conjureCharacterPortrait(character: SkyfarerCharacter): String? = withContext(Dispatchers.IO) {
    val prompt = "Close-up atmospheric character portrait of ${character.name}, a fantasy ${character.archetype.title} from ${character.originIsle.isleName}. Wearing flight goggles or leather hood, glowing ${character.lanternAffinity.flameName} light illuminating their face, fantasy skyfarer aesthetic, painterly digital art."

    val result = imagenService.generateSceneIllustration(
      sceneTitle = "${character.name} Portrait",
      userPrompt = prompt,
      style = PainterlyStyle.PAINTERLY_OIL,
      aspectRatio = "1:1"
    )

    when (result) {
      is com.example.ai.ImageGenerationResult.Success -> result.savedFilePath
      is com.example.ai.ImageGenerationResult.Error -> null
    }
  }
}
