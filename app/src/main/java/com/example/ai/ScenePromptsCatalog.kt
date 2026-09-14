package com.example.ai

import com.example.R

data class CuratedScenePrompt(
  val id: String,
  val chapterId: Int,
  val title: String,
  val prompt: String,
  val defaultStyle: PainterlyStyle,
  val defaultDrawableRes: Int? = null
)

object ScenePromptsCatalog {
  val curatedScenes: List<CuratedScenePrompt> = listOf(
    CuratedScenePrompt(
      id = "ch1_skiff",
      chapterId = 0,
      title = "The Skiff in Golden Cloudsea",
      prompt = "Wooden wind-skiff with billowing amber-copper canvas sails gliding over an endless sea of rolling golden sunset clouds, distant floating island silhouettes, atmospheric fantasy landscape",
      defaultStyle = PainterlyStyle.GHIBLI_WIND,
      defaultDrawableRes = R.drawable.img_scene_skiff
    ),
    CuratedScenePrompt(
      id = "ch2_lantern",
      chapterId = 1,
      title = "The Amber Lantern of Salt Cliffs",
      prompt = "Ancient stone watchtower on a sheer floating cliff edge, glowing amber lantern beam cutting through midnight blue cloud mist, starry sky above the cloud abyss",
      defaultStyle = PainterlyStyle.AMBER_CHIAROSCURO,
      defaultDrawableRes = R.drawable.img_scene_salt_cliffs
    ),
    CuratedScenePrompt(
      id = "ch3_chain_climb",
      chapterId = 2,
      title = "Ascending the Rust Chains",
      prompt = "A daring courier climbing massive iron suspension chains between two floating mossy islands, swirling mist and wind currents, dramatic scale and depth",
      defaultStyle = PainterlyStyle.PAINTERLY_OIL,
      defaultDrawableRes = R.drawable.img_chain_climb
    ),
    CuratedScenePrompt(
      id = "ch4_coil_dragon",
      chapterId = 3,
      title = "The Slumbering Sky Serpent",
      prompt = "Ancient celestial serpent sleeping coiled around a floating ruined citadel tower, glowing scales blending into thunderstorm clouds, painterly fantasy concept art",
      defaultStyle = PainterlyStyle.PAINTERLY_OIL,
      defaultDrawableRes = R.drawable.img_coil_dragon
    ),
    CuratedScenePrompt(
      id = "ch5_archipelago_dawn",
      chapterId = 4,
      title = "Dawn Over the Thousand Isles",
      prompt = "Panoramic sunrise over the floating Skybound Archipelago, waterfalls pouring into golden cloud banks below, flocks of migrating white sky-rays",
      defaultStyle = PainterlyStyle.PARCHMENT_WATERCOLOR,
      defaultDrawableRes = R.drawable.img_hero_archipelago
    )
  )

  fun getPromptsForChapter(chapterId: Int): List<CuratedScenePrompt> {
    return curatedScenes.filter { it.chapterId == chapterId }
  }
}
