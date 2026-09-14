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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocationNode
import com.example.data.StoryRepository
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
fun ArchipelagoMapScreen(
  viewModel: StoryViewModel,
  modifier: Modifier = Modifier
) {
  val locations = StoryRepository.locationNodes
  var selectedLocation by remember { mutableStateOf(locations.first()) }

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
          text = "THE SKYBOUND ARCHIPELAGO",
          color = VermilionCourier,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.5.sp
        )
        Text(
          text = "Cartography of Hanging Promises",
          color = TextPrimaryNight,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif
        )
      }

      Icon(
        imageVector = Icons.Default.Map,
        contentDescription = "Map",
        tint = BlueGlass,
        modifier = Modifier.size(28.dp)
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Featured Selected Location Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(
          width = 1.dp,
          brush = Brush.horizontalGradient(listOf(BlueGlass, AmberLamp)),
          shape = RoundedCornerShape(16.dp)
        )
        .testTag("selected_location_card"),
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
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = selectedLocation.name,
              color = BlueGlass,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = selectedLocation.subtitle,
              color = AmberLamp,
              fontSize = 12.sp,
              fontStyle = FontStyle.Italic
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(NightSurfaceVariant)
              .border(1.dp, NightCardBorder, RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = selectedLocation.lanternStatus,
              color = TextSecondaryNight,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = selectedLocation.description,
          color = TextPrimaryNight,
          fontSize = 13.sp,
          fontFamily = FontFamily.Serif,
          lineHeight = 19.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            viewModel.selectChapter(selectedLocation.chapterId)
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = BlueGlass,
            contentColor = VoidDark
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("jump_to_chapter_button")
        ) {
          Icon(
            imageVector = Icons.Default.AutoStories,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Read Chapter ${selectedLocation.chapterId}",
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    Text(
      text = "SURVEYED DISTRICTS & PLATFORMS",
      color = TextMutedNight,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    // List of Map Nodes
    LazyColumn(
      modifier = Modifier.fillMaxWidth().weight(1f),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(locations) { loc ->
        val isSelected = loc.id == selectedLocation.id

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) BlueGlass.copy(alpha = 0.12f) else NightSurface)
            .border(
              width = 1.dp,
              color = if (isSelected) BlueGlass else NightCardBorder,
              shape = RoundedCornerShape(12.dp)
            )
            .clickable { selectedLocation = loc }
            .padding(14.dp)
            .testTag("location_node_${loc.id}"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (isSelected) BlueGlass else NightSurfaceVariant)
              .border(1.dp, if (isSelected) BlueGlass else NightCardBorder, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = if (isSelected) VoidDark else BlueGlass,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = loc.name,
              color = if (isSelected) BlueGlass else TextPrimaryNight,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = loc.lanternStatus,
              color = if (isSelected) AmberLamp else TextSecondaryNight,
              fontSize = 11.sp
            )
          }

          Text(
            text = "Ch. ${loc.chapterId}",
            color = TextMutedNight,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}
