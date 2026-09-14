package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LanternMode
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.VoidDark

@Composable
fun LanternModeBar(
  activeMode: LanternMode,
  onModeSelected: (LanternMode) -> Unit,
  isAudioPlaying: Boolean,
  isNarrating: Boolean,
  onToggleAudio: () -> Unit,
  onOpenAudioOptions: () -> Unit,
  modifier: Modifier = Modifier
) {
  val modes = listOf(
    Triple(LanternMode.STANDARD, "Courier", Icons.Default.Shield),
    Triple(LanternMode.BLUE_GLASS, "Blue Glass", Icons.Default.Navigation),
    Triple(LanternMode.AMBER_LAMP, "Amber Lamp", Icons.Default.Lightbulb),
    Triple(LanternMode.MOONLIGHT, "Moon-Koi", Icons.Default.NightlightRound)
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(VoidDark.copy(alpha = 0.94f))
      .padding(horizontal = 10.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Scrollable pill selector
    Row(
      modifier = Modifier
        .weight(1f)
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      modes.forEach { (mode, label, icon) ->
        val isSelected = activeMode == mode
        val accentColor = when (mode) {
          LanternMode.STANDARD -> Color(0xFF8A99AD)
          LanternMode.BLUE_GLASS -> BlueGlass
          LanternMode.AMBER_LAMP -> AmberLamp
          LanternMode.MOONLIGHT -> MoonSilver
        }

        val animatedBg by animateColorAsState(
          targetValue = if (isSelected) accentColor.copy(alpha = 0.22f) else NightSurfaceVariant.copy(alpha = 0.6f),
          label = "mode_bg"
        )
        val animatedBorder by animateColorAsState(
          targetValue = if (isSelected) accentColor else NightCardBorder,
          label = "mode_border"
        )

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(animatedBg)
            .border(1.dp, animatedBorder, RoundedCornerShape(20.dp))
            .clickable { onModeSelected(mode) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("lantern_mode_${mode.name.lowercase()}"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) accentColor else Color(0xFF8A99AD),
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) accentColor else Color(0xFFCAD5E2)
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(6.dp))

    // Audio Controls Row
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      // Ambient Audio quick toggle button
      IconButton(
        onClick = onToggleAudio,
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(
            if (isAudioPlaying) Brush.radialGradient(
              listOf(BlueGlass.copy(alpha = 0.3f), NightSurfaceVariant)
            ) else Brush.radialGradient(
              listOf(NightSurfaceVariant, VoidDark)
            )
          )
          .border(
            1.dp,
            if (isAudioPlaying) BlueGlass else NightCardBorder,
            CircleShape
          )
          .testTag("audio_toggle_button")
      ) {
        Icon(
          imageVector = if (isAudioPlaying) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.VolumeMute,
          contentDescription = if (isAudioPlaying) "Mute ambient cloud sea" else "Play ambient cloud sea",
          tint = if (isAudioPlaying) BlueGlass else Color(0xFF8A99AD),
          modifier = Modifier.size(18.dp)
        )
      }

      // Audio Options Sheet trigger (Headphones / Equalizer)
      IconButton(
        onClick = onOpenAudioOptions,
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(
            if (isNarrating) Brush.radialGradient(
              listOf(AmberLamp.copy(alpha = 0.3f), NightSurfaceVariant)
            ) else Brush.radialGradient(
              listOf(NightSurfaceVariant, VoidDark)
            )
          )
          .border(
            1.dp,
            if (isNarrating) AmberLamp else NightCardBorder,
            CircleShape
          )
          .testTag("audio_options_button")
      ) {
        Icon(
          imageVector = if (isNarrating) Icons.Default.GraphicEq else Icons.Default.Headphones,
          contentDescription = "Audio Options and Narration",
          tint = if (isNarrating) AmberLamp else Color(0xFFCAD5E2),
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
