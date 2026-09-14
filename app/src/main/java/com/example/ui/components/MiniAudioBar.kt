package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Chapter
import com.example.ui.StoryUiState
import com.example.ui.StoryViewModel
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.TextMutedNight
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VermilionCourier
import com.example.ui.theme.VoidDark

@Composable
fun MiniAudioBar(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  currentChapter: Chapter,
  onOpenAudioOptions: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isVisible = uiState.isNarrating || uiState.isAudioPlaying

  AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
    exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(DeepNight.copy(alpha = 0.95f))
        .border(
          width = 1.dp,
          brush = Brush.horizontalGradient(listOf(BlueGlass, AmberLamp)),
          shape = RoundedCornerShape(16.dp)
        )
        .clickable { onOpenAudioOptions() }
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .testTag("mini_audio_bar"),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(BlueGlass.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (uiState.isNarrating) Icons.Default.GraphicEq else Icons.Default.VolumeUp,
            contentDescription = null,
            tint = BlueGlass,
            modifier = Modifier.size(16.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Text(
            text = if (uiState.isNarrating) {
              if (uiState.isNarrationPaused) "Narration Paused" else "Narrating: Para ${uiState.narratingParagraphIndex + 1}/${currentChapter.paragraphs.size}"
            } else {
              "Ambient: ${uiState.ambientPreset.title}"
            },
            color = TextPrimaryNight,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
          Text(
            text = if (uiState.isNarrating) "Tap to open audio settings" else "Soundscape playing (${(uiState.ambientVolume * 100).toInt()}%)",
            color = AmberLamp,
            fontSize = 10.sp
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        if (uiState.isNarrating) {
          // Play / Pause toggle
          IconButton(
            onClick = {
              if (uiState.isNarrationPaused) {
                viewModel.resumeNarration()
              } else {
                viewModel.pauseNarration()
              }
            },
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(BlueGlass)
              .testTag("mini_audio_play_pause")
          ) {
            Icon(
              imageVector = if (uiState.isNarrationPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
              contentDescription = "Toggle Narration",
              tint = VoidDark,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Skip to next paragraph
          IconButton(
            onClick = { viewModel.nextNarratorParagraph() },
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.FastForward,
              contentDescription = "Next Paragraph",
              tint = TextSecondaryNight,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        // Open audio options
        IconButton(
          onClick = onOpenAudioOptions,
          modifier = Modifier.size(28.dp).testTag("mini_audio_settings_button")
        ) {
          Icon(
            imageVector = Icons.Default.Headphones,
            contentDescription = "Audio Options",
            tint = AmberLamp,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}
