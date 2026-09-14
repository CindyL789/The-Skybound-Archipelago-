package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnnotatedTerm
import com.example.data.CharacterProfile
import com.example.data.StoryRepository
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

enum class CodexTab(val label: String) {
  PERSONAE("Cast & Allies"),
  GLOSSARY("Archipelago Lore")
}

@Composable
fun CodexScreen(
  modifier: Modifier = Modifier
) {
  var activeTab by remember { mutableStateOf(CodexTab.PERSONAE) }
  var selectedTerm by remember { mutableStateOf<AnnotatedTerm?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(VoidDark)
      .padding(horizontal = 20.dp, vertical = 16.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Text(
          text = "CODEX & CHRONICLES",
          color = VermilionCourier,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.5.sp
        )
        Text(
          text = "The Skybound Ledger",
          color = TextPrimaryNight,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif
        )
      }

      Icon(
        imageVector = Icons.Default.MenuBook,
        contentDescription = "Codex",
        tint = BlueGlass,
        modifier = Modifier.size(28.dp)
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Segmented Tab Switcher
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(NightSurface)
        .border(1.dp, NightCardBorder, RoundedCornerShape(12.dp))
        .padding(4.dp)
    ) {
      CodexTab.values().forEach { tab ->
        val isSelected = activeTab == tab
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) BlueGlass else Color.Transparent)
            .clickable { activeTab = tab }
            .padding(vertical = 8.dp)
            .testTag("codex_tab_${tab.name.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = tab.label,
            color = if (isSelected) VoidDark else TextSecondaryNight,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    when (activeTab) {
      CodexTab.PERSONAE -> {
        LazyColumn(
          modifier = Modifier.fillMaxWidth().weight(1f),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(StoryRepository.characters) { char ->
            CharacterCard(char)
          }
        }
      }
      CodexTab.GLOSSARY -> {
        LazyColumn(
          modifier = Modifier.fillMaxWidth().weight(1f),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(StoryRepository.glossaryTerms) { term ->
            GlossaryCard(term)
          }
        }
      }
    }
  }
}

@Composable
private fun CharacterCard(char: CharacterProfile) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, NightCardBorder, RoundedCornerShape(14.dp))
      .testTag("char_card_${char.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = DeepNight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = char.name,
            color = BlueGlass,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = char.title,
            color = AmberLamp,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(VermilionCourier.copy(alpha = 0.2f))
            .border(1.dp, VermilionCourier, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = char.role.uppercase(),
            color = VermilionCourier,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = char.description,
        color = TextSecondaryNight,
        fontSize = 12.sp,
        fontFamily = FontFamily.Serif,
        lineHeight = 17.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(NightSurfaceVariant.copy(alpha = 0.5f))
          .padding(10.dp)
      ) {
        Text(
          text = "\"${char.quote}\"",
          color = TextPrimaryNight,
          fontSize = 11.sp,
          fontStyle = FontStyle.Italic,
          fontFamily = FontFamily.Serif
        )
      }
    }
  }
}

@Composable
private fun GlossaryCard(term: AnnotatedTerm) {
  val badgeColor = when (term.category) {
    "Creature" -> BlueGlass
    "Relic" -> AmberLamp
    "Faction" -> VermilionCourier
    "Geography" -> Color(0xFF48CAE4)
    else -> Color(0xFFC77DFF)
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, NightCardBorder, RoundedCornerShape(14.dp))
      .testTag("glossary_card_${term.term.lowercase().replace(" ", "_")}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = DeepNight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = term.term,
          color = TextPrimaryNight,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(badgeColor.copy(alpha = 0.2f))
            .border(1.dp, badgeColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = term.category.uppercase(),
            color = badgeColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = term.summary,
        color = AmberLamp,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = term.detail,
        color = TextSecondaryNight,
        fontSize = 11.sp,
        fontFamily = FontFamily.Serif,
        lineHeight = 16.sp
      )
    }
  }
}
