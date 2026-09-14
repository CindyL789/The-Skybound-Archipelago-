package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Explore
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.StoryChoice
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurface
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VermilionCourier

@Composable
fun InteractiveChoiceCard(
  choice: StoryChoice,
  selectedOptionId: String?,
  onOptionSelected: (ChoiceOption) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp)
      .border(
        width = 1.dp,
        brush = Brush.horizontalGradient(listOf(VermilionCourier, AmberLamp, BlueGlass)),
        shape = RoundedCornerShape(16.dp)
      )
      .testTag("interactive_choice_card_${choice.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = DeepNight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(VermilionCourier.copy(alpha = 0.2f))
            .border(1.dp, VermilionCourier, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Explore,
            contentDescription = null,
            tint = VermilionCourier,
            modifier = Modifier.size(16.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
          text = "COURIER'S CHOICE",
          color = VermilionCourier,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.5.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = choice.prompt,
        color = TextPrimaryNight,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Serif,
        lineHeight = 22.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      choice.options.forEach { option ->
        val isChosen = selectedOptionId == option.id

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isChosen) BlueGlass.copy(alpha = 0.15f) else NightSurfaceVariant.copy(alpha = 0.5f))
            .border(
              width = 1.dp,
              color = if (isChosen) BlueGlass else NightCardBorder,
              shape = RoundedCornerShape(12.dp)
            )
            .clickable { onOptionSelected(option) }
            .padding(14.dp)
            .testTag("option_${option.id}")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (isChosen) BlueGlass else NightSurface)
                .border(1.dp, if (isChosen) BlueGlass else Color(0xFF657B96), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              if (isChosen) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = DeepNight,
                  modifier = Modifier.size(12.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
              text = option.text,
              color = if (isChosen) BlueGlass else TextPrimaryNight,
              fontSize = 14.sp,
              fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
              modifier = Modifier.weight(1f)
            )
          }

          AnimatedVisibility(
            visible = isChosen,
            enter = fadeIn() + expandVertically()
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 30.dp)
            ) {
              Text(
                text = option.consequenceText,
                color = AmberLamp,
                fontSize = 13.sp,
                fontFamily = FontFamily.Serif,
                lineHeight = 18.sp
              )

              if (option.reputationTag != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Reputation Acquired: ",
                    color = TextSecondaryNight,
                    fontSize = 11.sp
                  )
                  Text(
                    text = option.reputationTag,
                    color = BlueGlass,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
