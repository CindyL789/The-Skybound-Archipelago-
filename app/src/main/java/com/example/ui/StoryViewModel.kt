package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.CuratedScenePrompt
import com.example.ai.ImageGenerationResult
import com.example.ai.ImagenIllustrationService
import com.example.ai.PainterlyStyle
import com.example.ai.SceneIllustration
import com.example.ai.ScenePromptsCatalog
import com.example.audio.AtmosphericSoundPlayer
import com.example.audio.DeviceVoice
import com.example.audio.NarratorPersona
import com.example.audio.SoundscapePreset
import com.example.audio.StoryNarrator
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
  MAP("Archipelago"),
  ATELIER("Gallery"),
  SATCHEL("Satchel"),
  CODEX("Lore & Cast")
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
  val illustrationGallery: List<SceneIllustration> = emptyList()
)

class StoryViewModel(application: Application) : AndroidViewModel(application) {
  private val prefs = CourierPreferences(application)
  private val soundPlayer = AtmosphericSoundPlayer()
  private val imagenService = ImagenIllustrationService(application)

  private val initialPreset: SoundscapePreset = try {
    SoundscapePreset.valueOf(prefs.ambientPresetName)
  } catch (e: Exception) {
    SoundscapePreset.CLOUD_SEA
  }

  private val initialPersona = NarratorPersona.fromId(prefs.narrationPersonaId)

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
      illustrationGallery = loadIllustrations()
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

    if (prefs.ambientAudioEnabled) {
      toggleAudio()
    }
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

  override fun onCleared() {
    super.onCleared()
    soundPlayer.stop()
    narrator.cleanup()
  }
}
