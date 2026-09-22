package com.example.character

import com.example.R
import com.example.data.CourierRig
import com.example.data.LanternMode
import org.json.JSONArray
import org.json.JSONObject

data class CharacterStats(
  val windSense: Int = 5,
  val gritAltitude: Int = 5,
  val lanternCraft: Int = 5,
  val loreWisdom: Int = 5,
  val tillerAgility: Int = 5
) {
  val totalPoints: Int get() = windSense + gritAltitude + lanternCraft + loreWisdom + tillerAgility

  fun toJsonObject(): JSONObject {
    return JSONObject().apply {
      put("windSense", windSense)
      put("gritAltitude", gritAltitude)
      put("lanternCraft", lanternCraft)
      put("loreWisdom", loreWisdom)
      put("tillerAgility", tillerAgility)
    }
  }

  companion object {
    fun fromJsonObject(json: JSONObject): CharacterStats {
      return CharacterStats(
        windSense = json.optInt("windSense", 5),
        gritAltitude = json.optInt("gritAltitude", 5),
        lanternCraft = json.optInt("lanternCraft", 5),
        loreWisdom = json.optInt("loreWisdom", 5),
        tillerAgility = json.optInt("tillerAgility", 5)
      )
    }
  }
}

enum class SkyfarerArchetype(
  val id: String,
  val title: String,
  val subtitle: String,
  val description: String,
  val defaultDrawableRes: Int,
  val primaryStatName: String,
  val recommendedRig: CourierRig,
  val recommendedLantern: LanternAffinity
) {
  SALT_COURIER(
    id = "SALT_COURIER",
    title = "Salt Courier",
    subtitle = "Abyss-Crossing Letter Carrier",
    description = "Daring long-range glider pilot delivering sealed brass message tubes across treacherous cloud shears, trusting hardware and promises kept.",
    defaultDrawableRes = R.drawable.img_sera_nami,
    primaryStatName = "Tiller Agility",
    recommendedRig = CourierRig.STANDARD_COURIER,
    recommendedLantern = LanternAffinity.AMBER_HEARTH
  ),
  MIST_NAVIGATOR(
    id = "MIST_NAVIGATOR",
    title = "Mist Navigator",
    subtitle = "Cloud-Tide Chart Reader",
    description = "Master of thermal updrafts and pressure squalls, reading unwritten sky trails and celestial compasses where maps dissolve.",
    defaultDrawableRes = R.drawable.img_scene_skiff,
    primaryStatName = "Wind Sense",
    recommendedRig = CourierRig.DAWN_DOCK,
    recommendedLantern = LanternAffinity.STORMGLASS_AZURE
  ),
  LANTERN_SMITH(
    id = "LANTERN_SMITH",
    title = "Lantern Smith",
    subtitle = "Amber Flame Artisan",
    description = "Glassblower and resin-warden who distills volatile amber ores to pierce dense cloud fog and ward off hungry mist predators.",
    defaultDrawableRes = R.drawable.img_scene_salt_cliffs,
    primaryStatName = "Lantern Craft",
    recommendedRig = CourierRig.UNDERTOW_CIVILIAN,
    recommendedLantern = LanternAffinity.AMBER_HEARTH
  ),
  CHAIN_CLIMBER(
    id = "CHAIN_CLIMBER",
    title = "Rust-Chain Climber",
    subtitle = "Anchor Link Scaler",
    description = "Ironclad mountaineer scaling colossal suspension chains between drifting isles, braving freezing gale winds and singing iron cables.",
    defaultDrawableRes = R.drawable.img_chain_climb,
    primaryStatName = "Grit & Altitude",
    recommendedRig = CourierRig.STORM_RUN,
    recommendedLantern = LanternAffinity.COPPER_SUNBURST
  ),
  CODEX_ARCHIVIST(
    id = "CODEX_ARCHIVIST",
    title = "Codex Archivist",
    subtitle = "Keeper of Forgotten Choirs",
    description = "Scholar investigating ancient sky monoliths, deciphering dragon glyphs, and recovering records before the city washes them clean.",
    defaultDrawableRes = R.drawable.img_coil_dragon,
    primaryStatName = "Lore & Wisdom",
    recommendedRig = CourierRig.STANDARD_COURIER,
    recommendedLantern = LanternAffinity.ABYSSAL_INDIGO
  ),
  CLOUD_HARVESTER(
    id = "CLOUD_HARVESTER",
    title = "Cloud Harvester",
    subtitle = "High-Atmosphere Gatherer",
    description = "Acrobatic skimmer sailing the upper troposphere to gather rare aerolith minerals, dew-salt, and luminous lightning-motes.",
    defaultDrawableRes = R.drawable.img_hero_archipelago,
    primaryStatName = "Wind Sense",
    recommendedRig = CourierRig.STORM_RUN,
    recommendedLantern = LanternAffinity.VERMILION_CANDLE
  );

  companion object {
    fun fromId(id: String): SkyfarerArchetype {
      return values().firstOrNull { it.id.equals(id, ignoreCase = true) } ?: SALT_COURIER
    }
  }
}

enum class OriginIsle(
  val id: String,
  val isleName: String,
  val region: String,
  val trait: String,
  val environment: String
) {
  OROS_CLIFFS(
    id = "OROS_CLIFFS",
    isleName = "Salt Cliffs of Oros",
    region = "Outer Northern Rim",
    trait = "Hardened by stinging salt spray and sheer basalt drops.",
    environment = "Sheer chalk cliffs, ancient stone watchtowers, amber harbor lanterns"
  ),
  WHISPERING_ATOLL(
    id = "WHISPERING_ATOLL",
    isleName = "The Whispering Atoll",
    region = "The Floating Steppes",
    trait = "Intuitive kinship with luminous Moon-Koi and soft cloud currents.",
    environment = "Moss-covered stepping stones, rope suspensions, calm thermal ponds"
  ),
  GLASS_CITADEL(
    id = "GLASS_CITADEL",
    isleName = "Citadel of Glass Winds",
    region = "Upper Stratum",
    trait = "Tuned to harmonic wind-chimes and iridescent spire updrafts.",
    environment = "Soaring crystal spires, whistling fluted arches, razor gusts"
  ),
  IRON_BASTION(
    id = "IRON_BASTION",
    isleName = "Iron Bastion of Thal",
    region = "Anchor Core",
    trait = "Relentless endurance forged in chain foundries and dock yards.",
    environment = "Massive anchor links, smelting furnaces, soot-swept mooring towers"
  ),
  SILT_DEPTHS(
    id = "SILT_DEPTHS",
    isleName = "The Lower Veil",
    region = "Under-Cloud Abyss",
    trait = "Stealthy survivor of sunken barges, unmapped stilt markets, and heavy fog.",
    environment = "Sunken barge wrecks, phosphorescent moss, forbidden waters"
  ),
  CELESTIAL_ROOST(
    id = "CELESTIAL_ROOST",
    isleName = "Shrine of the Coil",
    region = "Sacred High Apex",
    trait = "Blessed with calm breathing under raw, uncompressed celestial weather.",
    environment = "White stone ramparts, ancient incense brazens, slumbering sky serpent"
  );

  companion object {
    fun fromId(id: String): OriginIsle {
      return values().firstOrNull { it.id.equals(id, ignoreCase = true) } ?: OROS_CLIFFS
    }
  }
}

enum class LanternAffinity(
  val id: String,
  val flameName: String,
  val colorHex: String,
  val matchingMode: LanternMode,
  val auraEffect: String
) {
  AMBER_HEARTH(
    id = "AMBER_HEARTH",
    flameName = "Amber Hearthstone",
    colorHex = "#FFB300",
    matchingMode = LanternMode.AMBER_LAMP,
    auraEffect = "Radiates comforting warmth that prevents frost on glider wings and reveals harbor secrets."
  ),
  STORMGLASS_AZURE(
    id = "STORMGLASS_AZURE",
    flameName = "Stormglass Azure",
    colorHex = "#4FC3F7",
    matchingMode = LanternMode.BLUE_GLASS,
    auraEffect = "Piercing cyan beam that reveals hidden courier paths, thermal updrafts, and navigation runes."
  ),
  ABYSSAL_INDIGO(
    id = "ABYSSAL_INDIGO",
    flameName = "Abyssal Indigo",
    colorHex = "#9FA8DA",
    matchingMode = LanternMode.MOONLIGHT,
    auraEffect = "Soft ethereal glow that resonates with ancient dragon lore, moonlight memory, and erased truths."
  ),
  COPPER_SUNBURST(
    id = "COPPER_SUNBURST",
    flameName = "Copper Sunburst",
    colorHex = "#FF7043",
    matchingMode = LanternMode.STANDARD,
    auraEffect = "Crackling spark-flame that dazzles predatory cloud-eels and illuminates sheer rock faces."
  ),
  VERMILION_CANDLE(
    id = "VERMILION_CANDLE",
    flameName = "Vermilion Signal",
    colorHex = "#E53935",
    matchingMode = LanternMode.STANDARD,
    auraEffect = "High-visibility courier beacon visible across leagues of fog to signal urgent passage."
  );

  companion object {
    fun fromId(id: String): LanternAffinity {
      return values().firstOrNull { it.id.equals(id, ignoreCase = true) } ?: AMBER_HEARTH
    }
  }
}

data class SkyfarerCharacter(
  val id: String,
  val name: String,
  val title: String,
  val archetype: SkyfarerArchetype,
  val originIsle: OriginIsle,
  val lanternAffinity: LanternAffinity,
  val gliderRig: CourierRig,
  val stats: CharacterStats,
  val signatureRelic: String,
  val personalityQuirk: String,
  val motivation: String,
  val backstory: String,
  val customQuote: String,
  val portraitDrawableRes: Int = archetype.defaultDrawableRes,
  val customPortraitPath: String? = null,
  val isAiGenerated: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
) {
  fun toJsonObject(): JSONObject {
    return JSONObject().apply {
      put("id", id)
      put("name", name)
      put("title", title)
      put("archetypeId", archetype.id)
      put("originIsleId", originIsle.id)
      put("lanternAffinityId", lanternAffinity.id)
      put("gliderRigName", gliderRig.name)
      put("stats", stats.toJsonObject())
      put("signatureRelic", signatureRelic)
      put("personalityQuirk", personalityQuirk)
      put("motivation", motivation)
      put("backstory", backstory)
      put("customQuote", customQuote)
      put("portraitDrawableRes", portraitDrawableRes)
      put("customPortraitPath", customPortraitPath ?: "")
      put("isAiGenerated", isAiGenerated)
      put("createdAt", createdAt)
    }
  }

  companion object {
    fun fromJsonObject(json: JSONObject): SkyfarerCharacter {
      val archetype = SkyfarerArchetype.fromId(json.optString("archetypeId", "SALT_COURIER"))
      val rig = try {
        CourierRig.valueOf(json.optString("gliderRigName", CourierRig.STANDARD_COURIER.name))
      } catch (e: Exception) {
        archetype.recommendedRig
      }

      val path = json.optString("customPortraitPath", "")

      return SkyfarerCharacter(
        id = json.optString("id", "char_${System.currentTimeMillis()}"),
        name = json.optString("name", "Unnamed Courier"),
        title = json.optString("title", "Skyfarer"),
        archetype = archetype,
        originIsle = OriginIsle.fromId(json.optString("originIsleId", "OROS_CLIFFS")),
        lanternAffinity = LanternAffinity.fromId(json.optString("lanternAffinityId", "AMBER_HEARTH")),
        gliderRig = rig,
        stats = CharacterStats.fromJsonObject(json.optJSONObject("stats") ?: JSONObject()),
        signatureRelic = json.optString("signatureRelic", "Sealed Brass Message Tube"),
        personalityQuirk = json.optString("personalityQuirk", "Checks wind direction with a pinch of salt."),
        motivation = json.optString("motivation", "Delivering a promise against the weather."),
        backstory = json.optString("backstory", "Raised between the salt cliffs and cloud sea."),
        customQuote = json.optString("customQuote", "The sky takes what it wants, but we keep our promises."),
        portraitDrawableRes = json.optInt("portraitDrawableRes", archetype.defaultDrawableRes),
        customPortraitPath = if (path.isBlank()) null else path,
        isAiGenerated = json.optBoolean("isAiGenerated", false),
        createdAt = json.optLong("createdAt", System.currentTimeMillis())
      )
    }

    fun listToJson(list: List<SkyfarerCharacter>): String {
      val array = JSONArray()
      list.forEach { array.put(it.toJsonObject()) }
      return array.toString()
    }

    fun listFromJson(jsonStr: String): List<SkyfarerCharacter> {
      val list = mutableListOf<SkyfarerCharacter>()
      try {
        val array = JSONArray(jsonStr)
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          list.add(fromJsonObject(obj))
        }
      } catch (e: Exception) {
        // Return empty on parse error
      }
      return list
    }
  }
}
