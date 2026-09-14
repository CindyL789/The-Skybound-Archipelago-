package com.example.audio

import java.util.Locale

/**
 * Distinct voice personas tailored for Skybound Archipelago narrative storytelling.
 * Provides custom pitch, tempo cadence, and preferred regional acoustic attributes.
 */
enum class NarratorPersona(
  val id: String,
  val title: String,
  val characterTag: String,
  val description: String,
  val speechRate: Float,
  val pitch: Float,
  val preferredLocale: Locale,
  val voiceHint: String? = null
) {
  COURIER(
    id = "COURIER",
    title = "The Salt Courier",
    characterTag = "Warm Storyteller",
    description = "Warm, mellow cadence with a gentle storytelling tempo. Atmospheric and soothing.",
    speechRate = 0.92f,
    pitch = 0.88f,
    preferredLocale = Locale.US,
    voiceHint = null
  ),
  ARCHIVIST(
    id = "ARCHIVIST",
    title = "Elder Chronicler",
    characterTag = "Deep & Resonant",
    description = "Rich, low-register voice with measured pacing suited for ancient histories and sky legends.",
    speechRate = 0.85f,
    pitch = 0.74f,
    preferredLocale = Locale.US,
    voiceHint = "male"
  ),
  SCHOLAR(
    id = "SCHOLAR",
    title = "Archipelago Scholar",
    characterTag = "British / Articulate",
    description = "Articulate British English cadence with crisp, clear enunciation and classical delivery.",
    speechRate = 0.95f,
    pitch = 0.98f,
    preferredLocale = Locale.UK,
    voiceHint = null
  ),
  PILOT(
    id = "PILOT",
    title = "Skiff Navigator",
    characterTag = "Brisk & Clear",
    description = "Clean, lively, and energetic voice for adventurous airship voyages over the cloud sea.",
    speechRate = 1.05f,
    pitch = 1.05f,
    preferredLocale = Locale.US,
    voiceHint = null
  ),
  LANTERNKEEPER(
    id = "LANTERNKEEPER",
    title = "Gentle Lanternkeeper",
    characterTag = "Soft & Calming",
    description = "Quiet, serene cadence designed for evening reading under the Amber Lamp.",
    speechRate = 0.88f,
    pitch = 0.94f,
    preferredLocale = Locale.US,
    voiceHint = "female"
  );

  companion object {
    fun fromId(id: String?): NarratorPersona {
      return values().find { it.id.equals(id, ignoreCase = true) } ?: COURIER
    }
  }
}

data class DeviceVoice(
  val name: String,
  val label: String,
  val locale: Locale,
  val countryName: String,
  val isHighQuality: Boolean,
  val genderHint: String? = null
)
