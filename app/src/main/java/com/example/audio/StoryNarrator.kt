package com.example.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.example.data.Chapter
import java.util.Locale

class StoryNarrator(
  private val context: Context,
  private val onStateChanged: (isNarrating: Boolean, isPaused: Boolean, paragraphIndex: Int) -> Unit,
  private val onChapterCompleted: () -> Unit,
  private val onVoicesDiscovered: (List<DeviceVoice>) -> Unit = {},
  private val onPreviewStateChanged: (Boolean) -> Unit = {}
) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = null
  private var isInitialized = false

  private var currentChapter: Chapter? = null
  private var currentParagraphIndex: Int = 0
  private var isNarrating: Boolean = false
  private var isPaused: Boolean = false
  private var isPreviewing: Boolean = false

  private var speechRate: Float = 0.92f
  private var pitch: Float = 0.88f
  private var activeVoiceName: String? = null
  private var activeLocale: Locale = Locale.US

  init {
    tts = TextToSpeech(context, this)
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      applyLocaleAndVoice()
      tts?.setSpeechRate(speechRate)
      tts?.setPitch(pitch)
      setupUtteranceListener()
      isInitialized = true

      val discoveredVoices = scanVoices()
      onVoicesDiscovered(discoveredVoices)
      Log.d("StoryNarrator", "TTS initialized with ${discoveredVoices.size} voices found")
    } else {
      Log.e("StoryNarrator", "TTS initialization failed with status $status")
    }
  }

  private fun scanVoices(): List<DeviceVoice> {
    return try {
      val voiceSet = tts?.voices ?: return emptyList()
      voiceSet.filter { voice ->
        val lang = voice.locale.language
        lang.equals("en", ignoreCase = true) ||
          lang.equals(Locale.getDefault().language, ignoreCase = true)
      }.map { voice ->
        val isFemale = voice.name.contains("female", ignoreCase = true) ||
          voice.features?.contains("female") == true
        val isMale = voice.name.contains("male", ignoreCase = true) ||
          voice.features?.contains("male") == true
        val gender = when {
          isFemale -> "Female"
          isMale -> "Male"
          else -> null
        }

        val country = voice.locale.displayCountry.ifBlank {
          voice.locale.displayName
        }

        val cleanName = voice.name
          .substringAfterLast('#', voice.name.substringAfterLast('/'))
          .replace("-local", "")
          .replace("_", " ")

        val label = buildString {
          append(country)
          if (gender != null) append(" • $gender")
          if (cleanName.isNotBlank() && cleanName != voice.name) {
            append(" ($cleanName)")
          }
        }

        DeviceVoice(
          name = voice.name,
          label = label,
          locale = voice.locale,
          countryName = country,
          isHighQuality = voice.quality >= Voice.QUALITY_HIGH,
          genderHint = gender
        )
      }.sortedWith(compareBy({ it.countryName }, { it.genderHint ?: "" }, { it.label }))
    } catch (e: Exception) {
      Log.e("StoryNarrator", "Error scanning voices: ${e.message}")
      emptyList()
    }
  }

  private fun setupUtteranceListener() {
    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
      override fun onStart(utteranceId: String?) {
        if (utteranceId == "sample_preview") {
          isPreviewing = true
          onPreviewStateChanged(true)
        } else {
          notifyState()
        }
      }

      override fun onDone(utteranceId: String?) {
        if (utteranceId == "sample_preview") {
          isPreviewing = false
          onPreviewStateChanged(false)
          return
        }

        if (!isNarrating || isPaused) return
        val chapter = currentChapter ?: return

        val nextIndex = currentParagraphIndex + 1
        if (nextIndex < chapter.paragraphs.size) {
          currentParagraphIndex = nextIndex
          speakParagraph(chapter.paragraphs[currentParagraphIndex].text)
        } else {
          isNarrating = false
          isPaused = false
          notifyState()
          onChapterCompleted()
        }
      }

      override fun onError(utteranceId: String?) {
        Log.e("StoryNarrator", "TTS Utterance error on $utteranceId")
        if (utteranceId == "sample_preview") {
          isPreviewing = false
          onPreviewStateChanged(false)
        } else {
          isNarrating = false
          isPaused = false
          notifyState()
        }
      }
    })
  }

  private fun applyLocaleAndVoice() {
    try {
      if (activeVoiceName != null) {
        val targetVoice = tts?.voices?.find { it.name == activeVoiceName }
        if (targetVoice != null) {
          tts?.voice = targetVoice
          activeLocale = targetVoice.locale
          return
        }
      }
      val res = tts?.setLanguage(activeLocale)
      if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
        tts?.setLanguage(Locale.getDefault())
      }
    } catch (e: Exception) {
      Log.w("StoryNarrator", "Voice configuration fallback: ${e.message}")
    }
  }

  fun selectDeviceVoice(voiceName: String?) {
    activeVoiceName = voiceName
    if (voiceName != null) {
      val target = tts?.voices?.find { it.name == voiceName }
      if (target != null) {
        tts?.voice = target
        activeLocale = target.locale
      }
    } else {
      applyLocaleAndVoice()
    }
  }

  fun applyPersona(persona: NarratorPersona) {
    speechRate = persona.speechRate
    pitch = persona.pitch
    activeLocale = persona.preferredLocale
    activeVoiceName = null

    tts?.setSpeechRate(speechRate)
    tts?.setPitch(pitch)

    // Try finding matching voice for this persona if installed
    try {
      val matchingVoice = tts?.voices?.find { voice ->
        val matchesLocale = voice.locale.language.equals(persona.preferredLocale.language, ignoreCase = true) &&
          (persona.preferredLocale.country.isBlank() || voice.locale.country.equals(persona.preferredLocale.country, ignoreCase = true))
        val matchesGender = persona.voiceHint == null ||
          voice.name.contains(persona.voiceHint, ignoreCase = true) ||
          voice.features?.contains(persona.voiceHint) == true
        matchesLocale && matchesGender
      }

      if (matchingVoice != null) {
        tts?.voice = matchingVoice
      } else {
        tts?.setLanguage(persona.preferredLocale)
      }
    } catch (e: Exception) {
      tts?.setLanguage(persona.preferredLocale)
    }
  }

  fun previewVoice(sampleText: String = "The wind over the Cloud Sea carries memories of fallen towers and skybound voyages.") {
    stopChapterSpeechOnly()
    val params = Bundle()
    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "sample_preview")
    tts?.speak(sampleText, TextToSpeech.QUEUE_FLUSH, params, "sample_preview")
  }

  fun stopPreview() {
    if (isPreviewing) {
      isPreviewing = false
      tts?.stop()
      onPreviewStateChanged(false)
    }
  }

  fun startChapter(chapter: Chapter, startParagraph: Int = 0) {
    stopPreview()
    currentChapter = chapter
    currentParagraphIndex = startParagraph.coerceIn(0, chapter.paragraphs.size - 1)
    isNarrating = true
    isPaused = false

    if (!isInitialized) return
    speakCurrentParagraph()
  }

  private fun speakCurrentParagraph() {
    val chapter = currentChapter ?: return
    if (currentParagraphIndex in chapter.paragraphs.indices) {
      speakParagraph(chapter.paragraphs[currentParagraphIndex].text)
    }
  }

  private fun speakParagraph(text: String) {
    val params = Bundle()
    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "para_$currentParagraphIndex")
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "para_$currentParagraphIndex")
    notifyState()
  }

  fun pause() {
    if (isNarrating && !isPaused) {
      isPaused = true
      tts?.stop()
      notifyState()
    }
  }

  fun resume() {
    if (isNarrating && isPaused) {
      isPaused = false
      speakCurrentParagraph()
    }
  }

  private fun stopChapterSpeechOnly() {
    if (isNarrating) {
      isPaused = true
      tts?.stop()
      notifyState()
    }
  }

  fun stop() {
    isNarrating = false
    isPaused = false
    isPreviewing = false
    tts?.stop()
    notifyState()
    onPreviewStateChanged(false)
  }

  fun nextParagraph() {
    val chapter = currentChapter ?: return
    if (currentParagraphIndex < chapter.paragraphs.size - 1) {
      currentParagraphIndex++
      if (isNarrating && !isPaused) {
        speakCurrentParagraph()
      } else {
        notifyState()
      }
    }
  }

  fun previousParagraph() {
    if (currentParagraphIndex > 0) {
      currentParagraphIndex--
      if (isNarrating && !isPaused) {
        speakCurrentParagraph()
      } else {
        notifyState()
      }
    }
  }

  fun setSpeechRate(rate: Float) {
    speechRate = rate.coerceIn(0.6f, 1.6f)
    tts?.setSpeechRate(speechRate)
  }

  fun setPitch(pitchVal: Float) {
    pitch = pitchVal.coerceIn(0.6f, 1.5f)
    tts?.setPitch(pitch)
  }

  fun isNarrating(): Boolean = isNarrating
  fun isPaused(): Boolean = isPaused
  fun isPreviewing(): Boolean = isPreviewing
  fun getCurrentParagraph(): Int = currentParagraphIndex
  fun getActiveVoiceName(): String? = activeVoiceName
  fun getSpeechRate(): Float = speechRate
  fun getPitch(): Float = pitch

  private fun notifyState() {
    onStateChanged(isNarrating, isPaused, currentParagraphIndex)
  }

  fun cleanup() {
    stop()
    tts?.shutdown()
    tts = null
    isInitialized = false
  }
}
