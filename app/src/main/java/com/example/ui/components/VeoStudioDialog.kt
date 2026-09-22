package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.VeoPresetsCatalog
import com.example.ai.VeoVideo
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
import java.io.File

@Composable
fun VeoStudioDialog(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var promptText by remember {
    mutableStateOf(
      "Cinematic drifting clouds with volumetric sunlight shafts across floating archipelago stone spires, 24fps smooth camera motion."
    )
  }
  var videoTitle by remember { mutableStateOf(uiState.selectedVeoSourceTitle.ifBlank { "Archipelago Motion" }) }
  var showPlatePicker by remember { mutableStateOf(false) }

  // Photo Picker launcher compliant with Google Play zero-permission policy
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    uri?.let {
      try {
        context.contentResolver.openInputStream(it)?.use { stream ->
          val bitmap = BitmapFactory.decodeStream(stream)
          if (bitmap != null) {
            viewModel.setVeoSourceBitmap(bitmap, "Uploaded Photo")
            videoTitle = "Uploaded Photo Video"
          }
        }
      } catch (e: Exception) {
        // Handle gracefully
      }
    }
  }

  Dialog(
    onDismissRequest = {
      if (!uiState.isGeneratingVeoVideo) onDismiss()
    },
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .padding(vertical = 20.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, NightCardBorder, RoundedCornerShape(20.dp)),
      color = DeepNight
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Top Header
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
                .background(BlueGlass.copy(alpha = 0.2f))
                .border(1.dp, BlueGlass, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = null,
                tint = BlueGlass,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "VEO CINEMA STUDIO",
                color = BlueGlass,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp
              )
              Text(
                text = "Animate Photo to Video",
                color = TextPrimaryNight,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            enabled = !uiState.isGeneratingVeoVideo,
            modifier = Modifier.testTag("close_veo_studio_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextSecondaryNight
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Model: veo-3.1-fast-generate-preview. Upload any photo from your device or select an existing plate to generate cinematic atmospheric video in 16:9 or 9:16.",
          color = TextSecondaryNight,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Source Photo Selector Section
        Text(
          text = "1. SOURCE PHOTO",
          color = BlueGlass,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NightCardBorder, RoundedCornerShape(12.dp)),
          colors = CardDefaults.cardColors(containerColor = NightSurface),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            val sourceBitmap = uiState.selectedVeoSourceBitmap
            if (sourceBitmap != null) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(160.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(VoidDark),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  bitmap = sourceBitmap.asImageBitmap(),
                  contentDescription = "Source Photo",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )

                // Label overlay
                Box(
                  modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(VoidDark.copy(alpha = 0.75f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = uiState.selectedVeoSourceTitle.ifBlank { "Selected Photo" },
                    color = TextPrimaryNight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            } else {
              // Placeholder when no photo is selected yet
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(120.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(NightSurfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = TextMutedNight,
                    modifier = Modifier.size(36.dp)
                  )
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "No photo selected yet",
                    color = TextSecondaryNight,
                    fontSize = 12.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Upload Photo Button
              Button(
                onClick = {
                  photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                  )
                },
                modifier = Modifier
                  .weight(1f)
                  .testTag("veo_upload_photo_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = BlueGlass,
                  contentColor = VoidDark
                )
              ) {
                Icon(
                  imageVector = Icons.Default.AddPhotoAlternate,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Upload Photo",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              // Pick from Plates Button
              OutlinedButton(
                onClick = { showPlatePicker = !showPlatePicker },
                modifier = Modifier
                  .weight(1f)
                  .testTag("veo_pick_plate_button"),
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                  brush = androidx.compose.ui.graphics.SolidColor(NightCardBorder)
                )
              ) {
                Icon(
                  imageVector = Icons.Default.Collections,
                  contentDescription = null,
                  tint = TextPrimaryNight,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "From Plates",
                  color = TextPrimaryNight,
                  fontSize = 12.sp
                )
              }
            }

            // Expandable plate picker
            if (showPlatePicker && uiState.illustrationGallery.isNotEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Choose from Atelier Plates:",
                color = TextSecondaryNight,
                fontSize = 11.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                items(uiState.illustrationGallery) { plate ->
                  val bmp = remember(plate.id) {
                    if (plate.localFilePath != null) {
                      BitmapFactory.decodeFile(plate.localFilePath)
                    } else if (plate.drawableResId != null) {
                      BitmapFactory.decodeResource(context.resources, plate.drawableResId)
                    } else null
                  }

                  Card(
                    modifier = Modifier
                      .size(width = 110.dp, height = 75.dp)
                      .clip(RoundedCornerShape(6.dp))
                      .clickable {
                        if (bmp != null) {
                          viewModel.setVeoSourceBitmap(bmp, plate.title)
                          videoTitle = "${plate.title} Cinema"
                          showPlatePicker = false
                        }
                      },
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                      if (bmp != null) {
                        Image(
                          bitmap = bmp.asImageBitmap(),
                          contentDescription = plate.title,
                          modifier = Modifier.fillMaxSize(),
                          contentScale = ContentScale.Crop
                        )
                      }
                      Box(
                        modifier = Modifier
                          .align(Alignment.BottomCenter)
                          .fillMaxWidth()
                          .background(VoidDark.copy(alpha = 0.75f))
                          .padding(2.dp),
                        contentAlignment = Alignment.Center
                      ) {
                        Text(
                          text = plate.title,
                          color = TextPrimaryNight,
                          fontSize = 9.sp,
                          maxLines = 1
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aspect Ratio Selector (Strictly 16:9 and 9:16)
        Text(
          text = "2. ASPECT RATIO",
          color = BlueGlass,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // 16:9 Landscape
          val isLandscape = uiState.selectedVeoAspectRatio == "16:9"
          Card(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .border(
                1.5.dp,
                if (isLandscape) BlueGlass else NightCardBorder,
                RoundedCornerShape(10.dp)
              )
              .clickable { viewModel.setVeoAspectRatio("16:9") }
              .testTag("veo_aspect_ratio_16_9"),
            colors = CardDefaults.cardColors(
              containerColor = if (isLandscape) BlueGlass.copy(alpha = 0.15f) else NightSurface
            )
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "16:9",
                color = if (isLandscape) BlueGlass else TextPrimaryNight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Landscape Cinema",
                color = TextSecondaryNight,
                fontSize = 11.sp
              )
            }
          }

          // 9:16 Portrait
          val isPortrait = uiState.selectedVeoAspectRatio == "9:16"
          Card(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .border(
                1.5.dp,
                if (isPortrait) BlueGlass else NightCardBorder,
                RoundedCornerShape(10.dp)
              )
              .clickable { viewModel.setVeoAspectRatio("9:16") }
              .testTag("veo_aspect_ratio_9_16"),
            colors = CardDefaults.cardColors(
              containerColor = if (isPortrait) BlueGlass.copy(alpha = 0.15f) else NightSurface
            )
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "9:16",
                color = if (isPortrait) BlueGlass else TextPrimaryNight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Portrait Reel",
                color = TextSecondaryNight,
                fontSize = 11.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Motion Presets
        Text(
          text = "3. MOTION DIRECTION & PROMPT",
          color = BlueGlass,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(VeoPresetsCatalog.presets) { preset ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(NightSurface)
                .border(1.dp, NightCardBorder, RoundedCornerShape(8.dp))
                .clickable { promptText = preset.prompt }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = preset.icon, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = preset.title,
                  color = TextPrimaryNight,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = promptText,
          onValueChange = { promptText = it },
          label = { Text("Camera & Motion Prompt", color = TextSecondaryNight, fontSize = 12.sp) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("veo_prompt_input"),
          maxLines = 4,
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimaryNight,
            unfocusedTextColor = TextPrimaryNight,
            focusedBorderColor = BlueGlass,
            unfocusedBorderColor = NightCardBorder,
            focusedContainerColor = NightSurface,
            unfocusedContainerColor = NightSurface
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = videoTitle,
          onValueChange = { videoTitle = it },
          label = { Text("Video Title", color = TextSecondaryNight, fontSize = 12.sp) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("veo_title_input"),
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimaryNight,
            unfocusedTextColor = TextPrimaryNight,
            focusedBorderColor = BlueGlass,
            unfocusedBorderColor = NightCardBorder,
            focusedContainerColor = NightSurface,
            unfocusedContainerColor = NightSurface
          )
        )

        if (uiState.veoVideoGenerationError != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = uiState.veoVideoGenerationError,
            color = VermilionCourier,
            fontSize = 12.sp
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Generate Button
        Button(
          onClick = {
            viewModel.generateVeoVideo(
              prompt = promptText,
              aspectRatio = uiState.selectedVeoAspectRatio,
              title = videoTitle
            )
          },
          enabled = !uiState.isGeneratingVeoVideo && (uiState.selectedVeoSourceBitmap != null || uiState.illustrationGallery.isNotEmpty()),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("veo_generate_submit_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = BlueGlass,
            contentColor = VoidDark
          )
        ) {
          if (uiState.isGeneratingVeoVideo) {
            CircularProgressIndicator(
              modifier = Modifier.size(20.dp),
              color = VoidDark,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Synthesizing Veo Video (${uiState.selectedVeoAspectRatio})...",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          } else {
            Icon(
              imageVector = Icons.Default.Movie,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Generate Video (${uiState.selectedVeoAspectRatio})",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun VeoVideoPlayerDialog(
  video: VeoVideo,
  onDismiss: () -> Unit,
  onDelete: () -> Unit
) {
  val ratioFloat = if (video.aspectRatio == "9:16") 9f / 16f else 16f / 9f

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp)),
      color = DeepNight
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(BlueGlass.copy(alpha = 0.2f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "veo-3.1-fast • ${video.aspectRatio}",
                  color = BlueGlass,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = video.title,
              color = TextPrimaryNight,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Serif
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextSecondaryNight
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Video View Container
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratioFloat)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black),
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
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Video rendering...",
                color = TextSecondaryNight,
                fontSize = 12.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = video.prompt,
          color = TextSecondaryNight,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        if (video.note != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = video.note,
            color = BlueGlass,
            fontSize = 11.sp
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onDelete,
            modifier = Modifier.testTag("delete_veo_video_button")
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete Video",
              tint = VermilionCourier
            )
          }

          Button(
            onClick = onDismiss,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = BlueGlass,
              contentColor = VoidDark
            )
          ) {
            Text("Done", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
