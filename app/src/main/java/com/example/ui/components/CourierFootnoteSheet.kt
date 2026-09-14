package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.data.AnnotatedTerm
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VermilionCourier

@Composable
fun CourierFootnoteSheet(
  term: AnnotatedTerm,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val badgeColor = when (term.category) {
    "Creature" -> BlueGlass
    "Relic" -> AmberLamp
    "Faction" -> VermilionCourier
    "Geography" -> Color(0xFF48CAE4)
    else -> Color(0xFFC77DFF)
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp))
      .testTag("footnote_sheet_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = DeepNight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
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
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
          text = term.term,
          color = TextPrimaryNight,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.weight(1f)
        )

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("footnote_close_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close footnote",
            tint = TextSecondaryNight
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = term.summary,
        color = AmberLamp,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(NightSurfaceVariant.copy(alpha = 0.6f))
          .padding(12.dp)
      ) {
        Text(
          text = term.detail,
          color = TextSecondaryNight,
          fontSize = 13.sp,
          fontFamily = FontFamily.Serif,
          lineHeight = 19.sp
        )
      }
    }
  }
}
