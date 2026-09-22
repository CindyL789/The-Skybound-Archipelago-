package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.CuratedScenePrompt
import com.example.ai.GeneratedMusicTrack
import com.example.ai.ImageGenerationResult
import com.example.ai.ImagenIllustrationService
import com.example.ai.LyriaModel
import com.example.ai.LyriaMusicService
import com.example.ai.MusicGenerationResult
import com.example.ai.PainterlyStyle
import com.example.ai.SceneIllustration
import com.example.ai.ScenePromptsCatalog
import com.example.ai.VeoVideo
import com.example.ai.VeoVideoResult
import com.example.ai.VeoVideoService
import java.io.File
import com.example.audio.AtmosphericSoundPlayer
import com.example.audio.DeviceVoice
import com.example.audio.NarratorPersona
import com.example.audio.SoundscapePreset
import com.example.audio.StoryNarrator
import com.example.character.CharacterAiChronicler
import com.example.character.CharacterGeneratorEngine
import com.example.character.CharacterStats
import com.example.character.LanternAffinity
import com.example.character.OriginIsle
import com.example.character.SkyfarerArchetype
import com.example.character.SkyfarerCharacter
import com.example.data.AnnotatedTerm
import com.example.data.Chapter
import com.example.data.ChoiceOption
import com.example.data.CourierItem
import com.example.data.CourierPreferences
import com.example.data.CourierRig
import com.example.data.LanternMode
import com.example.data.StoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

enum class NavigationTab(val label: String) {
  READER("Story"),
  CHARACTER("Creator"),
  MAP("Archipelago"),
  ATELIER("Gallery"),
  SATCHEL("Satchel"),
  CODEX("Lore & Cast")
}

enum class CharacterGeneratorTab(val label: String) {
  FORGE("Forge & Conjure"),
  ROSTER("Courier Roster")
}

data class StoryUiState(
  val currentChapterId: Int = 0,
  val activeTab: NavigationTab = NavigationTab.READER,
  val lanternMode: LanternMode = LanternMode.STANDARD,
  val activeRig: CourierRig = CourierRig.STANDARD_COURIER,
  val isParchmentMode: Boolean = false,
  val fontScale: Float = 1.0f,
  val isAudioPlaying: Boolean = false,
  val ambientVolume: Float = 0.7f,
  val ambientPreset: SoundscapePreset = SoundscapePreset.CLOUD_SEA,
  val ambientDucking: Boolean = true,
  val isNarrating: Boolean = false,
  val isNarrationPaused: Boolean = false,
  val narratingParagraphIndex: Int = 0,
  val narrationSpeed: Float = 1.0f,
  val narrationPitch: Float = 1.0f,
  val selectedPersona: NarratorPersona = NarratorPersona.COURIER,
  val selectedVoiceName: String? = null,
  val availableVoices: List<DeviceVoice> = emptyList(),
  val isPreviewingVoice: Boolean = false,
  val showAudioOptions: Boolean = false,
  val bookmarks: Set<Int> = emptySet(),
  val collectedItemIds: Set<String> = emptySet(),
  val madeChoices: Map<String, String> = emptyMap(),
  val activeFootnote: AnnotatedTerm? = null,
  val showChapterDirectory: Boolean = false,
  val showCompassModal: Boolean = false,
  // --- Imagen Illustration State ---
  val showIllustrationStudio: Boolean = false,
  val studioSceneTitle: String = "",
  val studioPromptText: String = "",
  val studioSelectedStyle: PainterlyStyle = PainterlyStyle.PAINTERLY_OIL,
  val studioAspectRatio: String = "16:9",
  val isGeneratingIllustration: Boolean = false,
  val generationStatusMessage: String? = null,
  val activeIllustrationDetail: SceneIllustration? = null,
  val illustrationGallery: List<SceneIllustration> = emptyList(),
  // --- Character Generator State ---
  val generatedCharacter: SkyfarerCharacter? = null,
  val activeCharacter: SkyfarerCharacter? = null,
  val savedRoster: List<SkyfarerCharacter> = emptyList(),
  val isGeneratingCharacter: Boolean = false,
  val isGeneratingPortrait: Boolean = false,
  val characterStatusMessage: String? = null,
  val characterGeneratorTab: CharacterGeneratorTab = CharacterGeneratorTab.FORGE,
  // --- Lyria Music State ---
  val generatedMusicTracks: List<GeneratedMusicTrack> = emptyList(),
  val isGeneratingMusic: Boolean = false,
  val musicGenerationError: String? = null,
  val playingMusicTrackId: String? = null,
  val isMusicPlaying: Boolean = false,
  val selectedMusicModel: LyriaModel = LyriaModel.CLIP_PREVIEW,
  val isMusicLooping: Boolean = true,
  // --- Veo Video State ---
  val veoVideos: List<VeoVideo> = emptyList(),
  val isGeneratingVeoVideo: Boolean = false,
  val veoVideoGenerationError: String? = null,
  val showVeoStudioDialog: Boolean = false,
  val selectedVeoAspectRatio: String = "16:9",
  val selectedVeoSourceBitmap: Bitmap? = null,
  val selectedVeoSourceTitle: String = "",
  val activeViewingVideo: VeoVideo? = null
)

class StoryViewModel(application: Application) : AndroidViewModel(application) {
  private val prefs = CourierPreferences(application)
  private val soundPlayer = AtmosphericSoundPlayer()
  private val imagenService = ImagenIllustrationService(application)
  private val characterChronicler = CharacterAiChronicler(application)
  private val lyriaService = LyriaMusicService(application)
  private val veoService = VeoVideoService(application)
  private var musicMediaPlayer: MediaPlayer? = null

  private val initialPreset: SoundscapePreset = try {
    SoundscapePreset.valueOf(prefs.ambientPresetName)
  } catch (e: Exception) {
    SoundscapePreset.CLOUD_SEA
  }

  private val initialPersona = NarratorPersona.fromId(prefs.narrationPersonaId)
  private val initialSavedRoster = loadSavedCharacters()
  private val initialActiveChar = loadActiveCharacter()

  private val _uiState = MutableStateFlow(
    StoryUiState(
      currentChapterId = prefs.currentChapterId,
      lanternMode = prefs.lanternMode,
      activeRig = prefs.activeRig,
      isParchmentMode = prefs.isParchmentMode,
      fontScale = prefs.fontScale,
      isAudioPlaying = false,
      ambientVolume = prefs.ambientVolume,
      ambientPreset = initialPreset,
      ambientDucking = prefs.ambientDucking,
      narrationSpeed = prefs.narrationSpeed,
      narrationPitch = prefs.narrationPitch,
      selectedPersona = initialPersona,
      selectedVoiceName = prefs.narrationVoiceName,
      bookmarks = prefs.getBookmarks(),
      collectedItemIds = prefs.getCollectedItemIds(),
      illustrationGallery = loadIllustrations(),
      savedRoster = initialSavedRoster,
      activeCharacter = initialActiveChar,
      generatedCharacter = initialActiveChar ?: CharacterGeneratorEngine.generateFullProceduralCharacter(),
      generatedMusicTracks = loadSavedMusic(),
      veoVideos = loadSavedVideos()
    )
  )
  val uiState: StateFlow<StoryUiState> = _uiState.asStateFlow()

  val chapters: List<Chapter> = StoryRepository.getChapters()

  private val narrator: StoryNarrator = StoryNarrator(
    context = application.applicationContext,
    onStateChanged = { isNarrating, isPaused, paragraphIndex ->
      _uiState.update {
        it.copy(
          isNarrating = isNarrating,
          isNarrationPaused = isPaused,
          narratingParagraphIndex = paragraphIndex
        )
      }
      if (_uiState.value.ambientDucking) {
        soundPlayer.setDucking(isNarrating && !isPaused)
      }
    },
    onChapterCompleted = {
      _uiState.update {
        it.copy(
          isNarrating = false,
          isNarrationPaused = false
        )
      }
      soundPlayer.setDucking(false)
    },
    onVoicesDiscovered = { voices ->
      _uiState.update { it.copy(availableVoices = voices) }
    },
    onPreviewStateChanged = { isPreviewing ->
      _uiState.update { it.copy(isPreviewingVoice = isPreviewing) }
    }
  )

  init {
    soundPlayer.setVolume(prefs.ambientVolume)
    soundPlayer.setPreset(initialPreset)

    if (prefs.narrationVoiceName != null) {
      narrator.selectDeviceVoice(prefs.narrationVoiceName)
    } else {
      narrator.applyPersona(initialPersona)
    }
    narrator.setSpeechRate(prefs.narrationSpeed)
    narrator.setPitch(prefs.narrationPitch)
  }

  fun setTab(tab: NavigationTab) {
    _uiState.update { it.copy(activeTab = tab) }
  }

  fun selectChapter(chapterId: Int) {
    val clamped = chapterId.coerceIn(0, chapters.size - 1)
    if (clamped != _uiState.value.currentChapterId && _uiState.value.isNarrating) {
      stopNarration()
    }
    prefs.currentChapterId = clamped
    _uiState.update {
      it.copy(
        currentChapterId = clamped,
        showChapterDirectory = false,
        activeTab = NavigationTab.READER
      )
    }
  }

  fun nextChapter() {
    val next = (_uiState.value.currentChapterId + 1).coerceAtMost(chapters.size - 1)
    selectChapter(next)
  }

  fun prevChapter() {
    val prev = (_uiState.value.currentChapterId - 1).coerceAtLeast(0)
    selectChapter(prev)
  }

  fun setLanternMode(mode: LanternMode) {
    prefs.lanternMode = mode
    _uiState.update { it.copy(lanternMode = mode) }
  }

  fun setCourierRig(rig: CourierRig) {
    prefs.activeRig = rig
    _uiState.update { it.copy(activeRig = rig) }
  }

  fun toggleParchmentMode() {
    val newMode = !_uiState.value.isParchmentMode
    prefs.isParchmentMode = newMode
    _uiState.update { it.copy(isParchmentMode = newMode) }
  }

  fun adjustFontSize(delta: Float) {
    val newScale = (_uiState.value.fontScale + delta).coerceIn(0.85f, 1.35f)
    prefs.fontScale = newScale
    _uiState.update { it.copy(fontScale = newScale) }
  }

  fun toggleBookmark(chapterId: Int) {
    prefs.toggleBookmark(chapterId)
    _uiState.update { it.copy(bookmarks = prefs.getBookmarks()) }
  }

  // --- Ambient Audio Controls ---

  fun toggleAudio() {
    val willPlay = !soundPlayer.isPlaying()
    if (willPlay) {
      soundPlayer.start(_uiState.value.ambientPreset)
    } else {
      soundPlayer.stop()
    }
    prefs.ambientAudioEnabled = willPlay
    _uiState.update { it.copy(isAudioPlaying = willPlay) }
  }

  fun setAmbientVolume(volume: Float) {
    val clamped = volume.coerceIn(0.0f, 1.0f)
    prefs.ambientVolume = clamped
    soundPlayer.setVolume(clamped)
    _uiState.update { it.copy(ambientVolume = clamped) }
  }

  fun setAmbientPreset(preset: SoundscapePreset) {
    prefs.ambientPresetName = preset.name
    soundPlayer.setPreset(preset)
    _uiState.update { it.copy(ambientPreset = preset) }
  }

  fun toggleAmbientDucking() {
    val newDucking = !_uiState.value.ambientDucking
    prefs.ambientDucking = newDucking
    _uiState.update { it.copy(ambientDucking = newDucking) }
    soundPlayer.setDucking(newDucking && _uiState.value.isNarrating && !_uiState.value.isNarrationPaused)
  }

  // --- Narration (Text-to-Speech) Controls ---

  fun startNarration(chapter: Chapter, startParagraph: Int = 0) {
    narrator.startChapter(chapter, startParagraph)
    _uiState.update {
      it.copy(
        isNarrating = true,
        isNarrationPaused = false,
        narratingParagraphIndex = startParagraph
      )
    }
  }

  fun pauseNarration() {
    narrator.pause()
    _uiState.update { it.copy(isNarrationPaused = true) }
    soundPlayer.setDucking(false)
  }

  fun resumeNarration() {
    narrator.resume()
    _uiState.update { it.copy(isNarrationPaused = false) }
    if (_uiState.value.ambientDucking) {
      soundPlayer.setDucking(true)
    }
  }

  fun stopNarration() {
    narrator.stop()
    _uiState.update {
      it.copy(
        isNarrating = false,
        isNarrationPaused = false
      )
    }
    soundPlayer.setDucking(false)
  }

  fun nextNarratorParagraph() {
    narrator.nextParagraph()
  }

  fun previousNarratorParagraph() {
    narrator.previousParagraph()
  }

  fun setNarrationSpeed(speed: Float) {
    prefs.narrationSpeed = speed
    narrator.setSpeechRate(speed)
    _uiState.update { it.copy(narrationSpeed = speed) }
  }

  fun setNarrationPitch(pitch: Float) {
    prefs.narrationPitch = pitch
    narrator.setPitch(pitch)
    _uiState.update { it.copy(narrationPitch = pitch) }
  }

  fun selectPersona(persona: NarratorPersona) {
    prefs.narrationPersonaId = persona.id
    prefs.narrationVoiceName = null
    prefs.narrationSpeed = persona.speechRate
    prefs.narrationPitch = persona.pitch
    narrator.applyPersona(persona)
    _uiState.update {
      it.copy(
        selectedPersona = persona,
        selectedVoiceName = null,
        narrationSpeed = persona.speechRate,
        narrationPitch = persona.pitch
      )
    }
  }

  fun selectDeviceVoice(voice: DeviceVoice) {
    prefs.narrationVoiceName = voice.name
    narrator.selectDeviceVoice(voice.name)
    _uiState.update {
      it.copy(
        selectedVoiceName = voice.name
      )
    }
  }

  fun clearDeviceVoiceToPersona() {
    prefs.narrationVoiceName = null
    narrator.applyPersona(_uiState.value.selectedPersona)
    _uiState.update {
      it.copy(selectedVoiceName = null)
    }
  }

  fun previewVoiceSample(sampleText: String? = null) {
    val sample = sampleText ?: "The wind over the Cloud Sea carries memories of fallen towers and skybound voyages."
    narrator.previewVoice(sample)
  }

  fun stopVoicePreview() {
    narrator.stopPreview()
  }

  fun resetVoiceDefaults() {
    selectPersona(NarratorPersona.COURIER)
  }

  fun toggleAudioOptions(show: Boolean) {
    _uiState.update { it.copy(showAudioOptions = show) }
  }

  // --- Story Decisions & Satchel ---

  fun selectOption(choiceId: String, option: ChoiceOption) {
    prefs.saveChoice(choiceId, option.id)
    if (option.rewardItem != null) {
      prefs.unlockItem(option.rewardItem.id)
    }
    val updatedChoices = _uiState.value.madeChoices.toMutableMap()
    updatedChoices[choiceId] = option.id

    _uiState.update {
      it.copy(
        madeChoices = updatedChoices,
        collectedItemIds = prefs.getCollectedItemIds()
      )
    }
  }

  fun getSelectedOptionId(choiceId: String): String? {
    return _uiState.value.madeChoices[choiceId] ?: prefs.getChoiceFor(choiceId)
  }

  fun showFootnote(term: AnnotatedTerm?) {
    _uiState.update { it.copy(activeFootnote = term) }
  }

  fun toggleChapterDirectory(show: Boolean) {
    _uiState.update { it.copy(showChapterDirectory = show) }
  }

  fun toggleCompassModal(show: Boolean) {
    _uiState.update { it.copy(showCompassModal = show) }
  }

  // --- Imagen Image Generation & Scene Illustrations ---

  private fun loadIllustrations(): List<SceneIllustration> {
    val curated = ScenePromptsCatalog.curatedScenes.map { prompt ->
      SceneIllustration(
        id = prompt.id,
        title = prompt.title,
        prompt = prompt.prompt,
        style = prompt.defaultStyle,
        timestamp = 1718000000000L + (prompt.chapterId * 3600000L),
        drawableResId = prompt.defaultDrawableRes,
        chapterId = prompt.chapterId,
        isUserGenerated = false
      )
    }
    val savedUserItems = deserializeIllustrations(prefs.getSavedIllustrationsJson())
    return savedUserItems + curated
  }

  fun openIllustrationStudio(
    sceneTitle: String? = null,
    prompt: String? = null,
    style: PainterlyStyle? = null
  ) {
    val currentChap = chapters.getOrNull(_uiState.value.currentChapterId)
    val defaultTitle = sceneTitle ?: "Scene: ${currentChap?.title ?: "The Skybound Archipelago"}"
    val defaultPrompt = prompt ?: ScenePromptsCatalog.getPromptsForChapter(_uiState.value.currentChapterId).firstOrNull()?.prompt
      ?: "The wind-skiff skimming over endless golden cloudbanks beneath twilight skies, floating island silhouetted"

    _uiState.update {
      it.copy(
        showIllustrationStudio = true,
        studioSceneTitle = defaultTitle,
        studioPromptText = defaultPrompt,
        studioSelectedStyle = style ?: it.studioSelectedStyle,
        generationStatusMessage = null
      )
    }
  }

  fun closeIllustrationStudio() {
    _uiState.update {
      it.copy(
        showIllustrationStudio = false,
        isGeneratingIllustration = false,
        generationStatusMessage = null
      )
    }
  }

  fun setStudioPromptText(text: String) {
    _uiState.update { it.copy(studioPromptText = text) }
  }

  fun setStudioSceneTitle(title: String) {
    _uiState.update { it.copy(studioSceneTitle = title) }
  }

  fun setStudioStyle(style: PainterlyStyle) {
    _uiState.update { it.copy(studioSelectedStyle = style) }
  }

  fun setStudioAspectRatio(aspectRatio: String) {
    _uiState.update { it.copy(studioAspectRatio = aspectRatio) }
  }

  fun generateSceneIllustration(
    title: String? = null,
    promptText: String? = null,
    style: PainterlyStyle? = null
  ) {
    val currentTitle = title ?: _uiState.value.studioSceneTitle
    val currentPrompt = promptText ?: _uiState.value.studioPromptText
    val currentStyle = style ?: _uiState.value.studioSelectedStyle
    val currentRatio = _uiState.value.studioAspectRatio
    val currentChapterId = _uiState.value.currentChapterId

    _uiState.update {
      it.copy(
        isGeneratingIllustration = true,
        generationStatusMessage = "Conjuring painterly scene with Imagen 3..."
      )
    }

    viewModelScope.launch {
      val result = imagenService.generateSceneIllustration(
        sceneTitle = currentTitle,
        userPrompt = currentPrompt,
        style = currentStyle,
        aspectRatio = currentRatio,
        chapterId = currentChapterId
      )

      when (result) {
        is ImageGenerationResult.Success -> {
          val newIllustration = SceneIllustration(
            id = "user_gen_${System.currentTimeMillis()}",
            title = currentTitle,
            prompt = currentPrompt,
            style = currentStyle,
            timestamp = System.currentTimeMillis(),
            localFilePath = result.savedFilePath,
            chapterId = currentChapterId,
            isUserGenerated = true
          )

          val updatedList = listOf(newIllustration) + _uiState.value.illustrationGallery
          persistIllustrations(updatedList)

          _uiState.update {
            it.copy(
              isGeneratingIllustration = false,
              generationStatusMessage = result.note ?: "Illustration conjured successfully!",
              illustrationGallery = updatedList,
              activeIllustrationDetail = newIllustration,
              showIllustrationStudio = false
            )
          }
        }
        is ImageGenerationResult.Error -> {
          _uiState.update {
            it.copy(
              isGeneratingIllustration = false,
              generationStatusMessage = "Note: ${result.message}"
            )
          }
        }
      }
    }
  }

  fun viewIllustrationDetail(illustration: SceneIllustration) {
    _uiState.update { it.copy(activeIllustrationDetail = illustration) }
  }

  fun closeIllustrationDetail() {
    _uiState.update { it.copy(activeIllustrationDetail = null) }
  }

  fun deleteUserIllustration(id: String) {
    val updatedList = _uiState.value.illustrationGallery.filterNot { it.id == id && it.isUserGenerated }
    persistIllustrations(updatedList)
    _uiState.update {
      it.copy(
        illustrationGallery = updatedList,
        activeIllustrationDetail = if (it.activeIllustrationDetail?.id == id) null else it.activeIllustrationDetail
      )
    }
  }

  private fun persistIllustrations(list: List<SceneIllustration>) {
    val json = serializeIllustrations(list)
    prefs.saveIllustrationsJson(json)
  }

  private fun serializeIllustrations(list: List<SceneIllustration>): String {
    val array = JSONArray()
    list.filter { it.isUserGenerated }.forEach { item ->
      val obj = JSONObject().apply {
        put("id", item.id)
        put("title", item.title)
        put("prompt", item.prompt)
        put("styleId", item.style.id)
        put("timestamp", item.timestamp)
        put("localFilePath", item.localFilePath ?: "")
        put("chapterId", item.chapterId)
      }
      array.put(obj)
    }
    return array.toString()
  }

  private fun deserializeIllustrations(json: String?): List<SceneIllustration> {
    if (json.isNullOrEmpty()) return emptyList()
    return try {
      val array = JSONArray(json)
      val list = mutableListOf<SceneIllustration>()
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        list.add(
          SceneIllustration(
            id = obj.getString("id"),
            title = obj.getString("title"),
            prompt = obj.getString("prompt"),
            style = PainterlyStyle.fromId(obj.optString("styleId")),
            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
            localFilePath = obj.optString("localFilePath").takeIf { it.isNotEmpty() },
            chapterId = obj.optInt("chapterId", 0),
            isUserGenerated = true
          )
        )
      }
      list
    } catch (e: Exception) {
      emptyList()
    }
  }

  // ==========================================
  // Character Generator Methods
  // ==========================================

  fun setCharacterGeneratorTab(tab: CharacterGeneratorTab) {
    _uiState.update { it.copy(characterGeneratorTab = tab) }
  }

  fun updateDraftCharacter(character: SkyfarerCharacter) {
    _uiState.update { it.copy(generatedCharacter = character) }
  }

  fun generateProceduralCharacter(
    archetype: SkyfarerArchetype? = null,
    origin: OriginIsle? = null,
    lantern: LanternAffinity? = null
  ) {
    val char = CharacterGeneratorEngine.generateFullProceduralCharacter(archetype, origin, lantern)
    _uiState.update {
      it.copy(
        generatedCharacter = char,
        characterStatusMessage = "Forged ${char.name} (${char.archetype.title})"
      )
    }
  }

  fun generateAiCharacter(
    archetype: SkyfarerArchetype? = null,
    origin: OriginIsle? = null,
    lantern: LanternAffinity? = null,
    preferredName: String? = null
  ) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isGeneratingCharacter = true,
          characterStatusMessage = "Consulting the Elder Chronicler..."
        )
      }
      try {
        val char = characterChronicler.chronicleCharacterWithAi(archetype, origin, lantern, preferredName)
        _uiState.update {
          it.copy(
            generatedCharacter = char,
            isGeneratingCharacter = false,
            characterStatusMessage = if (char.isAiGenerated) "Gemini Chronicler wove ${char.name}'s bio!" else "Procedurally forged ${char.name}"
          )
        }
      } catch (e: Exception) {
        val fallback = CharacterGeneratorEngine.generateFullProceduralCharacter(archetype, origin, lantern)
        _uiState.update {
          it.copy(
            generatedCharacter = fallback,
            isGeneratingCharacter = false,
            characterStatusMessage = "Procedurally forged ${fallback.name}"
          )
        }
      }
    }
  }

  fun conjurePortraitForCurrentCharacter() {
    val current = _uiState.value.generatedCharacter ?: return
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isGeneratingPortrait = true,
          characterStatusMessage = "Painting portrait with Imagen 3..."
        )
      }
      val path = characterChronicler.conjureCharacterPortrait(current)
      if (path != null) {
        val updated = current.copy(customPortraitPath = path)
        _uiState.update {
          it.copy(
            generatedCharacter = updated,
            isGeneratingPortrait = false,
            characterStatusMessage = "Painted portrait for ${current.name}!"
          )
        }
      } else {
        _uiState.update {
          it.copy(
            isGeneratingPortrait = false,
            characterStatusMessage = "Portrait complete (using archetype plate)."
          )
        }
      }
    }
  }

  fun saveCurrentCharacterToRoster() {
    val current = _uiState.value.generatedCharacter ?: return
    val existing = _uiState.value.savedRoster
    val updated = if (existing.any { it.id == current.id }) {
      existing.map { if (it.id == current.id) current else it }
    } else {
      listOf(current) + existing
    }
    persistCharacters(updated)
    _uiState.update {
      it.copy(
        savedRoster = updated,
        characterStatusMessage = "${current.name} saved to Courier Roster!"
      )
    }
  }

  fun deleteCharacterFromRoster(id: String) {
    val updated = _uiState.value.savedRoster.filterNot { it.id == id }
    persistCharacters(updated)
    val newActive = if (_uiState.value.activeCharacter?.id == id) updated.firstOrNull() else _uiState.value.activeCharacter
    if (newActive != null) {
      prefs.activeCharacterId = newActive.id
    } else {
      prefs.activeCharacterId = null
    }
    _uiState.update {
      it.copy(
        savedRoster = updated,
        activeCharacter = newActive,
        characterStatusMessage = "Removed from roster."
      )
    }
  }

  fun setActiveCharacter(character: SkyfarerCharacter) {
    prefs.activeCharacterId = character.id
    prefs.activeRig = character.gliderRig
    prefs.lanternMode = character.lanternAffinity.matchingMode
    _uiState.update {
      it.copy(
        activeCharacter = character,
        activeRig = character.gliderRig,
        lanternMode = character.lanternAffinity.matchingMode,
        characterStatusMessage = "${character.name} is now your active Courier!"
      )
    }
  }

  fun clearCharacterStatusMessage() {
    _uiState.update { it.copy(characterStatusMessage = null) }
  }

  private fun loadSavedCharacters(): List<SkyfarerCharacter> {
    val json = prefs.getSavedCharactersJson()
    val list = if (!json.isNullOrBlank()) SkyfarerCharacter.listFromJson(json) else emptyList()
    return if (list.isNotEmpty()) list else {
      val defaultChar = CharacterGeneratorEngine.generateFullProceduralCharacter(
        archetype = SkyfarerArchetype.SALT_COURIER,
        origin = OriginIsle.OROS_CLIFFS,
        lantern = LanternAffinity.AMBER_HEARTH
      ).copy(
        name = "Sera Venn",
        title = "The Moon-Koi Courier",
        customQuote = "Every route is temporary. Every delivery is a promise made against the weather."
      )
      listOf(defaultChar)
    }
  }

  private fun loadActiveCharacter(): SkyfarerCharacter? {
    val roster = loadSavedCharacters()
    val activeId = prefs.activeCharacterId
    return roster.firstOrNull { it.id == activeId } ?: roster.firstOrNull()
  }

  private fun persistCharacters(list: List<SkyfarerCharacter>) {
    val json = SkyfarerCharacter.listToJson(list)
    prefs.saveCharactersJson(json)
  }

  // --- Lyria Music Generation & Playback ---

  private fun loadSavedMusic(): List<GeneratedMusicTrack> {
    val json = prefs.getSavedMusicJson()
    val list = if (!json.isNullOrBlank()) GeneratedMusicTrack.listFromJson(json) else emptyList()
    return list
  }

  fun selectMusicModel(model: LyriaModel) {
    _uiState.update { it.copy(selectedMusicModel = model) }
  }

  fun generateMusic(
    title: String,
    prompt: String,
    model: LyriaModel = _uiState.value.selectedMusicModel
  ) {
    if (_uiState.value.isGeneratingMusic) return
    _uiState.update { it.copy(isGeneratingMusic = true, musicGenerationError = null) }

    viewModelScope.launch {
      when (val result = lyriaService.generateMusic(title, prompt, model)) {
        is MusicGenerationResult.Success -> {
          val updated = listOf(result.track) + _uiState.value.generatedMusicTracks.filterNot { it.id == result.track.id }
          _uiState.update {
            it.copy(
              isGeneratingMusic = false,
              generatedMusicTracks = updated
            )
          }
          prefs.saveMusicJson(GeneratedMusicTrack.listToJson(updated))
          playMusicTrack(result.track)
        }
        is MusicGenerationResult.Error -> {
          _uiState.update {
            it.copy(
              isGeneratingMusic = false,
              musicGenerationError = result.message
            )
          }
        }
      }
    }
  }

  fun playMusicTrack(track: GeneratedMusicTrack) {
    try {
      musicMediaPlayer?.stop()
      musicMediaPlayer?.release()
      musicMediaPlayer = null

      val file = File(track.audioFilePath)
      if (file.exists() && file.length() > 0) {
        musicMediaPlayer = MediaPlayer().apply {
          setDataSource(file.absolutePath)
          isLooping = _uiState.value.isMusicLooping
          setOnCompletionListener {
            if (!_uiState.value.isMusicLooping) {
              _uiState.update { it.copy(isMusicPlaying = false) }
            }
          }
          prepare()
          start()
        }
        _uiState.update {
          it.copy(
            playingMusicTrackId = track.id,
            isMusicPlaying = true
          )
        }
      } else {
        // Generate procedural track if file not found
        generateMusic(track.title, track.prompt, LyriaModel.CLIP_PREVIEW)
      }
    } catch (e: Exception) {
      Log.e("StoryViewModel", "Failed to play music track", e)
      _uiState.update { it.copy(isMusicPlaying = false) }
    }
  }

  fun pauseMusicTrack() {
    try {
      musicMediaPlayer?.pause()
      _uiState.update { it.copy(isMusicPlaying = false) }
    } catch (e: Exception) {
      Log.w("StoryViewModel", "Failed to pause music track", e)
    }
  }

  fun resumeMusicTrack() {
    try {
      musicMediaPlayer?.start()
      _uiState.update { it.copy(isMusicPlaying = true) }
    } catch (e: Exception) {
      Log.w("StoryViewModel", "Failed to resume music track", e)
    }
  }

  fun stopMusicTrack() {
    try {
      musicMediaPlayer?.stop()
      musicMediaPlayer?.release()
      musicMediaPlayer = null
      _uiState.update { it.copy(isMusicPlaying = false, playingMusicTrackId = null) }
    } catch (e: Exception) {
      Log.w("StoryViewModel", "Failed to stop music track", e)
    }
  }

  fun toggleMusicLoop() {
    val newLoop = !_uiState.value.isMusicLooping
    musicMediaPlayer?.isLooping = newLoop
    _uiState.update { it.copy(isMusicLooping = newLoop) }
  }

  fun deleteMusicTrack(trackId: String) {
    if (_uiState.value.playingMusicTrackId == trackId) {
      stopMusicTrack()
    }
    val updated = _uiState.value.generatedMusicTracks.filterNot { it.id == trackId }
    _uiState.update { it.copy(generatedMusicTracks = updated) }
    prefs.saveMusicJson(GeneratedMusicTrack.listToJson(updated))
  }

  // --- Veo Video Generation & Management ---

  private fun loadSavedVideos(): List<VeoVideo> {
    val json = prefs.getSavedVideosJson()
    val list = if (!json.isNullOrBlank()) VeoVideo.listFromJson(json) else emptyList()
    return list
  }

  fun openVeoStudio(
    sourceBitmap: Bitmap? = null,
    title: String = "Archipelago Motion",
    initialPrompt: String = "",
    aspectRatio: String = "16:9"
  ) {
    _uiState.update {
      it.copy(
        showVeoStudioDialog = true,
        selectedVeoSourceBitmap = sourceBitmap,
        selectedVeoSourceTitle = title,
        selectedVeoAspectRatio = if (aspectRatio == "9:16") "9:16" else "16:9",
        veoVideoGenerationError = null
      )
    }
  }

  fun closeVeoStudio() {
    _uiState.update { it.copy(showVeoStudioDialog = false) }
  }

  fun setVeoAspectRatio(aspectRatio: String) {
    _uiState.update { it.copy(selectedVeoAspectRatio = if (aspectRatio == "9:16") "9:16" else "16:9") }
  }

  fun setVeoSourceBitmap(bitmap: Bitmap, title: String = "Uploaded Photo") {
    _uiState.update { it.copy(selectedVeoSourceBitmap = bitmap, selectedVeoSourceTitle = title) }
  }

  fun setActiveViewingVideo(video: VeoVideo?) {
    _uiState.update { it.copy(activeViewingVideo = video) }
  }

  fun deleteVeoVideo(videoId: String) {
    if (_uiState.value.activeViewingVideo?.id == videoId) {
      _uiState.update { it.copy(activeViewingVideo = null) }
    }
    val updated = _uiState.value.veoVideos.filterNot { it.id == videoId }
    _uiState.update { it.copy(veoVideos = updated) }
    prefs.saveVideosJson(VeoVideo.listToJson(updated))
  }

  fun generateVeoVideo(
    prompt: String,
    aspectRatio: String = _uiState.value.selectedVeoAspectRatio,
    title: String = _uiState.value.selectedVeoSourceTitle.ifBlank { "Isle Motion" }
  ) {
    val bitmap = _uiState.value.selectedVeoSourceBitmap
    if (bitmap == null) {
      _uiState.update { it.copy(veoVideoGenerationError = "Please select or upload a photo first.") }
      return
    }

    if (_uiState.value.isGeneratingVeoVideo) return
    _uiState.update { it.copy(isGeneratingVeoVideo = true, veoVideoGenerationError = null) }

    viewModelScope.launch {
      when (val result = veoService.generateVideoFromImage(bitmap, prompt, aspectRatio, title)) {
        is VeoVideoResult.Success -> {
          val updated = listOf(result.video) + _uiState.value.veoVideos.filterNot { it.id == result.video.id }
          _uiState.update {
            it.copy(
              isGeneratingVeoVideo = false,
              veoVideos = updated,
              activeViewingVideo = result.video,
              showVeoStudioDialog = false
            )
          }
          prefs.saveVideosJson(VeoVideo.listToJson(updated))
        }
        is VeoVideoResult.Error -> {
          _uiState.update {
            it.copy(
              isGeneratingVeoVideo = false,
              veoVideoGenerationError = result.message
            )
          }
        }
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    try {
      musicMediaPlayer?.stop()
      musicMediaPlayer?.release()
      musicMediaPlayer = null
    } catch (e: Exception) {}
    soundPlayer.stop()
    narrator.cleanup()
  }
}
