package com.example.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IllustrationDetailDialog(
  illustration: SceneIllustration,
  viewModel: StoryViewModel,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val scrollState = rememberScrollState()
  val context = LocalContext.current

  val localBitmap = remember(illustration.localFilePath) {
    illustration.localFilePath?.let { path ->
      val file = File(path)
      if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = DeepNight,
    dragHandle = null,
    modifier = Modifier.testTag("illustration_detail_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .verticalScroll(scrollState)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = if (illustration.isUserGenerated) "USER CONJURED SCENE" else "ARCHIVE STORY PLATE",
            color = if (illustration.isUserGenerated) AmberLamp else BlueGlass,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Text(
            text = illustration.title,
            color = TextPrimaryNight,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
          )
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("close_detail_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close Detail",
            tint = TextSecondaryNight
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // The Picture Canvas
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, NightCardBorder, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NightSurface)
      ) {
        if (localBitmap != null) {
          Image(
            bitmap = localBitmap.asImageBitmap(),
            contentDescription = illustration.title,
            modifier = Modifier
              .fillMaxWidth()
              .height(260.dp)
              .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
          )
        } else if (illustration.drawableResId != null) {
          Image(
            painter = painterResource(id = illustration.drawableResId),
            contentDescription = illustration.title,
            modifier = Modifier
              .fillMaxWidth()
              .height(260.dp)
              .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
          )
        } else {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp),
            contentAlignment = Alignment.Center
          ) {
            Text("Plate preview unavailable", color = TextMutedNight, fontSize = 13.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Style & Metadata Chips
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AmberLamp.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = illustration.style.displayName,
            color = AmberLamp,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(NightSurfaceVariant)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "Chapter ${illustration.chapterId + 1}",
            color = BlueGlass,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Prompt Card
      Text(
        text = "PROMPT INVOCATION",
        color = TextMutedNight,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(6.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NightSurface)
      ) {
        Text(
          text = "\"${illustration.prompt}\"",
          color = TextSecondaryNight,
          fontSize = 12.sp,
          fontStyle = FontStyle.Italic,
          fontFamily = FontFamily.Serif,
          lineHeight = 17.sp,
          modifier = Modifier.padding(12.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = {
            val bmp = localBitmap ?: illustration.drawableResId?.let { resId ->
              BitmapFactory.decodeResource(context.resources, resId)
            }
            viewModel.openVeoStudio(
              sourceBitmap = bmp,
              title = "${illustration.title} Motion",
              initialPrompt = "Cinematic drifting clouds and atmospheric lighting across ${illustration.prompt}, 24fps",
              aspectRatio = "16:9"
            )
            onDismiss()
          },
          modifier = Modifier
            .weight(1f)
            .testTag("animate_with_veo_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = BlueGlass,
            contentColor = VoidDark
          )
        ) {
          Icon(
            imageVector = Icons.Default.Videocam,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("Animate with Veo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = {
            viewModel.openIllustrationStudio(
              sceneTitle = "${illustration.title} (Variation)",
              prompt = illustration.prompt,
              style = illustration.style
            )
            onDismiss()
          },
          modifier = Modifier
            .weight(1f)
            .testTag("remix_illustration_button"),
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
          Spacer(modifier = Modifier.width(6.dp))
          Text("Variation", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        if (illustration.isUserGenerated) {
          OutlinedButton(
            onClick = {
              viewModel.deleteUserIllustration(illustration.id)
              onDismiss()
            },
            modifier = Modifier.testTag("delete_illustration_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = VermilionCourier),
            border = androidx.compose.foundation.BorderStroke(1.dp, VermilionCourier.copy(alpha = 0.5f))
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete",
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
