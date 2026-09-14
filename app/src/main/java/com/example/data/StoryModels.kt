package com.example.data

enum class LanternMode(val displayName: String, val description: String) {
  STANDARD("Courier View", "Standard night reading with honest lamps."),
  BLUE_GLASS("Blue Glass", "Reveals hidden routes, paths, and courier tactical notes."),
  AMBER_LAMP("Amber Lamp", "Reveals harbor contracts, tavern whispers, and sanctuary lore."),
  MOONLIGHT("Residual Moon", "Reveals ethereal moon-koi vision, unspoken truths, and sea currents.")
}

enum class CourierRig(
  val displayName: String,
  val subtitle: String,
  val description: String,
  val defenseBonus: String
) {
  STANDARD_COURIER(
    "Standard Courier Rig",
    "Indigo Cloak & Vermilion Sash",
    "Indigo cloak stitched with faint gold, vermilion sash in the formal courier hitch, brass message tubes, and blue-ribbed staff-lantern.",
    "+Official Authority"
  ),
  DAWN_DOCK(
    "Dawn-Dock Disguise",
    "Sand-Grey Oilskin",
    "Sand-grey oilskin that eats lamplight, wrapped lower face, muted rope belt, and a blue-glass stub concealed in the sleeve.",
    "+Stealth & Concealment"
  ),
  STORM_RUN(
    "Storm-Run Rig",
    "Sailcoat & Lightning Harness",
    "Short dark sailcoat, tether harness, grappling hook, and lightning-proof gloves that drink cloud electrical discharge.",
    "+Chain Climbing Grip"
  ),
  UNDERTOW_CIVILIAN(
    "Undertow Civilian Cover",
    "Plum Waistcoat & Concealed Sheath",
    "Plum waistcoat over dark shirt, beads at the throat, sash tied low as a scrap, and a concealed sheath at the small of the back.",
    "+Tavern Discretion"
  )
}

data class AnnotatedTerm(
  val term: String,
  val category: String,
  val summary: String,
  val detail: String
)

data class StoryParagraph(
  val id: String,
  val text: String,
  val blueGlassSecret: String? = null,
  val amberLore: String? = null,
  val moonlightTruth: String? = null,
  val annotatedTerms: List<AnnotatedTerm> = emptyList()
)

data class ChoiceOption(
  val id: String,
  val text: String,
  val consequenceText: String,
  val rewardItem: CourierItem? = null,
  val reputationTag: String? = null
)

data class StoryChoice(
  val id: String,
  val prompt: String,
  val options: List<ChoiceOption>
)

data class CourierItem(
  val id: String,
  val name: String,
  val description: String,
  val iconName: String
)

data class Chapter(
  val id: Int,
  val actNumber: Int,
  val actTitle: String,
  val title: String,
  val subtitle: String? = null,
  val imageRes: Int? = null,
  val imageCaption: String? = null,
  val paragraphs: List<StoryParagraph>,
  val choice: StoryChoice? = null
)

data class CharacterProfile(
  val id: String,
  val name: String,
  val title: String,
  val role: String,
  val description: String,
  val quote: String
)

data class LocationNode(
  val id: String,
  val name: String,
  val subtitle: String,
  val description: String,
  val lanternStatus: String,
  val chapterId: Int
)
