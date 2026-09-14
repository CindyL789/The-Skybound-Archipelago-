package com.example.data

import android.content.Context
import android.content.SharedPreferences

class CourierPreferences(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("skybound_courier_prefs", Context.MODE_PRIVATE)

  var currentChapterId: Int
    get() = prefs.getInt("key_current_chapter", 0)
    set(value) = prefs.edit().putInt("key_current_chapter", value).apply()

  var isParchmentMode: Boolean
    get() = prefs.getBoolean("key_parchment_mode", false)
    set(value) = prefs.edit().putBoolean("key_parchment_mode", value).apply()

  var fontScale: Float
    get() = prefs.getFloat("key_font_scale", 1.0f)
    set(value) = prefs.edit().putFloat("key_font_scale", value).apply()

  var ambientAudioEnabled: Boolean
    get() = prefs.getBoolean("key_ambient_audio", false)
    set(value) = prefs.edit().putBoolean("key_ambient_audio", value).apply()

  var ambientVolume: Float
    get() = prefs.getFloat("key_ambient_volume", 0.7f)
    set(value) = prefs.edit().putFloat("key_ambient_volume", value).apply()

  var ambientPresetName: String
    get() = prefs.getString("key_ambient_preset", "CLOUD_SEA") ?: "CLOUD_SEA"
    set(value) = prefs.edit().putString("key_ambient_preset", value).apply()

  var ambientDucking: Boolean
    get() = prefs.getBoolean("key_ambient_ducking", true)
    set(value) = prefs.edit().putBoolean("key_ambient_ducking", value).apply()

  var narrationSpeed: Float
    get() = prefs.getFloat("key_narration_speed", 1.0f)
    set(value) = prefs.edit().putFloat("key_narration_speed", value).apply()

  var narrationPitch: Float
    get() = prefs.getFloat("key_narration_pitch", 1.0f)
    set(value) = prefs.edit().putFloat("key_narration_pitch", value).apply()

  var narrationPersonaId: String
    get() = prefs.getString("key_narration_persona", "COURIER") ?: "COURIER"
    set(value) = prefs.edit().putString("key_narration_persona", value).apply()

  var narrationVoiceName: String?
    get() = prefs.getString("key_narration_voice_name", null)
    set(value) = prefs.edit().putString("key_narration_voice_name", value).apply()

  var activeRig: CourierRig
    get() {
      val name = prefs.getString("key_active_rig", CourierRig.STANDARD_COURIER.name)
      return try {
        CourierRig.valueOf(name ?: CourierRig.STANDARD_COURIER.name)
      } catch (e: Exception) {
        CourierRig.STANDARD_COURIER
      }
    }
    set(value) = prefs.edit().putString("key_active_rig", value.name).apply()

  var lanternMode: LanternMode
    get() {
      val name = prefs.getString("key_lantern_mode", LanternMode.STANDARD.name)
      return try {
        LanternMode.valueOf(name ?: LanternMode.STANDARD.name)
      } catch (e: Exception) {
        LanternMode.STANDARD
      }
    }
    set(value) = prefs.edit().putString("key_lantern_mode", value.name).apply()

  fun getChoiceFor(choiceId: String): String? {
    return prefs.getString("choice_$choiceId", null)
  }

  fun saveChoice(choiceId: String, optionId: String) {
    prefs.edit().putString("choice_$choiceId", optionId).apply()
  }

  fun getBookmarks(): Set<Int> {
    val raw = prefs.getStringSet("key_bookmarks", emptySet()) ?: emptySet()
    return raw.mapNotNull { it.toIntOrNull() }.toSet()
  }

  fun toggleBookmark(chapterId: Int): Boolean {
    val current = getBookmarks().toMutableSet()
    val isBookmarked = if (current.contains(chapterId)) {
      current.remove(chapterId)
      false
    } else {
      current.add(chapterId)
      true
    }
    prefs.edit().putStringSet("key_bookmarks", current.map { it.toString() }.toSet()).apply()
    return isBookmarked
  }

  fun getCollectedItemIds(): Set<String> {
    return prefs.getStringSet("key_collected_items", defaultItems()) ?: defaultItems()
  }

  fun unlockItem(itemId: String) {
    val current = getCollectedItemIds().toMutableSet()
    current.add(itemId)
    prefs.edit().putStringSet("key_collected_items", current).apply()
  }

  fun getSavedIllustrationsJson(): String? {
    return prefs.getString("key_saved_illustrations_json", null)
  }

  fun saveIllustrationsJson(json: String) {
    prefs.edit().putString("key_saved_illustrations_json", json).apply()
  }

  private fun defaultItems(): Set<String> = setOf(
    "item_staff_lantern",
    "item_vermilion_sash",
    "item_scale_compass",
    "item_brass_tubes"
  )
}
