package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import com.example.audio.DeviceVoice
import com.example.audio.NarratorPersona
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundscapePreset
import com.example.data.Chapter
import com.example.ui.StoryUiState
import com.example.ui.StoryViewModel
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.AmberLampSoft
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurface
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.TextMutedNight
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VermilionCourier
import com.example.ui.theme.VoidDark

enum class AudioTab(val label: String) {
  NARRATION("Read Aloud"),
  AMBIENT("Ambient Sound")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioOptionsSheet(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  currentChapter: Chapter,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(AudioTab.NARRATION) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = DeepNight,
    modifier = modifier.testTag("audio_options_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
        .verticalScroll(rememberScrollState())
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(BlueGlass.copy(alpha = 0.2f))
              .border(1.dp, BlueGlass, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = BlueGlass,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = "AUDIO & SOUNDSCAPE",
              color = VermilionCourier,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.5.sp
            )
            Text(
              text = "Aural Settings",
              color = TextPrimaryNight,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("close_audio_sheet_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close audio settings",
            tint = TextSecondaryNight
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tab selector: Narration vs Ambient
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(NightSurface)
          .border(1.dp, NightCardBorder, RoundedCornerShape(12.dp))
          .padding(4.dp)
      ) {
        AudioTab.values().forEach { tab ->
          val isSelected = selectedTab == tab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) BlueGlass else Color.Transparent)
              .clickable { selectedTab = tab }
              .padding(vertical = 8.dp)
              .testTag("audio_tab_${tab.name.lowercase()}"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = tab.label,
              color = if (isSelected) VoidDark else TextSecondaryNight,
              fontSize = 13.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      when (selectedTab) {
        AudioTab.NARRATION -> {
          NarrationSection(
            viewModel = viewModel,
            uiState = uiState,
            currentChapter = currentChapter
          )
        }
        AudioTab.AMBIENT -> {
          AmbientSection(
            viewModel = viewModel,
            uiState = uiState
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}

@Composable
private fun NarrationSection(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  currentChapter: Chapter
) {
  val isNarrating = uiState.isNarrating
  val isPaused = uiState.isNarrationPaused
  val currentPara = uiState.narratingParagraphIndex
  val totalParas = currentChapter.paragraphs.size

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = 1.dp,
        brush = Brush.horizontalGradient(listOf(BlueGlass, AmberLamp)),
        shape = RoundedCornerShape(16.dp)
      )
      .testTag("narration_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NightSurface)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "READING ALOUD",
            color = VermilionCourier,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
          )
          Text(
            text = currentChapter.title,
            color = TextPrimaryNight,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isNarrating && !isPaused) BlueGlass.copy(alpha = 0.2f) else NightSurfaceVariant)
            .border(1.dp, if (isNarrating && !isPaused) BlueGlass else NightCardBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = when {
              isNarrating && !isPaused -> "NARRATING"
              isNarrating && isPaused -> "PAUSED"
              else -> "STANDBY"
            },
            color = if (isNarrating && !isPaused) BlueGlass else TextMutedNight,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Progress text
      Text(
        text = "Paragraph ${currentPara + 1} of $totalParas",
        color = AmberLamp,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )

      if (currentPara in currentChapter.paragraphs.indices) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "\"${currentChapter.paragraphs[currentPara].text.take(80)}...\"",
          color = TextSecondaryNight,
          fontSize = 12.sp,
          fontStyle = FontStyle.Italic,
          fontFamily = FontFamily.Serif,
          maxLines = 2
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Main Playback Controls
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Prev Paragraph
        IconButton(
          onClick = { viewModel.previousNarratorParagraph() },
          enabled = currentPara > 0,
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(NightSurfaceVariant)
            .testTag("narrator_prev_button")
        ) {
          Icon(
            imageVector = Icons.Default.FastRewind,
            contentDescription = "Previous Paragraph",
            tint = if (currentPara > 0) TextPrimaryNight else TextMutedNight
          )
        }

        // Play / Pause / Resume
        IconButton(
          onClick = {
            if (isNarrating && !isPaused) {
              viewModel.pauseNarration()
            } else if (isNarrating && isPaused) {
              viewModel.resumeNarration()
            } else {
              viewModel.startNarration(currentChapter)
            }
          },
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(BlueGlass)
            .testTag("narrator_play_pause_button")
        ) {
          Icon(
            imageVector = if (isNarrating && !isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isNarrating && !isPaused) "Pause Narration" else "Play Narration",
            tint = VoidDark,
            modifier = Modifier.size(28.dp)
          )
        }

        // Next Paragraph
        IconButton(
          onClick = { viewModel.nextNarratorParagraph() },
          enabled = currentPara < totalParas - 1,
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(NightSurfaceVariant)
            .testTag("narrator_next_button")
        ) {
          Icon(
            imageVector = Icons.Default.FastForward,
            contentDescription = "Next Paragraph",
            tint = if (currentPara < totalParas - 1) TextPrimaryNight else TextMutedNight
          )
        }

        // Stop
        IconButton(
          onClick = { viewModel.stopNarration() },
          enabled = isNarrating,
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(NightSurfaceVariant)
            .testTag("narrator_stop_button")
        ) {
          Icon(
            imageVector = Icons.Default.Stop,
            contentDescription = "Stop Narration",
            tint = if (isNarrating) VermilionCourier else TextMutedNight
          )
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(20.dp))

  // 1. ACTIVE VOICE BANNER & TEST BUTTON
  ActiveVoicePreviewCard(
    viewModel = viewModel,
    uiState = uiState
  )

  Spacer(modifier = Modifier.height(22.dp))

  // 2. STORYBOOK VOICE PERSONAS
  Text(
    text = "SELECT NARRATOR VOICE",
    color = TextMutedNight,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.sp
  )

  Spacer(modifier = Modifier.height(10.dp))

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    NarratorPersona.values().forEach { persona ->
      val isSelected = uiState.selectedVoiceName == null && uiState.selectedPersona == persona
      VoicePersonaCard(
        persona = persona,
        isSelected = isSelected,
        onSelect = { viewModel.selectPersona(persona) },
        onTest = {
          viewModel.previewVoiceSample("The wind over the Cloud Sea carries memories of fallen towers and skybound voyages.")
        }
      )
    }
  }

  // 3. DEVICE SYSTEM VOICES (COLLAPSIBLE)
  if (uiState.availableVoices.isNotEmpty()) {
    Spacer(modifier = Modifier.height(18.dp))
    DeviceVoicesSection(
      viewModel = viewModel,
      uiState = uiState
    )
  }

  Spacer(modifier = Modifier.height(22.dp))

  // 4. VOICE PITCH & TONE
  VoicePitchSection(
    currentPitch = uiState.narrationPitch,
    onPitchChanged = { viewModel.setNarrationPitch(it) }
  )

  Spacer(modifier = Modifier.height(18.dp))

  // 5. NARRATION SPEED
  NarrationSpeedSection(
    currentSpeed = uiState.narrationSpeed,
    onSpeedChanged = { viewModel.setNarrationSpeed(it) }
  )

  Spacer(modifier = Modifier.height(20.dp))

  // 6. RESET TO DEFAULTS
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    OutlinedButton(
      onClick = { viewModel.resetVoiceDefaults() },
      modifier = Modifier.testTag("reset_voice_defaults_button"),
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondaryNight),
      border = androidx.compose.foundation.BorderStroke(1.dp, NightCardBorder)
    ) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = null,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text("Reset to Default Story Voice", fontSize = 12.sp)
    }
  }
}

@Composable
private fun ActiveVoicePreviewCard(
  viewModel: StoryViewModel,
  uiState: StoryUiState
) {
  val isPreviewing = uiState.isPreviewingVoice
  val voiceTitle = if (uiState.selectedVoiceName != null) {
    val devVoice = uiState.availableVoices.find { it.name == uiState.selectedVoiceName }
    devVoice?.label ?: "Custom Device Voice"
  } else {
    uiState.selectedPersona.title
  }

  val voiceTag = if (uiState.selectedVoiceName != null) {
    "Custom Device Voice"
  } else {
    uiState.selectedPersona.characterTag
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, if (isPreviewing) AmberLamp else NightCardBorder, RoundedCornerShape(14.dp))
      .testTag("active_voice_card"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant.copy(alpha = 0.7f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isPreviewing) AmberLamp.copy(alpha = 0.2f) else BlueGlass.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPreviewing) Icons.Default.GraphicEq else Icons.Default.RecordVoiceOver,
            contentDescription = null,
            tint = if (isPreviewing) AmberLamp else BlueGlass,
            modifier = Modifier.size(22.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = "ACTIVE VOICE",
            color = if (isPreviewing) AmberLamp else TextMutedNight,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Text(
            text = voiceTitle,
            color = TextPrimaryNight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
          Text(
            text = voiceTag,
            color = AmberLamp,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Button(
        onClick = {
          if (isPreviewing) {
            viewModel.stopVoicePreview()
          } else {
            viewModel.previewVoiceSample("The wind over the Cloud Sea carries memories of fallen towers.")
          }
        },
        modifier = Modifier.testTag("preview_voice_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isPreviewing) VermilionCourier else AmberLamp,
          contentColor = if (isPreviewing) androidx.compose.ui.graphics.Color.White else VoidDark
        )
      ) {
        Icon(
          imageVector = if (isPreviewing) Icons.Default.Stop else Icons.Default.PlayArrow,
          contentDescription = if (isPreviewing) "Stop Sample" else "Test Voice",
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (isPreviewing) "Stop" else "Sample",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
private fun VoicePersonaCard(
  persona: NarratorPersona,
  isSelected: Boolean,
  onSelect: () -> Unit,
  onTest: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) AmberLamp else NightCardBorder,
        shape = RoundedCornerShape(12.dp)
      )
      .clickable { onSelect() }
      .testTag("persona_card_${persona.id.lowercase()}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) AmberLamp.copy(alpha = 0.08f) else NightSurface
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = persona.title,
            color = if (isSelected) AmberLamp else TextPrimaryNight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(if (isSelected) AmberLamp.copy(alpha = 0.2f) else NightSurfaceVariant)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = persona.characterTag,
              color = if (isSelected) AmberLamp else BlueGlass,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = persona.description,
          color = TextSecondaryNight,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Speed: ${persona.speechRate}x • Pitch: ${persona.pitch}x",
            color = TextMutedNight,
            fontSize = 10.sp
          )
          if (isSelected) {
            Text(
              text = "✓ ACTIVE",
              color = AmberLamp,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = {
          onSelect()
          onTest()
        },
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(NightSurfaceVariant)
          .testTag("test_persona_${persona.id.lowercase()}"),
      ) {
        Icon(
          imageVector = Icons.Default.PlayArrow,
          contentDescription = "Audition ${persona.title}",
          tint = if (isSelected) AmberLamp else TextSecondaryNight,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
private fun DeviceVoicesSection(
  viewModel: StoryViewModel,
  uiState: StoryUiState
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, NightCardBorder, RoundedCornerShape(12.dp)),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = NightSurface)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { isExpanded = !isExpanded }
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = null,
            tint = BlueGlass,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "DEVICE VOICES (${uiState.availableVoices.size})",
            color = TextPrimaryNight,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )
        }

        Icon(
          imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = if (isExpanded) "Collapse" else "Expand",
          tint = TextSecondaryNight
        )
      }

      if (isExpanded) {
        HorizontalDivider(color = NightCardBorder)

        if (uiState.selectedVoiceName != null) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(AmberLamp.copy(alpha = 0.1f))
              .clickable { viewModel.clearDeviceVoiceToPersona() }
              .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Custom device voice active.",
              color = AmberLamp,
              fontSize = 11.sp
            )
            Text(
              text = "USE STORY PERSONA",
              color = AmberLamp,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
          HorizontalDivider(color = NightCardBorder)
        }

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          uiState.availableVoices.take(12).forEach { voice ->
            val isSelected = uiState.selectedVoiceName == voice.name
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) BlueGlass.copy(alpha = 0.15f) else androidx.compose.ui.graphics.Color.Transparent)
                .clickable {
                  viewModel.selectDeviceVoice(voice)
                  viewModel.previewVoiceSample()
                }
                .padding(horizontal = 10.dp, vertical = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = voice.label,
                  color = if (isSelected) BlueGlass else TextPrimaryNight,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                  text = "Locale: ${voice.locale.displayName}",
                  color = TextMutedNight,
                  fontSize = 10.sp
                )
              }

              if (isSelected) {
                Text(
                  text = "ACTIVE",
                  color = BlueGlass,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun VoicePitchSection(
  currentPitch: Float,
  onPitchChanged: (Float) -> Unit
) {
  val pitchLabel = when {
    currentPitch < 0.80f -> "Deep & Solemn (${String.format(java.util.Locale.US, "%.2f", currentPitch)}x)"
    currentPitch < 0.95f -> "Warm Storyteller (${String.format(java.util.Locale.US, "%.2f", currentPitch)}x)"
    currentPitch <= 1.05f -> "Natural Tone (${String.format(java.util.Locale.US, "%.2f", currentPitch)}x)"
    else -> "Crisp & High (${String.format(java.util.Locale.US, "%.2f", currentPitch)}x)"
  }

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "VOICE PITCH & REGISTER",
      color = TextMutedNight,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )
    Text(
      text = pitchLabel,
      color = BlueGlass,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold
    )
  }

  Spacer(modifier = Modifier.height(4.dp))

  Slider(
    value = currentPitch,
    onValueChange = onPitchChanged,
    valueRange = 0.70f..1.30f,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("narration_pitch_slider"),
    colors = SliderDefaults.colors(
      thumbColor = BlueGlass,
      activeTrackColor = BlueGlass,
      inactiveTrackColor = NightSurfaceVariant
    )
  )

  Spacer(modifier = Modifier.height(6.dp))

  val pitchPresets = listOf(
    Pair("Deep", 0.74f),
    Pair("Warm", 0.88f),
    Pair("Natural", 1.0f),
    Pair("Crisp", 1.15f)
  )

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    pitchPresets.forEach { (label, pitch) ->
      val isSelected = kotlin.math.abs(currentPitch - pitch) < 0.05f
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(8.dp))
          .background(if (isSelected) BlueGlass.copy(alpha = 0.2f) else NightSurface)
          .border(1.dp, if (isSelected) BlueGlass else NightCardBorder, RoundedCornerShape(8.dp))
          .clickable { onPitchChanged(pitch) }
          .padding(vertical = 8.dp)
          .testTag("pitch_${label.lowercase()}"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = label,
          color = if (isSelected) BlueGlass else TextSecondaryNight,
          fontSize = 11.sp,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
      }
    }
  }
}

@Composable
private fun NarrationSpeedSection(
  currentSpeed: Float,
  onSpeedChanged: (Float) -> Unit
) {
  val speedLabel = String.format(java.util.Locale.US, "%.2fx", currentSpeed)

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "NARRATION SPEED & TEMPO",
      color = TextMutedNight,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )
    Text(
      text = speedLabel,
      color = AmberLamp,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold
    )
  }

  Spacer(modifier = Modifier.height(4.dp))

  Slider(
    value = currentSpeed,
    onValueChange = onSpeedChanged,
    valueRange = 0.70f..1.40f,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("narration_speed_slider"),
    colors = SliderDefaults.colors(
      thumbColor = AmberLamp,
      activeTrackColor = AmberLamp,
      inactiveTrackColor = NightSurfaceVariant
    )
  )

  Spacer(modifier = Modifier.height(6.dp))

  val speedPresets = listOf(0.75f, 0.90f, 1.0f, 1.15f, 1.25f)

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    speedPresets.forEach { speed ->
      val isSelected = kotlin.math.abs(currentSpeed - speed) < 0.04f
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(8.dp))
          .background(if (isSelected) AmberLamp.copy(alpha = 0.2f) else NightSurface)
          .border(1.dp, if (isSelected) AmberLamp else NightCardBorder, RoundedCornerShape(8.dp))
          .clickable { onSpeedChanged(speed) }
          .padding(vertical = 8.dp)
          .testTag("speed_${speed}x"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "${speed}x",
          color = if (isSelected) AmberLamp else TextSecondaryNight,
          fontSize = 11.sp,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
      }
    }
  }
}

@Composable
private fun AmbientSection(
  viewModel: StoryViewModel,
  uiState: StoryUiState
) {
  // Master Ambient Toggle
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NightSurface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(if (uiState.isAudioPlaying) BlueGlass.copy(alpha = 0.2f) else NightSurfaceVariant),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (uiState.isAudioPlaying) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.VolumeMute,
            contentDescription = null,
            tint = if (uiState.isAudioPlaying) BlueGlass else TextMutedNight,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = "Atmospheric Soundscape",
            color = TextPrimaryNight,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = if (uiState.isAudioPlaying) "Synthesizing live audio" else "Soundscape muted",
            color = if (uiState.isAudioPlaying) BlueGlass else TextMutedNight,
            fontSize = 12.sp
          )
        }
      }

      Switch(
        checked = uiState.isAudioPlaying,
        onCheckedChange = { viewModel.toggleAudio() },
        colors = SwitchDefaults.colors(
          checkedThumbColor = VoidDark,
          checkedTrackColor = BlueGlass,
          uncheckedThumbColor = TextMutedNight,
          uncheckedTrackColor = NightSurfaceVariant
        ),
        modifier = Modifier.testTag("ambient_master_switch")
      )
    }
  }

  Spacer(modifier = Modifier.height(18.dp))

  // Volume Slider
  Text(
    text = "SOUNDSCAPE VOLUME (${(uiState.ambientVolume * 100).toInt()}%)",
    color = TextMutedNight,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.sp
  )

  Spacer(modifier = Modifier.height(4.dp))

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.VolumeDown,
      contentDescription = null,
      tint = TextMutedNight,
      modifier = Modifier.size(20.dp)
    )

    Slider(
      value = uiState.ambientVolume,
      onValueChange = { viewModel.setAmbientVolume(it) },
      valueRange = 0.0f..1.0f,
      colors = SliderDefaults.colors(
        thumbColor = AmberLamp,
        activeTrackColor = AmberLamp,
        inactiveTrackColor = NightSurfaceVariant
      ),
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 8.dp)
        .testTag("ambient_volume_slider")
    )

    Icon(
      imageVector = Icons.AutoMirrored.Filled.VolumeUp,
      contentDescription = null,
      tint = AmberLamp,
      modifier = Modifier.size(20.dp)
    )
  }

  Spacer(modifier = Modifier.height(18.dp))

  // Soundscape Atmosphere Presets
  Text(
    text = "ATMOSPHERE PRESETS",
    color = TextMutedNight,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.sp
  )

  Spacer(modifier = Modifier.height(10.dp))

  SoundscapePreset.values().forEach { preset ->
    val isSelected = uiState.ambientPreset == preset

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)
        .border(
          width = 1.dp,
          color = if (isSelected) BlueGlass else NightCardBorder,
          shape = RoundedCornerShape(12.dp)
        )
        .clickable { viewModel.setAmbientPreset(preset) }
        .testTag("preset_${preset.name.lowercase()}"),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(
        containerColor = if (isSelected) BlueGlass.copy(alpha = 0.12f) else NightSurface
      )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(if (isSelected) BlueGlass else NightSurfaceVariant)
            .border(1.dp, if (isSelected) BlueGlass else Color(0xFF657B96), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          if (isSelected) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(VoidDark)
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = preset.title,
            color = if (isSelected) BlueGlass else TextPrimaryNight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = preset.description,
            color = if (isSelected) AmberLampSoft else TextSecondaryNight,
            fontSize = 11.sp,
            fontFamily = FontFamily.Serif
          )
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(14.dp))

  // Ducking toggle
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(NightSurface)
      .border(1.dp, NightCardBorder, RoundedCornerShape(12.dp))
      .padding(horizontal = 14.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = "Auto-Duck Ambient During Speech",
        color = TextPrimaryNight,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = "Lowers ambient volume while narrator speaks",
        color = TextSecondaryNight,
        fontSize = 11.sp
      )
    }

    Switch(
      checked = uiState.ambientDucking,
      onCheckedChange = { viewModel.toggleAmbientDucking() },
      colors = SwitchDefaults.colors(
        checkedThumbColor = VoidDark,
        checkedTrackColor = BlueGlass,
        uncheckedThumbColor = TextMutedNight,
        uncheckedTrackColor = NightSurfaceVariant
      ),
      modifier = Modifier.testTag("ambient_ducking_switch")
    )
  }
}
