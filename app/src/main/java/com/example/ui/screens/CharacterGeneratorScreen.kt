package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.character.CharacterGeneratorEngine
import com.example.character.CharacterStats
import com.example.character.LanternAffinity
import com.example.character.OriginIsle
import com.example.character.SkyfarerArchetype
import com.example.character.SkyfarerCharacter
import com.example.data.CourierRig
import com.example.ui.CharacterGeneratorTab
import com.example.ui.StoryUiState
import com.example.ui.StoryViewModel
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurface
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.ParchmentGold
import com.example.ui.theme.TextMutedNight
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VermilionCourier
import com.example.ui.theme.VoidDark
import java.io.File

@Composable
fun CharacterGeneratorScreen(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  // Status message toast
  LaunchedEffect(uiState.characterStatusMessage) {
    uiState.characterStatusMessage?.let { msg ->
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
      viewModel.clearCharacterStatusMessage()
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DeepNight)
  ) {
    // Top Bar Header
    Surface(
      color = DeepNight,
      border = BorderStroke(1.dp, NightCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Archipelago Character Forge",
                color = AmberLamp,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(AmberLamp.copy(alpha = 0.2f))
                  .border(1.dp, AmberLamp.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "CREATOR",
                  color = AmberLamp,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Text(
              text = "Forge salt couriers, mist-navigators, and skyfarers of the Cloudsea",
              color = TextSecondaryNight,
              fontSize = 12.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          // Active equipped courier badge
          uiState.activeCharacter?.let { active ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(BlueGlass.copy(alpha = 0.15f))
                .border(1.dp, BlueGlass.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = BlueGlass,
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = active.name,
                  color = BlueGlass,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tabs: Forge & Conjure vs Roster
        TabRow(
          selectedTabIndex = uiState.characterGeneratorTab.ordinal,
          containerColor = DeepNight,
          contentColor = AmberLamp,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.characterGeneratorTab.ordinal]),
              color = AmberLamp
            )
          },
          divider = {}
        ) {
          CharacterGeneratorTab.values().forEach { tab ->
            val isSelected = uiState.characterGeneratorTab == tab
            val tabTitle = if (tab == CharacterGeneratorTab.ROSTER) {
              "Courier Roster (${uiState.savedRoster.size})"
            } else {
              tab.label
            }

            Tab(
              selected = isSelected,
              onClick = { viewModel.setCharacterGeneratorTab(tab) },
              text = {
                Text(
                  text = tabTitle,
                  color = if (isSelected) AmberLamp else TextMutedNight,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 13.sp
                )
              }
            )
          }
        }
      }
    }

    // Tab Contents
    when (uiState.characterGeneratorTab) {
      CharacterGeneratorTab.FORGE -> {
        ForgeTabContent(
          viewModel = viewModel,
          uiState = uiState
        )
      }
      CharacterGeneratorTab.ROSTER -> {
        RosterTabContent(
          viewModel = viewModel,
          uiState = uiState
        )
      }
    }
  }
}

@Composable
private fun ForgeTabContent(
  viewModel: StoryViewModel,
  uiState: StoryUiState
) {
  val character = uiState.generatedCharacter ?: remember {
    CharacterGeneratorEngine.generateFullProceduralCharacter()
  }
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Generation Control Bar
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp))
          .testTag("forge_control_bar"),
        colors = CardDefaults.cardColors(containerColor = NightSurface),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Conjuration Engine",
            color = TextPrimaryNight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Generate a completely balanced skyfarer or let the Gemini Chronicler weave their tale.",
            color = TextSecondaryNight,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Instant Procedural Roll
            Button(
              onClick = { viewModel.generateProceduralCharacter() },
              enabled = !uiState.isGeneratingCharacter,
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("conjure_random_character_button"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = AmberLamp,
                contentColor = VoidDark
              )
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Conjure Random",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            // AI Chronicler (Gemini)
            OutlinedButton(
              onClick = {
                viewModel.generateAiCharacter(
                  archetype = character.archetype,
                  origin = character.originIsle,
                  lantern = character.lanternAffinity
                )
              },
              enabled = !uiState.isGeneratingCharacter,
              modifier = Modifier
                .weight(1.1f)
                .height(44.dp)
                .testTag("conjure_ai_character_button"),
              shape = RoundedCornerShape(10.dp),
              border = BorderStroke(1.dp, AmberLamp.copy(alpha = 0.7f)),
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AmberLamp
              )
            ) {
              if (uiState.isGeneratingCharacter) {
                CircularProgressIndicator(
                  modifier = Modifier.size(16.dp),
                  color = AmberLamp,
                  strokeWidth = 2.dp
                )
              } else {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = AmberLamp,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (uiState.isGeneratingCharacter) "Chronicling..." else "Gemini Chronicle",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }

    // 2. Character Portrait & Header Blueprint Card
    item {
      CharacterDossierCard(
        character = character,
        isGeneratingPortrait = uiState.isGeneratingPortrait,
        onConjurePortrait = { viewModel.conjurePortraitForCurrentCharacter() }
      )
    }

    // 3. Archetype Selector Carousel
    item {
      ArchetypeSelectorSection(
        selectedArchetype = character.archetype,
        onArchetypeSelected = { newArch ->
          val updatedStats = CharacterGeneratorEngine.generateProceduralStats(newArch)
          val updated = character.copy(
            archetype = newArch,
            title = CharacterGeneratorEngine.rollRandomTitle(newArch, character.originIsle),
            stats = updatedStats,
            gliderRig = newArch.recommendedRig,
            lanternAffinity = newArch.recommendedLantern,
            portraitDrawableRes = newArch.defaultDrawableRes,
            customQuote = CharacterGeneratorEngine.generateProceduralQuote(newArch, character.name)
          )
          viewModel.updateDraftCharacter(updated)
        }
      )
    }

    // 4. Origin Isle Selector
    item {
      OriginIsleSelectorSection(
        selectedOrigin = character.originIsle,
        onOriginSelected = { newOrigin ->
          val updated = character.copy(
            originIsle = newOrigin,
            backstory = CharacterGeneratorEngine.generateProceduralBackstory(
              character.name,
              character.archetype,
              newOrigin,
              character.signatureRelic,
              character.motivation
            )
          )
          viewModel.updateDraftCharacter(updated)
        }
      )
    }

    // 5. Lantern Flame Affinity & Glider Rig
    item {
      LanternAndRigSection(
        character = character,
        onLanternSelected = { newLantern ->
          viewModel.updateDraftCharacter(character.copy(lanternAffinity = newLantern))
        },
        onRigSelected = { newRig ->
          viewModel.updateDraftCharacter(character.copy(gliderRig = newRig))
        }
      )
    }

    // 6. Attributes & Stats
    item {
      CharacterStatsEditorSection(
        stats = character.stats,
        onStatsChanged = { newStats ->
          viewModel.updateDraftCharacter(character.copy(stats = newStats))
        },
        onResetArchetypeStats = {
          val resetStats = CharacterGeneratorEngine.generateProceduralStats(character.archetype)
          viewModel.updateDraftCharacter(character.copy(stats = resetStats))
        }
      )
    }

    // 7. Identity & Lore Details (Editable Name, Relic, Quirk, Motivation)
    item {
      IdentityAndLoreEditorSection(
        character = character,
        onCharacterUpdated = { updated ->
          viewModel.updateDraftCharacter(updated)
        }
      )
    }

    // 8. Bottom Action Tray
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Save to Roster
        Button(
          onClick = { viewModel.saveCurrentCharacterToRoster() },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("save_to_roster_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = AmberLamp,
            contentColor = VoidDark
          )
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Save to Roster",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Equip as Active Courier
        OutlinedButton(
          onClick = { viewModel.setActiveCharacter(character) },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("equip_active_courier_button"),
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, BlueGlass),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = BlueGlass
          )
        ) {
          Icon(
            imageVector = Icons.Default.Flight,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Equip as Active",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        // Copy Dossier
        IconButton(
          onClick = {
            val dossier = buildString {
              append("═══ SKYBOUND COURIER DOSSIER ═══\n")
              append("Name: ${character.name}\n")
              append("Title: ${character.title}\n")
              append("Archetype: ${character.archetype.title} (${character.archetype.subtitle})\n")
              append("Origin: ${character.originIsle.isleName} [${character.originIsle.region}]\n")
              append("Lantern Flame: ${character.lanternAffinity.flameName}\n")
              append("Glider Rig: ${character.gliderRig.displayName}\n\n")
              append("── ATTRIBUTES ──\n")
              append("Wind-Sense: ${character.stats.windSense}/10\n")
              append("Grit & Altitude: ${character.stats.gritAltitude}/10\n")
              append("Lantern Craft: ${character.stats.lanternCraft}/10\n")
              append("Lore & Wisdom: ${character.stats.loreWisdom}/10\n")
              append("Tiller Agility: ${character.stats.tillerAgility}/10\n\n")
              append("Signature Relic: ${character.signatureRelic}\n")
              append("Personality Quirk: ${character.personalityQuirk}\n")
              append("Motivation: ${character.motivation}\n\n")
              append("── BACKSTORY ──\n${character.backstory}\n\n")
              append("Quote: \"${character.customQuote}\"")
            }
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Skyfarer Dossier", dossier)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Dossier copied to clipboard!", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NightSurface)
            .border(1.dp, NightCardBorder, RoundedCornerShape(12.dp))
        ) {
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy Dossier",
            tint = TextSecondaryNight
          )
        }
      }
    }
  }
}

@Composable
private fun CharacterDossierCard(
  character: SkyfarerCharacter,
  isGeneratingPortrait: Boolean,
  onConjurePortrait: () -> Unit
) {
  val customBitmap = remember(character.customPortraitPath) {
    character.customPortraitPath?.let { path ->
      val file = File(path)
      if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, NightCardBorder, RoundedCornerShape(20.dp))
      .testTag("character_dossier_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = NightSurface)
  ) {
    Column {
      // Portrait Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(210.dp)
      ) {
        if (customBitmap != null) {
          Image(
            bitmap = customBitmap.asImageBitmap(),
            contentDescription = "${character.name} Portrait",
            modifier = Modifier
              .fillMaxSize()
              .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            contentScale = ContentScale.Crop
          )
        } else {
          Image(
            painter = painterResource(id = character.portraitDrawableRes),
            contentDescription = character.archetype.title,
            modifier = Modifier
              .fillMaxSize()
              .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            contentScale = ContentScale.Crop
          )
        }

        // Gradient Shade at bottom of image
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, NightSurface.copy(alpha = 0.9f)),
                startY = 120f
              )
            )
        )

        // AI / Origin Badges
        Row(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          if (character.isAiGenerated) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AmberLamp)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "GEMINI CHRONICLED",
                color = VoidDark,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }

          if (customBitmap != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(BlueGlass)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "IMAGEN PORTRAIT",
                color = VoidDark,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }
        }

        // Conjure Portrait Button
        Box(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(VoidDark.copy(alpha = 0.85f))
            .border(1.dp, AmberLamp.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .clickable(enabled = !isGeneratingPortrait) { onConjurePortrait() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            if (isGeneratingPortrait) {
              CircularProgressIndicator(
                modifier = Modifier.size(12.dp),
                color = AmberLamp,
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = AmberLamp,
                modifier = Modifier.size(13.dp)
              )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isGeneratingPortrait) "Painting..." else "Paint Portrait",
              color = AmberLamp,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Dossier Text Header
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = character.name,
          color = TextPrimaryNight,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif
        )
        Text(
          text = character.title,
          color = AmberLamp,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Attribute summary chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          DossierChip(
            label = character.archetype.title,
            color = BlueGlass
          )
          DossierChip(
            label = character.originIsle.isleName,
            color = AmberLamp
          )
          DossierChip(
            label = character.lanternAffinity.flameName,
            color = Color(android.graphics.Color.parseColor(character.lanternAffinity.colorHex))
          )
          DossierChip(
            label = character.gliderRig.displayName,
            color = TextSecondaryNight
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quote Block
        Surface(
          color = DeepNight.copy(alpha = 0.6f),
          border = BorderStroke(1.dp, NightCardBorder),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .width(3.dp)
                .height(32.dp)
                .background(AmberLamp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "\"${character.customQuote}\"",
              color = ParchmentGold,
              fontSize = 12.sp,
              fontStyle = FontStyle.Italic,
              fontFamily = FontFamily.Serif
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Backstory
        Text(
          text = character.backstory,
          color = TextSecondaryNight,
          fontSize = 13.sp,
          lineHeight = 20.sp,
          fontFamily = FontFamily.Serif
        )
      }
    }
  }
}

@Composable
private fun DossierChip(label: String, color: Color) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .background(color.copy(alpha = 0.12f))
      .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp)
  ) {
    Text(
      text = label,
      color = color,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}

@Composable
private fun ArchetypeSelectorSection(
  selectedArchetype: SkyfarerArchetype,
  onArchetypeSelected: (SkyfarerArchetype) -> Unit
) {
  Column {
    Text(
      text = "1. Choose Skyfarer Archetype",
      color = TextPrimaryNight,
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      SkyfarerArchetype.values().forEach { arch ->
        val isSelected = arch == selectedArchetype
        Card(
          modifier = Modifier
            .width(150.dp)
            .border(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) AmberLamp else NightCardBorder,
              shape = RoundedCornerShape(14.dp)
            )
            .clickable { onArchetypeSelected(arch) },
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AmberLamp.copy(alpha = 0.12f) else NightSurface
          ),
          shape = RoundedCornerShape(14.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = arch.title,
              color = if (isSelected) AmberLamp else TextPrimaryNight,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = arch.subtitle,
              color = TextSecondaryNight,
              fontSize = 10.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(NightSurfaceVariant)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "+${arch.primaryStatName}",
                color = BlueGlass,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun OriginIsleSelectorSection(
  selectedOrigin: OriginIsle,
  onOriginSelected: (OriginIsle) -> Unit
) {
  Column {
    Text(
      text = "2. Origin Isle & Homeland",
      color = TextPrimaryNight,
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OriginIsle.values().forEach { isle ->
        val isSelected = isle == selectedOrigin
        Card(
          modifier = Modifier
            .width(160.dp)
            .border(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) BlueGlass else NightCardBorder,
              shape = RoundedCornerShape(14.dp)
            )
            .clickable { onOriginSelected(isle) },
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BlueGlass.copy(alpha = 0.12f) else NightSurface
          ),
          shape = RoundedCornerShape(14.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = isle.isleName,
              color = if (isSelected) BlueGlass else TextPrimaryNight,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = isle.region,
              color = TextMutedNight,
              fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = isle.trait,
              color = TextSecondaryNight,
              fontSize = 10.sp,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}

@Composable
private fun LanternAndRigSection(
  character: SkyfarerCharacter,
  onLanternSelected: (LanternAffinity) -> Unit,
  onRigSelected: (CourierRig) -> Unit
) {
  Column {
    Text(
      text = "3. Lantern Flame & Glider Rig",
      color = TextPrimaryNight,
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(bottom = 8.dp)
    )

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = NightSurface),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        // Flame Affinity
        Text(
          text = "Lantern Flame Affinity",
          color = TextPrimaryNight,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = character.lanternAffinity.auraEffect,
          color = TextSecondaryNight,
          fontSize = 11.sp,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          LanternAffinity.values().forEach { affinity ->
            val isSelected = affinity == character.lanternAffinity
            val flameColor = Color(android.graphics.Color.parseColor(affinity.colorHex))

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) flameColor.copy(alpha = 0.2f) else NightSurfaceVariant)
                .border(
                  1.dp,
                  if (isSelected) flameColor else NightCardBorder,
                  RoundedCornerShape(10.dp)
                )
                .clickable { onLanternSelected(affinity) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(flameColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = affinity.flameName,
                  color = if (isSelected) flameColor else TextSecondaryNight,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = NightCardBorder)
        Spacer(modifier = Modifier.height(14.dp))

        // Glider Rig
        Text(
          text = "Equipped Courier Rig",
          color = TextPrimaryNight,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = character.gliderRig.subtitle,
          color = TextSecondaryNight,
          fontSize = 11.sp,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          CourierRig.values().forEach { rig ->
            val isSelected = rig == character.gliderRig
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) AmberLamp.copy(alpha = 0.15f) else NightSurfaceVariant)
                .border(
                  1.dp,
                  if (isSelected) AmberLamp else NightCardBorder,
                  RoundedCornerShape(10.dp)
                )
                .clickable { onRigSelected(rig) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Column {
                Text(
                  text = rig.displayName,
                  color = if (isSelected) AmberLamp else TextPrimaryNight,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = rig.defenseBonus,
                  color = BlueGlass,
                  fontSize = 9.sp
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
private fun CharacterStatsEditorSection(
  stats: CharacterStats,
  onStatsChanged: (CharacterStats) -> Unit,
  onResetArchetypeStats: () -> Unit
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "4. Skyfarer Attributes",
        color = TextPrimaryNight,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Total Points: ${stats.totalPoints}",
          color = AmberLamp,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
          onClick = onResetArchetypeStats,
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset Stats",
            tint = TextSecondaryNight,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = NightSurface),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier.padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatSliderRow(
          name = "Wind-Sense",
          desc = "Barometric awareness & squall reading",
          value = stats.windSense,
          onIncrement = { if (stats.windSense < 10) onStatsChanged(stats.copy(windSense = stats.windSense + 1)) },
          onDecrement = { if (stats.windSense > 1) onStatsChanged(stats.copy(windSense = stats.windSense - 1)) }
        )
        StatSliderRow(
          name = "Grit & Altitude",
          desc = "Endurance against sub-zero frost & sheer drops",
          value = stats.gritAltitude,
          onIncrement = { if (stats.gritAltitude < 10) onStatsChanged(stats.copy(gritAltitude = stats.gritAltitude + 1)) },
          onDecrement = { if (stats.gritAltitude > 1) onStatsChanged(stats.copy(gritAltitude = stats.gritAltitude - 1)) }
        )
        StatSliderRow(
          name = "Lantern Craft",
          desc = "Amber flame focusing & mist warding",
          value = stats.lanternCraft,
          onIncrement = { if (stats.lanternCraft < 10) onStatsChanged(stats.copy(lanternCraft = stats.lanternCraft + 1)) },
          onDecrement = { if (stats.lanternCraft > 1) onStatsChanged(stats.copy(lanternCraft = stats.lanternCraft - 1)) }
        )
        StatSliderRow(
          name = "Lore & Wisdom",
          desc = "Archipelago history & dragon glyph knowledge",
          value = stats.loreWisdom,
          onIncrement = { if (stats.loreWisdom < 10) onStatsChanged(stats.copy(loreWisdom = stats.loreWisdom + 1)) },
          onDecrement = { if (stats.loreWisdom > 1) onStatsChanged(stats.copy(loreWisdom = stats.loreWisdom - 1)) }
        )
        StatSliderRow(
          name = "Tiller Agility",
          desc = "Glider wing acrobatics & cable balance",
          value = stats.tillerAgility,
          onIncrement = { if (stats.tillerAgility < 10) onStatsChanged(stats.copy(tillerAgility = stats.tillerAgility + 1)) },
          onDecrement = { if (stats.tillerAgility > 1) onStatsChanged(stats.copy(tillerAgility = stats.tillerAgility - 1)) }
        )
      }
    }
  }
}

@Composable
private fun StatSliderRow(
  name: String,
  desc: String,
  value: Int,
  onIncrement: () -> Unit,
  onDecrement: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = name,
        color = TextPrimaryNight,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = desc,
        color = TextMutedNight,
        fontSize = 10.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      LinearProgressIndicator(
        progress = { value / 10f },
        modifier = Modifier
          .fillMaxWidth(0.9f)
          .height(4.dp)
          .clip(RoundedCornerShape(2.dp)),
        color = when {
          value >= 8 -> AmberLamp
          value >= 5 -> BlueGlass
          else -> TextMutedNight
        },
        trackColor = NightSurfaceVariant
      )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      IconButton(
        onClick = onDecrement,
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(NightSurfaceVariant)
      ) {
        Icon(
          imageVector = Icons.Default.Remove,
          contentDescription = "Decrease",
          tint = TextPrimaryNight,
          modifier = Modifier.size(14.dp)
        )
      }
      Text(
        text = "$value",
        color = AmberLamp,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 10.dp)
      )
      IconButton(
        onClick = onIncrement,
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(NightSurfaceVariant)
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Increase",
          tint = TextPrimaryNight,
          modifier = Modifier.size(14.dp)
        )
      }
    }
  }
}

@Composable
private fun IdentityAndLoreEditorSection(
  character: SkyfarerCharacter,
  onCharacterUpdated: (SkyfarerCharacter) -> Unit
) {
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "5. Identity & Lore Details",
      color = TextPrimaryNight,
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold
    )

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = NightSurface),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier.padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Name & Title Inputs
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = character.name,
            onValueChange = { onCharacterUpdated(character.copy(name = it)) },
            label = { Text("Character Name", fontSize = 11.sp) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = TextPrimaryNight,
              unfocusedTextColor = TextPrimaryNight,
              focusedBorderColor = AmberLamp,
              unfocusedBorderColor = NightCardBorder
            )
          )

          IconButton(
            onClick = {
              val newName = CharacterGeneratorEngine.rollRandomName()
              onCharacterUpdated(character.copy(name = newName))
            },
            modifier = Modifier
              .align(Alignment.CenterVertically)
              .size(40.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(NightSurfaceVariant)
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Reroll Name",
              tint = AmberLamp,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Relic
        LoreFieldRow(
          title = "Signature Relic",
          content = character.signatureRelic,
          onReroll = {
            val relics = listOf(
              "Carved Salt-Bone Flute that mimics high shear whistling",
              "Grandfather's Weighted Iron Grappling Hook with frayed sky-silk line",
              "Brass Pressure Gauge with etched markings from three censuses ago",
              "Jar of Living Phosphor Moon-Motes gathered from the Atoll",
              "Spool of Gilded Copper Signal Wire wrapped in tarred sailcloth",
              "Fractured Stormglass Prism that glows when lightning walks the cables",
              "Auditor's Tarnished Wax Seal salvaged from a sunken ledger-barge"
            )
            onCharacterUpdated(character.copy(signatureRelic = relics.random()))
          }
        )

        // Personality Quirk
        LoreFieldRow(
          title = "Skyfarer Quirk",
          content = character.personalityQuirk,
          onReroll = {
            val quirks = listOf(
              "Refuses to sleep indoors; always rigs a hammock beneath the skiff keel.",
              "Tastes the wind before every glider dive to gauge salt and barometric density.",
              "Carries a jar of rainwater from their childhood isle and never drinks from it.",
              "Whispers an apology to the air whenever deploying storm-brakes in high squalls.",
              "Counts the suspension chain links aloud when climbing through blinding fog.",
              "Never cuts their hair until an urgent courier delivery is successfully fulfilled."
            )
            onCharacterUpdated(character.copy(personalityQuirk = quirks.random()))
          }
        )

        // Motivation
        LoreFieldRow(
          title = "Driving Motivation",
          content = character.motivation,
          onReroll = {
            val motives = listOf(
              "Searching the Lower Salt abyss for the sunken storm-skiff of a lost sibling.",
              "Determined to deliver a sealed brass letter whose recipient vanished ten years ago.",
              "Working off an impossible iron-dock debt to reclaim their family's glider workshop.",
              "Plotting to chart an unmapped sky route across the Great Maw where no courier has returned.",
              "Protecting a young Moon-Koi whose luminescent memory holds the true history of the falls."
            )
            onCharacterUpdated(character.copy(motivation = motives.random()))
          }
        )
      }
    }
  }
}

@Composable
private fun LoreFieldRow(
  title: String,
  content: String,
  onReroll: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(NightSurfaceVariant.copy(alpha = 0.5f))
      .padding(horizontal = 10.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        color = AmberLamp,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = content,
        color = TextPrimaryNight,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )
    }
    IconButton(
      onClick = onReroll,
      modifier = Modifier.size(28.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = "Reroll $title",
        tint = TextSecondaryNight,
        modifier = Modifier.size(16.dp)
      )
    }
  }
}

@Composable
private fun RosterTabContent(
  viewModel: StoryViewModel,
  uiState: StoryUiState
) {
  val context = LocalContext.current
  val roster = uiState.savedRoster

  if (roster.isEmpty()) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = AmberLamp.copy(alpha = 0.6f),
          modifier = Modifier.size(48.dp)
        )
        Text(
          text = "No Skyfarers Saved Yet",
          color = TextPrimaryNight,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Switch to the Forge tab to conjure and customize characters for your courier roster.",
          color = TextSecondaryNight,
          fontSize = 13.sp,
          textAlign = TextAlign.Center
        )
        Button(
          onClick = { viewModel.setCharacterGeneratorTab(CharacterGeneratorTab.FORGE) },
          colors = ButtonDefaults.buttonColors(containerColor = AmberLamp, contentColor = VoidDark),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Go to Forge", fontWeight = FontWeight.Bold)
        }
      }
    }
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(roster, key = { it.id }) { item ->
        val isActive = uiState.activeCharacter?.id == item.id

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(
              width = if (isActive) 2.dp else 1.dp,
              color = if (isActive) BlueGlass else NightCardBorder,
              shape = RoundedCornerShape(16.dp)
            )
            .testTag("roster_item_${item.id}"),
          colors = CardDefaults.cardColors(containerColor = NightSurface),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = item.name,
                    color = TextPrimaryNight,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                  )
                  if (isActive) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(BlueGlass)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = "ACTIVE",
                        color = VoidDark,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                      )
                    }
                  }
                }
                Text(
                  text = "${item.title} • ${item.archetype.title}",
                  color = AmberLamp,
                  fontSize = 12.sp
                )
              }

              // Delete button
              IconButton(
                onClick = { viewModel.deleteCharacterFromRoster(item.id) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete Character",
                  tint = TextMutedNight,
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = item.motivation,
              color = TextSecondaryNight,
              fontSize = 12.sp,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
              fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Stats summary row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              RosterStatTag("Wind", item.stats.windSense)
              RosterStatTag("Grit", item.stats.gritAltitude)
              RosterStatTag("Lamp", item.stats.lanternCraft)
              RosterStatTag("Lore", item.stats.loreWisdom)
              RosterStatTag("Agil", item.stats.tillerAgility)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Equip Active
              if (!isActive) {
                Button(
                  onClick = { viewModel.setActiveCharacter(item) },
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = BlueGlass,
                    contentColor = VoidDark
                  )
                ) {
                  Text("Equip Active", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }

              // Edit / Load into Forge
              OutlinedButton(
                onClick = {
                  viewModel.updateDraftCharacter(item)
                  viewModel.setCharacterGeneratorTab(CharacterGeneratorTab.FORGE)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, AmberLamp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberLamp)
              ) {
                Text("Edit in Forge", fontSize = 11.sp)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun RosterStatTag(name: String, value: Int) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(NightSurfaceVariant)
      .padding(horizontal = 6.dp, vertical = 2.dp)
  ) {
    Text(
      text = "$name: $value",
      color = TextSecondaryNight,
      fontSize = 10.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}
