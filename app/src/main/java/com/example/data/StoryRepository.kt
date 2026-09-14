package com.example.data

import com.example.R

object StoryRepository {

  val glossaryTerms: List<AnnotatedTerm> = listOf(
    AnnotatedTerm(
      term = "Moon-Koi",
      category = "Creature",
      summary = "Luminous celestial fish that swim through the cloud sea.",
      detail = "Unlike ordinary fish, moon-koi do not swim in water; they swim in cloud, feeding on residual moonlight. Young ones choose a courier companion and cannot be caged without losing their navigational truth. Elder moon-koi are the size of continents and swim the aurora."
    ),
    AnnotatedTerm(
      term = "Residual Moonlight",
      category = "Phenomenon",
      summary = "Leftover silver luminescence trapped in cloud vapor after the moon has moved on.",
      detail = "The fundamental medium of navigation in the Skybound Archipelago. Most people cannot see it. Couriers and moon-koi track it to find honest routes through folding currents."
    ),
    AnnotatedTerm(
      term = "Blue Glass",
      category = "Rule",
      summary = "Official navigation lamps signaling 'You may move'.",
      detail = "Hooked along the wet black-lacquer avenues. Ribbed blue-glass chimneys throw a focused beam that marks safe, active passages across platforms and chain-bridges."
    ),
    AnnotatedTerm(
      term = "Amber Light",
      category = "Rule",
      summary = "Sanctuary and doorway lamps signaling 'You may stop'.",
      detail = "Amber light acts as a contract in the night market. To step under an amber awning means you are temporarily a protected guest of whoever pays for the oil. Neutral ground and warm ovens rely on amber."
    ),
    AnnotatedTerm(
      term = "Scale Compass",
      category = "Relic",
      summary = "A brass cup bearing a needle carved from a moon-koi scale.",
      detail = "It does not point north—north is a flatlander myth about ground that stays. The needle quivers toward residual moonlight, detecting when routes are being 'unwritten' or closed."
    ),
    AnnotatedTerm(
      term = "Storm Anchor Shrine",
      category = "Geography",
      summary = "A colossal hanging temple-island anchoring the Archipelago's central chains.",
      detail = "Black stone colonnade veined with teal. Here, massive iron collars hold the city's cables. It is both a sacred sanctuary and an enormous mechanical stabilizer."
    ),
    AnnotatedTerm(
      term = "The Coil",
      category = "Legend",
      summary = "A colossal white dragon wrapped around the central shrine island.",
      detail = "Cloud given a spine. White scales the size of shutters, branching antlers, and amber-gold eyes. It sleeps around the shrine like a living chain that grew opinions."
    ),
    AnnotatedTerm(
      term = "The Undertow Den",
      category = "Geography",
      summary = "A circular underground hold inside a decommissioned grain hauler barge.",
      detail = "Run by Olla. Drapes of plum and teal, live-gold moth cages, thick wagon-wheel porthole looking out over the drop. Strictly neutral ground where safety is rented by the cup."
    ),
    AnnotatedTerm(
      term = "Storm Jars",
      category = "Relic",
      summary = "Heavy glass jars sealing compressed violet and teal tempest weather.",
      detail = "Illicit weather-craft sold in clandestine stalls. Used by conspirators like the Office to tilt platforms, shake chains, and force districts to kneel."
    ),
    AnnotatedTerm(
      term = "The Office",
      category = "Faction",
      summary = "A shadowy syndicate of ambitious clerks seeking to regulate and control all routes.",
      detail = "They believe the Archipelago has too many hanging promises and want to force districts to kneel so the city can be recut, licensed, and owned like a ledger."
    ),
    AnnotatedTerm(
      term = "Lower Salt",
      category = "Geography",
      summary = "The true ocean beneath the cloud sea.",
      detail = "A forgotten, forbidden realm that flatlanders and hanging people pretend is blank. Filled with stilt markets, sunken barge-graves, and families who fell."
    ),
    AnnotatedTerm(
      term = "Warden Pell's Collar",
      category = "Relic",
      summary = "The central iron lock-collar at Storm Anchor Shrine.",
      detail = "Engraved with the name PELL. Warden Pell died during a previous census, but the lock still believes in him and only unlocks with his brass key."
    )
  )

  val characters: List<CharacterProfile> = listOf(
    CharacterProfile(
      id = "char_sera",
      name = "Sera Venn",
      title = "The Moon-Koi Courier",
      role = "Protagonist",
      description = "Raised on the salvage skiff Third Rib after her father was lost to the cloud sea. Practical, observant, wearing an indigo cloak and formal vermilion sash. She trusts hardware, residue, and promises kept.",
      quote = "Every route is temporary. Every delivery is a promise made against the weather."
    ),
    CharacterProfile(
      id = "char_nami",
      name = "Nami",
      title = "Young Moon-Koi Companion",
      role = "Celestial Navigator",
      description = "Pale as a lantern chimney, translucent as water, with a single vermilion mark at her gill like sealing wax. She feeds on residual moonlight, detects erased roads, and refuses every cage.",
      quote = "Nami drew a pale comma in the dark, and the comma meant this way, and also for now."
    ),
    CharacterProfile(
      id = "char_olla",
      name = "Olla of the Hold",
      title = "Keeper of the Undertow Den",
      role = "Neutral Sanctuary Broker",
      description = "Arms mapped with old rope burns. Governs the Den with the precision of a blade. Hates weather in her hold and chalks blunt proverbs on her jar shelf.",
      quote = "I don't keep things warm for ghosts. So don't become one."
    ),
    CharacterProfile(
      id = "char_tavi",
      name = "Tavi Quill",
      title = "Registry Scribe",
      role = "Allied Cartographer",
      description = "Nineteen, cropped hair under a clerk's cap, ink to the first knuckle, left wrist wrapped in wet indigo thread. Copies obedient charts for the Ward and true charts for couriers.",
      quote = "I don't invoice drowning. Alleys have better light after moonrise anyway."
    ),
    CharacterProfile(
      id = "char_len",
      name = "Len Vale",
      title = "Sailcloth Row Chain-Rigger",
      role = "Hardware Specialist",
      description = "Eleven years riding iron links. Has three broken fingers, a collar-bruise like ugly jewelry, and knows which pins are theater and which pins hold the city's weight.",
      quote = "Ward paid for a brace on the iron and a lecture on my posture. Didn't pay for the fingers."
    ),
    CharacterProfile(
      id = "char_marrow",
      name = "Sister Marrow",
      title = "Office Dissident & Temple Scribe",
      role = "Theologian of the Roof",
      description = "Climbs the shrine roof with blank paper, believing the Coil can be addressed without being coerced. Maintains a private register of the living.",
      quote = "Belief that cannot survive a burning is only furniture with better lighting."
    ),
    CharacterProfile(
      id = "char_hane",
      name = "Hane",
      title = "The Sailor Who Jumped",
      role = "Ocean Veteran",
      description = "Sunned and salt-marked, the sailor who survived the descent of the galleon Letter of Descent to the true sea and climbed back up the chain to tell the tale.",
      quote = "Ship left the sky. Lower Salt isn't empty. There are markets that drown instead of hang."
    ),
    CharacterProfile(
      id = "char_irix",
      name = "Captain Irix Halder",
      title = "Barge Magnate",
      role = "Ambitious Merchant",
      description = "Smiles with only the public half of his mouth. Buys time and missing cuts, navigating the border between convenience and complicity.",
      quote = "False paths are a market, Venn. You work in a market. Don't perform surprise."
    ),
    CharacterProfile(
      id = "char_kelm",
      name = "Auditor Kelm",
      title = "Bazaar Ward Chief",
      role = "Imperial Bureaucrat",
      description = "Loves ledgers more than outcomes. Attempts to requisition memory, license moon-koi as public safety instruments, and sanitize the night.",
      quote = "Memory is a tool. Tools can be requisitioned when a city nearly kneels."
    ),
    CharacterProfile(
      id = "char_coil",
      name = "The Coil",
      title = "The Shrine Dragon",
      role = "Ancient Arbiter",
      description = "A living rampart around the shrine island. White scales, golden eyes, branched antlers. Breathes raw uncompressed weather.",
      quote = "The Coil did not read. It breathed, and the breath was weather that hadn't been compressed first."
    )
  )

  val locationNodes: List<LocationNode> = listOf(
    LocationNode(
      id = "loc_bazaar",
      name = "The Lantern Bazaar",
      subtitle = "Black-Lacquer Avenue",
      description = "The pulsing heart of the Archipelago. Wet black lacquer shining like fresh ink between hulls, blue glass lanterns guiding moving couriers, and amber lamps sheltering merchants of spice, paper, and pepper broth.",
      lanternStatus = "Blue Light & Amber Awnings",
      chapterId = 1
    ),
    LocationNode(
      id = "loc_undertow",
      name = "The Undertow Den",
      subtitle = "Hold of the Grain Barge",
      description = "A circular chamber deep in the belly of an old barge. Thick circular porthole looking out over the drop, drapes of plum and teal, live-gold moths in cages, and bitter smoky tea.",
      lanternStatus = "Amber Sheltered Taps",
      chapterId = 6
    ),
    LocationNode(
      id = "loc_chain_road",
      name = "The Great Chain Road",
      subtitle = "Outer Iron Face",
      description = "Colossal links the size of doors running between floating towers into the cloud sea. Cold wind, singing iron under strain, and sheet lightning walking the distant cables.",
      lanternStatus = "Dark Interval & Stuttering Blue",
      chapterId = 9
    ),
    LocationNode(
      id = "loc_shrine",
      name = "Storm Anchor Shrine",
      subtitle = "Pell's Collar Gallery",
      description = "A colonnade of black stone veined with teal, anchoring dozens of immense cables. Contains Warden Pell's locked collar and the inner well where moonlight unmade stolen weather.",
      lanternStatus = "Sacred Colonnade",
      chapterId = 10
    ),
    LocationNode(
      id = "loc_sailcloth",
      name = "Sailcloth Row",
      subtitle = "The Slack Platform",
      description = "A working timber district where riggers mend cables and laundries flutter. Severed by the Office's sabotage until braced by an ugly salvage door-hook.",
      lanternStatus = "Braced & Relit",
      chapterId = 18
    ),
    LocationNode(
      id = "loc_measure",
      name = "Barge Measure (formerly Plenty)",
      subtitle = "The Office's Secret Well",
      description = "Hidden in an uncharted lee. Inside was a ribbed glass tank forcing violet lightning and moonlight into a grotesque marriage before Sera and Nami unseamed it.",
      lanternStatus = "Blackened Porthole Broken",
      chapterId = 22
    ),
    LocationNode(
      id = "loc_lower_salt",
      name = "Lower Salt",
      subtitle = "The True Ocean Below",
      description = "Beneath the cloud floor lies true water. Stilt markets at low tide, drowned barge-graves, and the wake of the galleon Letter of Descent heading toward Joss Venn.",
      lanternStatus = "True Tide & Distant Moon",
      chapterId = 26
    )
  )

  val allItems: List<CourierItem> = listOf(
    CourierItem(
      id = "item_staff_lantern",
      name = "Blue-Ribbed Staff-Lantern",
      description = "Darkened brass enclosing a globe of ribbed blue glass. Casts a narrow safe-zone claiming: this person is working.",
      iconName = "lantern"
    ),
    CourierItem(
      id = "item_scale_compass",
      name = "Moon-Koi Scale Compass",
      description = "Carried in a brass cup at the belt. The needle is a sliver of moon-koi scale set in glass, pointing to residual moonlight.",
      iconName = "compass"
    ),
    CourierItem(
      id = "item_vermilion_sash",
      name = "Vermilion Courier Sash",
      description = "Tied in the formal courier hitch across the chest. Later adorned with the shrine's blessing-mark.",
      iconName = "sash"
    ),
    CourierItem(
      id = "item_brass_tubes",
      name = "Sealed Brass Message Tubes",
      description = "Lightweight ribbed brass tubes riding against ribs, sealed with honest amber wax.",
      iconName = "tube"
    ),
    CourierItem(
      id = "item_glass_packet",
      name = "Warm Blue-Glass Packet",
      description = "Addressed to Warden Pell. Sealed in ribbed blue glass that pulses like a second heart with bad intentions.",
      iconName = "packet"
    ),
    CourierItem(
      id = "item_favor_token",
      name = "Black Lacquer Favor-Token",
      description = "Polished black lacquer containing a single amber inclusion that gleams like a trapped spark.",
      iconName = "token"
    ),
    CourierItem(
      id = "item_pell_key",
      name = "Warden Pell's Brass Key",
      description = "A thin brass key blackened with handling that teaches a locked iron collar to remember the city.",
      iconName = "key"
    ),
    CourierItem(
      id = "item_candied_peel",
      name = "Stall Auntie's Candied Peel",
      description = "Bought from the glass-eyed woman. Sugar keeps the mouth from telling the wind too much.",
      iconName = "peel"
    ),
    CourierItem(
      id = "item_lower_salt_chart",
      name = "Tavi's Lower Salt Chart",
      description = "Drawn on damp fiber with un-erased ink. Marks the barge-graves of Venn-water and the route to Joss Venn.",
      iconName = "map"
    )
  )

  // Returns all chapters of Book One
  fun getChapters(): List<Chapter> = StoryChaptersData.chapters
}
