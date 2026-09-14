package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.PainterlyStyle
import com.example.ai.ScenePromptsCatalog
import com.example.ui.StoryUiState
import com.example.ui.StoryViewModel
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurface
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.TextMutedNight
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VermilionCourier
import com.example.ui.theme.VoidDark

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun IllustrationStudioSheet(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val scrollState = rememberScrollState()

  val curatedPrompts = ScenePromptsCatalog.getPromptsForChapter(uiState.currentChapterId)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = DeepNight,
    dragHandle = null,
    modifier = Modifier.testTag("illustration_studio_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 680.dp)
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .verticalScroll(scrollState)
    ) {
      // Header Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(AmberLamp.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Palette,
              contentDescription = null,
              tint = AmberLamp,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "IMAGEN SCENE STUDIO",
              color = AmberLamp,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.2.sp
            )
            Text(
              text = "Atmospheric Painterly Illustrations",
              color = TextPrimaryNight,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Serif
            )
          }
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("close_studio_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close Studio",
            tint = TextSecondaryNight
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = NightCardBorder
      )

      // Curated Scene Prompts quick chips
      if (curatedPrompts.isNotEmpty()) {
        Text(
          text = "CHAPTER SCENE INSPIRATIONS",
          color = TextMutedNight,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          curatedPrompts.forEach { scene ->
            val isSelected = uiState.studioPromptText == scene.prompt
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) AmberLamp.copy(alpha = 0.2f) else NightSurface)
                .border(1.dp, if (isSelected) AmberLamp else NightCardBorder, RoundedCornerShape(8.dp))
                .clickable {
                  viewModel.setStudioSceneTitle(scene.title)
                  viewModel.setStudioPromptText(scene.prompt)
                  viewModel.setStudioStyle(scene.defaultStyle)
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("curated_prompt_${scene.id}")
            ) {
              Text(
                text = scene.title,
                color = if (isSelected) AmberLamp else TextSecondaryNight,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // Scene Title Field
      Text(
        text = "SCENE TITLE",
        color = TextMutedNight,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = uiState.studioSceneTitle,
        onValueChange = { viewModel.setStudioSceneTitle(it) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("studio_title_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = NightSurface,
          unfocusedContainerColor = NightSurface,
          focusedBorderColor = AmberLamp,
          unfocusedBorderColor = NightCardBorder,
          focusedTextColor = TextPrimaryNight,
          unfocusedTextColor = TextPrimaryNight
        ),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        placeholder = { Text("e.g. Flight over the Cloudsea", color = TextMutedNight) }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Scene Description / Prompt Field
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "SCENE DESCRIPTION & VISUAL PROMPT",
          color = TextMutedNight,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        if (uiState.studioPromptText.isNotEmpty()) {
          Text(
            text = "CLEAR",
            color = AmberLamp,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .clickable { viewModel.setStudioPromptText("") }
              .padding(4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = uiState.studioPromptText,
        onValueChange = { viewModel.setStudioPromptText(it) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("studio_prompt_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = NightSurface,
          unfocusedContainerColor = NightSurface,
          focusedBorderColor = AmberLamp,
          unfocusedBorderColor = NightCardBorder,
          focusedTextColor = TextPrimaryNight,
          unfocusedTextColor = TextPrimaryNight
        ),
        shape = RoundedCornerShape(10.dp),
        minLines = 3,
        maxLines = 5,
        placeholder = {
          Text(
            "Describe the scene, lightning over the floating archipelago, copper wings of the wind-skiff...",
            color = TextMutedNight,
            fontSize = 13.sp
          )
        }
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Painterly Art Style Selector
      Text(
        text = "PAINTERLY ART STYLE",
        color = TextMutedNight,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        PainterlyStyle.values().forEach { style ->
          val isSelected = uiState.studioSelectedStyle == style
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) AmberLamp else NightCardBorder,
                shape = RoundedCornerShape(10.dp)
              )
              .clickable { viewModel.setStudioStyle(style) }
              .testTag("style_${style.id.lowercase()}"),
            shape = RoundedCornerShape(10.dp),
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
                Text(
                  text = style.displayName,
                  color = if (isSelected) AmberLamp else TextPrimaryNight,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = style.description,
                  color = TextSecondaryNight,
                  fontSize = 11.sp
                )
              }

              if (isSelected) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(AmberLamp)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "SELECTED",
                    color = VoidDark,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Aspect Ratio Selector
      Text(
        text = "ASPECT RATIO",
        color = TextMutedNight,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      val ratios = listOf(
        Pair("16:9", "Cinematic Landscape"),
        Pair("4:3", "Classic Storybook"),
        Pair("1:1", "Square Codex")
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ratios.forEach { (ratio, label) ->
          val isSelected = uiState.studioAspectRatio == ratio
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) BlueGlass.copy(alpha = 0.2f) else NightSurface)
              .border(1.dp, if (isSelected) BlueGlass else NightCardBorder, RoundedCornerShape(8.dp))
              .clickable { viewModel.setStudioAspectRatio(ratio) }
              .padding(vertical = 8.dp)
              .testTag("ratio_$ratio"),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = ratio,
                color = if (isSelected) BlueGlass else TextPrimaryNight,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = label,
                color = TextMutedNight,
                fontSize = 9.sp,
                maxLines = 1
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Status Note / Secrets Info
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant.copy(alpha = 0.6f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = BlueGlass,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Imagen 3 uses Google's image generation model. You can configure your GEMINI_API_KEY in the AI Studio Secrets panel. Archive story plates are available offline.",
            color = TextSecondaryNight,
            fontSize = 11.sp,
            lineHeight = 15.sp
          )
        }
      }

      if (uiState.generationStatusMessage != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = uiState.generationStatusMessage,
          color = AmberLamp,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Generate Button
      Button(
        onClick = {
          viewModel.generateSceneIllustration()
        },
        enabled = !uiState.isGeneratingIllustration && uiState.studioPromptText.isNotBlank(),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("generate_scene_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = AmberLamp,
          contentColor = VoidDark,
          disabledContainerColor = NightSurfaceVariant,
          disabledContentColor = TextMutedNight
        )
      ) {
        if (uiState.isGeneratingIllustration) {
          CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = VoidDark,
            strokeWidth = 2.dp
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Conjuring Illustration with Imagen...",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        } else {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Conjure Scene with Imagen",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
