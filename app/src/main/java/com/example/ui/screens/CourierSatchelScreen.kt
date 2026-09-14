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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.CourierRig
import com.example.data.StoryRepository
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

@Composable
fun CourierSatchelScreen(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  modifier: Modifier = Modifier
) {
  val allItems = StoryRepository.allItems

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(VoidDark)
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "COURIER KIT & DISGUISES",
            color = VermilionCourier,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
          )
          Text(
            text = "Sera Venn's Satchel",
            color = TextPrimaryNight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
          )
        }

        Icon(
          imageVector = Icons.Default.Work,
          contentDescription = "Satchel",
          tint = AmberLamp,
          modifier = Modifier.size(28.dp)
        )
      }
    }

    // Active Rig Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(
            width = 1.dp,
            brush = Brush.horizontalGradient(listOf(VermilionCourier, AmberLamp)),
            shape = RoundedCornerShape(16.dp)
          )
          .testTag("active_rig_card"),
        shape = RoundedCornerShape(16.dp),
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
            Column {
              Text(
                text = "EQUIPPED ATTIRE",
                color = VermilionCourier,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
              )
              Text(
                text = uiState.activeRig.displayName,
                color = BlueGlass,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(BlueGlass.copy(alpha = 0.2f))
                .border(1.dp, BlueGlass, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = uiState.activeRig.defenseBonus,
                color = BlueGlass,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = uiState.activeRig.description,
            color = TextSecondaryNight,
            fontSize = 12.sp,
            fontFamily = FontFamily.Serif,
            lineHeight = 17.sp
          )
        }
      }
    }

    // Locker Rig Selector
    item {
      Text(
        text = "NET-MENDER'S CHANGING LOCKER",
        color = TextMutedNight,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
    }

    items(CourierRig.values()) { rig ->
      val isEquipped = uiState.activeRig == rig

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(if (isEquipped) BlueGlass.copy(alpha = 0.12f) else NightSurface)
          .border(
            width = 1.dp,
            color = if (isEquipped) BlueGlass else NightCardBorder,
            shape = RoundedCornerShape(12.dp)
          )
          .clickable { viewModel.setCourierRig(rig) }
          .padding(14.dp)
          .testTag("rig_option_${rig.name.lowercase()}"),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (isEquipped) BlueGlass else NightSurfaceVariant)
            .border(1.dp, if (isEquipped) BlueGlass else NightCardBorder, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          if (isEquipped) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = VoidDark,
              modifier = Modifier.size(14.dp)
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = rig.displayName,
            color = if (isEquipped) BlueGlass else TextPrimaryNight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = rig.subtitle,
            color = if (isEquipped) AmberLamp else TextSecondaryNight,
            fontSize = 11.sp,
            fontStyle = FontStyle.Italic
          )
        }

        Text(
          text = if (isEquipped) "EQUIPPED" else "EQUIP",
          color = if (isEquipped) BlueGlass else TextMutedNight,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // Collected Relics & Documents
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "COLLECTED RELICS & DOCUMENTS",
        color = TextMutedNight,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
    }

    items(allItems) { item ->
      val isUnlocked = uiState.collectedItemIds.contains(item.id)

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(if (isUnlocked) NightSurface else NightSurface.copy(alpha = 0.4f))
          .border(
            width = 1.dp,
            color = if (isUnlocked) NightCardBorder else Color.Transparent,
            shape = RoundedCornerShape(12.dp)
          )
          .padding(14.dp)
          .testTag("item_${item.id}"),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
          contentDescription = null,
          tint = if (isUnlocked) AmberLamp else TextMutedNight,
          modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.name,
            color = if (isUnlocked) TextPrimaryNight else TextMutedNight,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = if (isUnlocked) item.description else "Discovered through story decisions.",
            color = if (isUnlocked) TextSecondaryNight else TextMutedNight,
            fontSize = 11.sp,
            fontFamily = FontFamily.Serif
          )
        }
      }
    }
  }
}
