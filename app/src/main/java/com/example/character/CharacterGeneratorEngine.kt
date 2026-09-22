package com.example.character

import java.util.UUID
import kotlin.random.Random

object CharacterGeneratorEngine {

  private val firstNames = listOf(
    "Sera", "Kaelen", "Zephyr", "Tarek", "Vesper", "Lyra", "Silas", "Corin",
    "Tavi", "Rowan", "Brann", "Jarek", "Cassia", "Fenn", "Nomi", "Vance",
    "Maren", "Kest", "Elora", "Doran", "Rhea", "Orin", "Brynna", "Malik"
  )

  private val surnames = listOf(
    "Venn", "Halder", "Locke", "Corvus", "Gale", "Morrow", "Vane",
    "Salt-Singer", "Chasm-Walker", "Cloud-Drift", "Iron-Spur", "Mist-Weaver",
    "Storm-Watcher", "Batten-Link", "Rook", "Thorn", "Pell", "Oros"
  )

  private val titlePrefixes = listOf(
    "The Cloud-Skimmer", "Lanternkeeper", "Warden of the Seventh Chasm",
    "Kite of the Upper Veil", "Voice of Glass Winds", "Anchor of Thal",
    "The Salt-Eyed", "Navigator of the Deep Mist", "Glider of the Red Dawn",
    "The Untethered", "The Echo-Finder", "Keeper of the Sealed Tubes"
  )

  private val signatureRelics = listOf(
    "Carved Salt-Bone Flute that mimics high shear whistling",
    "Grandfather's Weighted Iron Grappling Hook with frayed sky-silk line",
    "Brass Pressure Gauge with etched markings from three censuses ago",
    "Jar of Living Phosphor Moon-Motes gathered from the Atoll",
    "Spool of Gilded Copper Signal Wire wrapped in tarred sailcloth",
    "Fractured Stormglass Prism that glows when lightning walks the cables",
    "Auditor's Tarnished Wax Seal salvaged from a sunken ledger-barge",
    "Locket containing dried blue mountain-berries from an isle that fell",
    "Pair of Stitched Cloud-Goat Leather Gloves with scarred palms",
    "Heavy Brass Key that remembers a lock that no longer exists"
  )

  private val personalityQuirks = listOf(
    "Refuses to sleep indoors; always rigs a hammock beneath the skiff keel.",
    "Tastes the wind before every glider dive to gauge salt and barometric density.",
    "Carries a jar of rainwater from their childhood isle and never drinks from it.",
    "Whispers an apology to the air whenever deploying storm-brakes in high squalls.",
    "Counts the suspension chain links aloud when climbing through blinding fog.",
    "Refuses to sign written contracts, trusting only a nod and an exchanged wax bead.",
    "Never cuts their hair until an urgent courier delivery is successfully fulfilled.",
    "Keeps a pet cloud-swallow inside their sailcoat lining that chirps at downdrafts."
  )

  private val motivations = listOf(
    "Searching the Lower Salt abyss for the sunken storm-skiff of a lost sibling.",
    "Determined to deliver a sealed brass letter whose recipient vanished ten years ago.",
    "Working off an impossible iron-dock debt to reclaim their family's glider workshop.",
    "Plotting to chart an unmapped sky route across the Great Maw where no courier has returned.",
    "Protecting a young Moon-Koi whose luminescent memory holds the true history of the falls.",
    "Escaping an auditor's writ by staying perpetually aloft in the high wind currents.",
    "Seeking the legendary slumbering Sky Dragon to witness raw, uncompressed celestial weather."
  )

  fun rollRandomName(): String {
    val first = firstNames.random()
    val last = surnames.random()
    return "$first $last"
  }

  fun rollRandomTitle(archetype: SkyfarerArchetype, origin: OriginIsle): String {
    val specificTitles = when (archetype) {
      SkyfarerArchetype.SALT_COURIER -> listOf("The Salt-Skimmer", "Courier of the Long Chasm", "The Wax-Sealed")
      SkyfarerArchetype.MIST_NAVIGATOR -> listOf("Wayfinder of the Gray Veil", "Compass of the Squalls", "The Cloud-Tide Eye")
      SkyfarerArchetype.LANTERN_SMITH -> listOf("Warden of the Amber Chimney", "Flame-Blower of Oros", "Spark-Keeper")
      SkyfarerArchetype.CHAIN_CLIMBER -> listOf("Iron-Grip of Thal", "The Chain-Leaper", "Anchor-Master")
      SkyfarerArchetype.CODEX_ARCHIVIST -> listOf("Voice of the Forgotten Choirs", "The Ledger-Breaker", "Chronicle-Seeker")
      SkyfarerArchetype.CLOUD_HARVESTER -> listOf("Troposphere Skimmer", "The Aerolith Diver", "Dew-Reaper")
    }
    return specificTitles.random()
  }

  fun generateProceduralStats(archetype: SkyfarerArchetype): CharacterStats {
    // 28 total points distributed across 5 attributes (average 5.6 per stat)
    val base = mutableMapOf(
      "windSense" to 4,
      "gritAltitude" to 4,
      "lanternCraft" to 4,
      "loreWisdom" to 4,
      "tillerAgility" to 4
    )

    // Archetype primary boost
    when (archetype) {
      SkyfarerArchetype.SALT_COURIER -> {
        base["tillerAgility"] = 8
        base["windSense"] = 6
      }
      SkyfarerArchetype.MIST_NAVIGATOR -> {
        base["windSense"] = 8
        base["loreWisdom"] = 6
      }
      SkyfarerArchetype.LANTERN_SMITH -> {
        base["lanternCraft"] = 8
        base["gritAltitude"] = 6
      }
      SkyfarerArchetype.CHAIN_CLIMBER -> {
        base["gritAltitude"] = 8
        base["tillerAgility"] = 6
      }
      SkyfarerArchetype.CODEX_ARCHIVIST -> {
        base["loreWisdom"] = 8
        base["lanternCraft"] = 6
      }
      SkyfarerArchetype.CLOUD_HARVESTER -> {
        base["windSense"] = 7
        base["tillerAgility"] = 7
      }
    }

    // Distribute remaining points (up to 28 total)
    var currentSum = base.values.sum()
    var loopBudget = 0
    while (currentSum < 28 && loopBudget < 50) {
      loopBudget++
      val eligibleKeys = base.keys.filter { (base[it] ?: 4) < 9 }
      if (eligibleKeys.isEmpty()) break
      val key = eligibleKeys.random()
      base[key] = (base[key] ?: 4) + 1
      currentSum++
    }

    return CharacterStats(
      windSense = base["windSense"] ?: 5,
      gritAltitude = base["gritAltitude"] ?: 5,
      lanternCraft = base["lanternCraft"] ?: 5,
      loreWisdom = base["loreWisdom"] ?: 5,
      tillerAgility = base["tillerAgility"] ?: 5
    )
  }

  fun generateProceduralBackstory(
    name: String,
    archetype: SkyfarerArchetype,
    origin: OriginIsle,
    relic: String,
    motivation: String
  ): String {
    val originSentence = when (origin) {
      OriginIsle.OROS_CLIFFS -> "Born upon the chalky windward precipice of the Salt Cliffs of Oros, $name grew up measuring life by the rhythm of passing squalls and the groan of moored barges."
      OriginIsle.WHISPERING_ATOLL -> "Raised among the moss-grown suspension bridges and thermal pools of the Whispering Atoll, $name learned early that the sky is not empty, but teeming with ancient memory."
      OriginIsle.GLASS_CITADEL -> "Trained beneath the whistling spires and harmonic flutes of the Citadel of Glass Winds, $name developed an uncanny sensitivity to subtle changes in atmospheric pressure."
      OriginIsle.IRON_BASTION -> "Tempered in the shadow of Thal's colossal iron anchor chains and roaring dock foundries, $name was taught that only honest iron and tight rigging can defy the abyss."
      OriginIsle.SILT_DEPTHS -> "A child of the murky Lower Veil, $name spent years navigating sunken barge hulks and unmapped stilt channels where the sky turns to mist."
      OriginIsle.CELESTIAL_ROOST -> "Having spent formative years in contemplative altitude at the Shrine of the Coil, $name is one of the few who has gazed into the eye of the slumbering sky serpent."
    }

    val roleSentence = when (archetype) {
      SkyfarerArchetype.SALT_COURIER -> "As a licensed Salt Courier, their glider wings have caught drafts across seven major chasms, carrying messages sealed with wax that must outrace the weather."
      SkyfarerArchetype.MIST_NAVIGATOR -> "Steering by star-angles and barometric drifts where landmarks dissolve, they serve as the indispensable compass for those daring to cross unmapped currents."
      SkyfarerArchetype.LANTERN_SMITH -> "Armed with blown stormglass canisters and purified amber oil, their lanterns cut through the thickest whiteouts to shepherd stragglers to safety."
      SkyfarerArchetype.CHAIN_CLIMBER -> "Specializing in the perilous vertical ascents along frozen suspension cables, their scarred knuckles and grappling gear hold the archipelago's lifeline together."
      SkyfarerArchetype.CODEX_ARCHIVIST -> "Rescuing damp parchment leaves and forgotten songs before the auditors can sanitize the historical record, they treat memory as the ultimate sanctuary."
      SkyfarerArchetype.CLOUD_HARVESTER -> "Skimming the razor updrafts of the upper troposphere, they harvest the rare dew-crystals and aerolith sparks that fuel the archipelago's lamps."
    }

    val goalSentence = "Now carrying $relic, they remain driven by one resolute vow: $motivation"

    return "$originSentence $roleSentence $goalSentence"
  }

  fun generateProceduralQuote(archetype: SkyfarerArchetype, name: String): String {
    return when (archetype) {
      SkyfarerArchetype.SALT_COURIER -> "The wind doesn't read the address on the tube. You dive when the thermal answers, or you fall."
      SkyfarerArchetype.MIST_NAVIGATOR -> "There are no empty places in the cloud sea. Only places where you forgot how to listen."
      SkyfarerArchetype.LANTERN_SMITH -> "A flame isn't just light out here; it's a hearth you carry against the cold."
      SkyfarerArchetype.CHAIN_CLIMBER -> "Every link was forged by someone whose name is gone. Trust the iron, but check your carabiner."
      SkyfarerArchetype.CODEX_ARCHIVIST -> "If you let them erase the old roads from the ledger, the city forgets how to breathe."
      SkyfarerArchetype.CLOUD_HARVESTER -> "High enough up, the clouds are solid as marble and twice as cruel. That's where the best salt lives."
    }
  }

  fun generateFullProceduralCharacter(
    archetype: SkyfarerArchetype? = null,
    origin: OriginIsle? = null,
    lantern: LanternAffinity? = null
  ): SkyfarerCharacter {
    val selectedArchetype = archetype ?: SkyfarerArchetype.values().random()
    val selectedOrigin = origin ?: OriginIsle.values().random()
    val selectedLantern = lantern ?: selectedArchetype.recommendedLantern
    val selectedRig = selectedArchetype.recommendedRig

    val name = rollRandomName()
    val title = rollRandomTitle(selectedArchetype, selectedOrigin)
    val relic = signatureRelics.random()
    val quirk = personalityQuirks.random()
    val motivation = motivations.random()
    val stats = generateProceduralStats(selectedArchetype)
    val backstory = generateProceduralBackstory(name, selectedArchetype, selectedOrigin, relic, motivation)
    val quote = generateProceduralQuote(selectedArchetype, name)

    return SkyfarerCharacter(
      id = "skyfarer_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
      name = name,
      title = title,
      archetype = selectedArchetype,
      originIsle = selectedOrigin,
      lanternAffinity = selectedLantern,
      gliderRig = selectedRig,
      stats = stats,
      signatureRelic = relic,
      personalityQuirk = quirk,
      motivation = motivation,
      backstory = backstory,
      customQuote = quote,
      portraitDrawableRes = selectedArchetype.defaultDrawableRes,
      isAiGenerated = false,
      createdAt = System.currentTimeMillis()
    )
  }
}
