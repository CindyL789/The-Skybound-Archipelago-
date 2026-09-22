package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.StoryRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Skybound", appName)
  }

  @Test
  fun `verify story chapters loaded`() {
    val chapters = StoryRepository.getChapters()
    assertTrue(chapters.isNotEmpty())
    assertEquals(17, chapters.size) // 17 comprehensive book chapters across 7 acts
    val prologue = chapters.first()
    assertTrue(prologue.title.contains("Prologue"))
  }

  @Test
  fun `verify characters and glossary terms`() {
    assertTrue(StoryRepository.characters.isNotEmpty())
    val sera = StoryRepository.characters.find { it.id == "char_sera" }
    assertNotNull(sera)
    assertEquals("Sera Venn", sera?.name)

    val moonKoi = StoryRepository.glossaryTerms.find { it.term == "Moon-Koi" }
    assertNotNull(moonKoi)
  }

  @Test
  fun `verify audio preferences and presets`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.example.data.CourierPreferences(context)

    // Check defaults
    assertEquals(0.7f, prefs.ambientVolume, 0.01f)
    assertEquals("CLOUD_SEA", prefs.ambientPresetName)
    assertTrue(prefs.ambientDucking)
    assertEquals(1.0f, prefs.narrationSpeed, 0.01f)

    // Update settings
    prefs.ambientVolume = 0.85f
    prefs.ambientPresetName = com.example.audio.SoundscapePreset.SALT_RAIN.name
    prefs.ambientDucking = false
    prefs.narrationSpeed = 1.25f

    assertEquals(0.85f, prefs.ambientVolume, 0.01f)
    assertEquals("SALT_RAIN", prefs.ambientPresetName)
    org.junit.Assert.assertFalse(prefs.ambientDucking)
    assertEquals(1.25f, prefs.narrationSpeed, 0.01f)
  }

  @Test
  fun `verify narrator personas and voice preferences`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.example.data.CourierPreferences(context)

    // Check defaults
    assertEquals("COURIER", prefs.narrationPersonaId)
    org.junit.Assert.assertNull(prefs.narrationVoiceName)

    // Verify persona lookups and definitions
    val courier = com.example.audio.NarratorPersona.fromId("COURIER")
    assertEquals("The Salt Courier", courier.title)
    assertEquals(0.92f, courier.speechRate, 0.01f)
    assertEquals(0.88f, courier.pitch, 0.01f)

    val archivist = com.example.audio.NarratorPersona.fromId("ARCHIVIST")
    assertEquals("Elder Chronicler", archivist.title)
    assertEquals(0.74f, archivist.pitch, 0.01f)

    val scholar = com.example.audio.NarratorPersona.fromId("SCHOLAR")
    assertEquals("Archipelago Scholar", scholar.title)

    // Save persona and custom voice preference
    prefs.narrationPersonaId = "SCHOLAR"
    prefs.narrationVoiceName = "en-gb-custom-voice"

    assertEquals("SCHOLAR", prefs.narrationPersonaId)
    assertEquals("en-gb-custom-voice", prefs.narrationVoiceName)
  }

  @Test
  fun `verify painterly styles and curated scene prompts`() {
    val styles = com.example.ai.PainterlyStyle.values()
    assertTrue(styles.size >= 4)

    val impasto = com.example.ai.PainterlyStyle.fromId("PAINTERLY_OIL")
    assertEquals("Painterly Impasto", impasto.displayName)
    assertTrue(impasto.promptModifier.contains("oil brushwork"))

    val watercolor = com.example.ai.PainterlyStyle.fromId("PARCHMENT_WATERCOLOR")
    assertEquals("Antiquarian Watercolor", watercolor.displayName)
    assertTrue(watercolor.promptModifier.contains("parchment"))

    // Test scene catalog
    val scenes = com.example.ai.ScenePromptsCatalog.curatedScenes
    assertTrue(scenes.isNotEmpty())
    val ch1Scenes = com.example.ai.ScenePromptsCatalog.getPromptsForChapter(0)
    assertTrue(ch1Scenes.isNotEmpty())
    assertEquals("The Skiff in Golden Cloudsea", ch1Scenes.first().title)
  }

  @Test
  fun `verify illustration preferences persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.example.data.CourierPreferences(context)

    org.junit.Assert.assertNull(prefs.getSavedIllustrationsJson())

    val sampleJson = """[{"id":"user_123","title":"Cloud Flight","prompt":"Wind-skiff over clouds","styleId":"GHIBLI_WIND","timestamp":1718000000000}]"""
    prefs.saveIllustrationsJson(sampleJson)

    assertEquals(sampleJson, prefs.getSavedIllustrationsJson())
  }

  @Test
  fun `verify character generator engine and stat balance`() {
    val procedural = com.example.character.CharacterGeneratorEngine.generateFullProceduralCharacter(
      archetype = com.example.character.SkyfarerArchetype.SALT_COURIER,
      origin = com.example.character.OriginIsle.OROS_CLIFFS,
      lantern = com.example.character.LanternAffinity.AMBER_HEARTH
    )

    assertNotNull(procedural)
    assertTrue(procedural.name.isNotBlank())
    assertTrue(procedural.title.isNotBlank())
    assertEquals(com.example.character.SkyfarerArchetype.SALT_COURIER, procedural.archetype)
    assertEquals(com.example.character.OriginIsle.OROS_CLIFFS, procedural.originIsle)
    assertEquals(com.example.character.LanternAffinity.AMBER_HEARTH, procedural.lanternAffinity)
    // Verify balanced stat distribution: 28 total points
    assertEquals(28, procedural.stats.totalPoints)
    assertTrue(procedural.stats.tillerAgility >= 8) // Primary stat for Salt Courier
    assertTrue(procedural.signatureRelic.isNotBlank())
    assertTrue(procedural.personalityQuirk.isNotBlank())
    assertTrue(procedural.motivation.isNotBlank())
    assertTrue(procedural.backstory.isNotBlank())
    assertTrue(procedural.customQuote.isNotBlank())
  }

  @Test
  fun `verify character json serialization and persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.example.data.CourierPreferences(context)

    val char1 = com.example.character.CharacterGeneratorEngine.generateFullProceduralCharacter(
      archetype = com.example.character.SkyfarerArchetype.MIST_NAVIGATOR
    ).copy(name = "Zephyr Gale", title = "The Cloud-Tide Eye")

    val char2 = com.example.character.CharacterGeneratorEngine.generateFullProceduralCharacter(
      archetype = com.example.character.SkyfarerArchetype.CHAIN_CLIMBER
    ).copy(name = "Brann Iron-Spur", title = "Iron-Grip of Thal")

    val roster = listOf(char1, char2)
    val json = com.example.character.SkyfarerCharacter.listToJson(roster)
    prefs.saveCharactersJson(json)
    prefs.activeCharacterId = char1.id

    val loadedJson = prefs.getSavedCharactersJson()
    assertNotNull(loadedJson)
    val parsedRoster = com.example.character.SkyfarerCharacter.listFromJson(loadedJson!!)
    assertEquals(2, parsedRoster.size)
    assertEquals("Zephyr Gale", parsedRoster[0].name)
    assertEquals("Brann Iron-Spur", parsedRoster[1].name)
    assertEquals(char1.id, prefs.activeCharacterId)
  }

  @Test
  fun `verify all skyfarer archetypes and affinities`() {
    val archetypes = com.example.character.SkyfarerArchetype.values()
    assertEquals(6, archetypes.size)

    val origins = com.example.character.OriginIsle.values()
    assertEquals(6, origins.size)

    val affinities = com.example.character.LanternAffinity.values()
    assertEquals(5, affinities.size)

    archetypes.forEach { arch ->
      val stats = com.example.character.CharacterGeneratorEngine.generateProceduralStats(arch)
      assertEquals(28, stats.totalPoints)
    }
  }
}
