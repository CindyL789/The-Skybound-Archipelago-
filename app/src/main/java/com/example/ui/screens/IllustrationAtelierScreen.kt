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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ai.VeoVideo
import com.example.ui.theme.VermilionCourier
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
  val context = LocalContext.current
  var selectedFilter by remember { mutableStateOf(-1) } // -1 = All, -2 = Veo Videos, 0..4 = Chapters, 99 = User Conjured

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
                  text = "Plates & Veo Cinema",
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
            text = "Atmospheric visual plates and cinematic AI video across the floating isles. Conjure scenes with Imagen 3 or animate any photo with Veo 3.1 Fast video.",
            color = TextSecondaryNight,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                viewModel.openIllustrationStudio()
              },
              modifier = Modifier
                .weight(1f)
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
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Imagen Scene",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Button(
              onClick = {
                val firstPlate = uiState.illustrationGallery.firstOrNull()
                val bmp = firstPlate?.let { plate ->
                  if (plate.localFilePath != null) BitmapFactory.decodeFile(plate.localFilePath)
                  else if (plate.drawableResId != null) BitmapFactory.decodeResource(context.resources, plate.drawableResId)
                  else null
                }
                viewModel.openVeoStudio(
                  sourceBitmap = bmp,
                  title = "${firstPlate?.title ?: "Archipelago"} Motion",
                  initialPrompt = "Cinematic drifting clouds and atmospheric light across floating isles, 24fps",
                  aspectRatio = "16:9"
                )
              },
              modifier = Modifier
                .weight(1f)
                .testTag("atelier_new_veo_button"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = BlueGlass,
                contentColor = VoidDark
              )
            ) {
              Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = null,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Veo Video",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Filter Chips
    item {
      val filterOptions = listOf(
        Pair(-1, "All Plates (${uiState.illustrationGallery.size})"),
        Pair(-2, "🎬 Veo Videos (${uiState.veoVideos.size})"),
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

    // Items section
    if (selectedFilter == -2) {
      if (uiState.veoVideos.isEmpty()) {
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
                imageVector = Icons.Default.Movie,
                contentDescription = null,
                tint = BlueGlass,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "No Veo Isle Cinema videos generated yet.",
                color = TextSecondaryNight,
                fontSize = 13.sp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Tap 'Veo Video' above to animate any photo with veo-3.1-fast-generate-preview.",
                color = TextMutedNight,
                fontSize = 11.sp
              )
            }
          }
        }
      } else {
        items(uiState.veoVideos, key = { it.id }) { video ->
          VeoVideoCard(
            video = video,
            onClick = { viewModel.setActiveViewingVideo(video) },
            onDelete = { viewModel.deleteVeoVideo(video.id) }
          )
          Spacer(modifier = Modifier.height(14.dp))
        }
      }
    } else if (filteredIllustrations.isEmpty()) {
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
              text = "Tap 'Imagen Scene' above to conjure one.",
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
          onClick = { viewModel.viewIllustrationDetail(illustration) },
          onAnimateVeo = {
            val bmp = if (illustration.localFilePath != null) BitmapFactory.decodeFile(illustration.localFilePath)
            else if (illustration.drawableResId != null) BitmapFactory.decodeResource(context.resources, illustration.drawableResId)
            else null

            viewModel.openVeoStudio(
              sourceBitmap = bmp,
              title = "${illustration.title} Motion",
              initialPrompt = "Cinematic drifting clouds and atmospheric lighting across ${illustration.prompt}, 24fps",
              aspectRatio = "16:9"
            )
          }
        )
        Spacer(modifier = Modifier.height(14.dp))
      }
    }
  }
}

@Composable
private fun VeoVideoCard(
  video: VeoVideo,
  onClick: () -> Unit,
  onDelete: () -> Unit
) {
  val ratioFloat = if (video.aspectRatio == "9:16") 9f / 16f else 16f / 9f

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .border(1.dp, NightCardBorder, RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("veo_video_card_${video.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = NightSurface)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(ratioFloat)
          .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
          .background(VoidDark),
        contentAlignment = Alignment.Center
      ) {
        val file = File(video.videoFilePath)
        if (file.exists() && file.length() > 0) {
          AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
              android.widget.VideoView(ctx).apply {
                setVideoPath(file.absolutePath)
                setOnPreparedListener { mp ->
                  mp.isLooping = true
                  start()
                }
              }
            }
          )
        } else {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Movie,
              contentDescription = null,
              tint = TextMutedNight,
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("Veo Video", color = TextSecondaryNight, fontSize = 11.sp)
          }
        }

        // Overlay Badges
        Box(
          modifier = Modifier
            .padding(10.dp)
            .align(Alignment.TopStart)
            .clip(RoundedCornerShape(6.dp))
            .background(VoidDark.copy(alpha = 0.8f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "VEO 3.1 • ${video.aspectRatio}",
            color = BlueGlass,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Box(
          modifier = Modifier
            .padding(10.dp)
            .align(Alignment.BottomEnd)
            .clip(CircleShape)
            .background(BlueGlass.copy(alpha = 0.85f))
            .padding(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Watch",
            tint = VoidDark,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = video.title,
            color = TextPrimaryNight,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.weight(1f)
          )

          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete Video",
              tint = VermilionCourier,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "\"${video.prompt}\"",
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

@Composable
private fun IllustrationPlateCard(
  illustration: SceneIllustration,
  onClick: () -> Unit,
  onAnimateVeo: () -> Unit
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

        // Quick Animate with Veo badge/button
        Box(
          modifier = Modifier
            .padding(10.dp)
            .align(Alignment.BottomEnd)
            .clip(RoundedCornerShape(6.dp))
            .background(VoidDark.copy(alpha = 0.85f))
            .clickable { onAnimateVeo() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Videocam,
              contentDescription = null,
              tint = BlueGlass,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Veo 3.1",
              color = BlueGlass,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
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
