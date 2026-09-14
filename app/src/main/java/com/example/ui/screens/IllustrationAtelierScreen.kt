package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Palette
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.SceneIllustration
import com.example.ui.StoryUiState
import com.example.ui.StoryViewModel
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurface
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.TextMutedNight
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VoidDark
import java.io.File

@Composable
fun IllustrationAtelierScreen(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  modifier: Modifier = Modifier
) {
  var selectedFilter by remember { mutableStateOf(-1) } // -1 = All, 0..4 = Chapters, 99 = User Conjured

  val filteredIllustrations = remember(selectedFilter, uiState.illustrationGallery) {
    when (selectedFilter) {
      -1 -> uiState.illustrationGallery
      99 -> uiState.illustrationGallery.filter { it.isUserGenerated }
      else -> uiState.illustrationGallery.filter { it.chapterId == selectedFilter }
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
  ) {
    // Header Hero Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp))
          .testTag("atelier_hero_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NightSurface)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(38.dp)
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
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "ARCHIPELAGO ATELIER",
                  color = AmberLamp,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.2.sp
                )
                Text(
                  text = "Painterly Scene Plates",
                  color = TextPrimaryNight,
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Serif
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Atmospheric visual plates depicting key scenes across the floating isles. Use Imagen to conjure new painterly illustrations in watercolor, oil impasto, or amber chiaroscuro.",
            color = TextSecondaryNight,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = {
              viewModel.openIllustrationStudio()
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("atelier_new_illustration_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = AmberLamp,
              contentColor = VoidDark
            )
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Conjure New Scene with Imagen",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Filter Chips
    item {
      val filterOptions = listOf(
        Pair(-1, "All Plates (${uiState.illustrationGallery.size})"),
        Pair(99, "My Creations (${uiState.illustrationGallery.count { it.isUserGenerated }})"),
        Pair(0, "Chapter 1"),
        Pair(1, "Chapter 2"),
        Pair(2, "Chapter 3"),
        Pair(3, "Chapter 4"),
        Pair(4, "Chapter 5")
      )

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(filterOptions) { (id, label) ->
          val isSelected = selectedFilter == id
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) AmberLamp.copy(alpha = 0.2f) else NightSurface)
              .border(1.dp, if (isSelected) AmberLamp else NightCardBorder, RoundedCornerShape(8.dp))
              .clickable { selectedFilter = id }
              .padding(horizontal = 12.dp, vertical = 7.dp)
              .testTag("filter_$id")
          ) {
            Text(
              text = label,
              color = if (isSelected) AmberLamp else TextSecondaryNight,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Empty state
    if (filteredIllustrations.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
          colors = CardDefaults.cardColors(containerColor = NightSurface)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Palette,
              contentDescription = null,
              tint = TextMutedNight,
              modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "No scene plates in this category yet.",
              color = TextSecondaryNight,
              fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Tap 'Conjure New Scene with Imagen' above to create one.",
              color = TextMutedNight,
              fontSize = 11.sp
            )
          }
        }
      }
    } else {
      // List of Illustration Cards
      items(filteredIllustrations, key = { it.id }) { illustration ->
        IllustrationPlateCard(
          illustration = illustration,
          onClick = { viewModel.viewIllustrationDetail(illustration) }
        )
        Spacer(modifier = Modifier.height(14.dp))
      }
    }
  }
}

@Composable
private fun IllustrationPlateCard(
  illustration: SceneIllustration,
  onClick: () -> Unit
) {
  val localBitmap = remember(illustration.localFilePath) {
    illustration.localFilePath?.let { path ->
      val file = File(path)
      if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .border(1.dp, NightCardBorder, RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("plate_card_${illustration.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = NightSurface)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
      ) {
        if (localBitmap != null) {
          Image(
            bitmap = localBitmap.asImageBitmap(),
            contentDescription = illustration.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        } else if (illustration.drawableResId != null) {
          Image(
            painter = painterResource(id = illustration.drawableResId),
            contentDescription = illustration.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        } else {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(NightSurfaceVariant),
            contentAlignment = Alignment.Center
          ) {
            Text("Plate Preview", color = TextMutedNight)
          }
        }

        // Style Badge Overlay
        Box(
          modifier = Modifier
            .padding(10.dp)
            .align(Alignment.TopStart)
            .clip(RoundedCornerShape(6.dp))
            .background(VoidDark.copy(alpha = 0.75f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = illustration.style.displayName,
            color = AmberLamp,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }

        if (illustration.isUserGenerated) {
          Box(
            modifier = Modifier
              .padding(10.dp)
              .align(Alignment.TopEnd)
              .clip(RoundedCornerShape(6.dp))
              .background(AmberLamp.copy(alpha = 0.9f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = "IMAGEN 3",
              color = VoidDark,
              fontSize = 9.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = illustration.title,
            color = TextPrimaryNight,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.weight(1f)
          )
          Text(
            text = "Chapter ${illustration.chapterId + 1}",
            color = BlueGlass,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "\"${illustration.prompt}\"",
          color = TextSecondaryNight,
          fontSize = 11.sp,
          fontStyle = FontStyle.Italic,
          fontFamily = FontFamily.Serif,
          maxLines = 2,
          lineHeight = 15.sp
        )
      }
    }
  }
}
