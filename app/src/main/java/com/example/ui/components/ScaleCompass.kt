package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NightSurface
import com.example.ui.theme.VermilionCourier
import kotlin.math.atan2

@Composable
fun ScaleCompass(
  modifier: Modifier = Modifier,
  size: Dp = 64.dp,
  onClick: (() -> Unit)? = null
) {
  var manualAngle by remember { mutableFloatStateOf(0f) }
  val infiniteTransition = rememberInfiniteTransition(label = "compass_quiver")
  val quiver by infiniteTransition.animateFloat(
    initialValue = -3.5f,
    targetValue = 3.5f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "quiver"
  )

  val totalRotation = manualAngle + quiver

  Box(
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(NightSurface)
      .border(
        width = 2.dp,
        brush = Brush.sweepGradient(
          listOf(
            AmberLamp,
            BlueGlass,
            Color(0xFF8A99AD),
            AmberLamp
          )
        ),
        shape = CircleShape
      )
      .pointerInput(Unit) {
        detectDragGestures { change, _ ->
          val center = Offset(size.toPx() / 2, size.toPx() / 2)
          val delta = change.position - center
          manualAngle = (Math.toDegrees(atan2(delta.y.toDouble(), delta.x.toDouble())).toFloat() + 90f)
        }
      }
      .clickable(enabled = onClick != null) { onClick?.invoke() }
      .testTag("scale_compass_widget"),
    contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier
        .size(size * 0.88f)
        .rotate(totalRotation)
    ) {
      val w = this.size.width
      val h = this.size.height
      val cx = w / 2f
      val cy = h / 2f

      // Compass Rim Tick Marks (No North, only celestial degrees)
      for (i in 0 until 12) {
        val angleRad = Math.toRadians((i * 30).toDouble())
        val startR = if (i % 3 == 0) w * 0.38f else w * 0.42f
        val endR = w * 0.46f
        val x1 = cx + (startR * kotlin.math.cos(angleRad)).toFloat()
        val y1 = cy + (startR * kotlin.math.sin(angleRad)).toFloat()
        val x2 = cx + (endR * kotlin.math.cos(angleRad)).toFloat()
        val y2 = cy + (endR * kotlin.math.sin(angleRad)).toFloat()

        drawLine(
          color = if (i % 3 == 0) AmberLamp.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.25f),
          start = Offset(x1, y1),
          end = Offset(x2, y2),
          strokeWidth = if (i % 3 == 0) 2.dp.toPx() else 1.dp.toPx()
        )
      }

      // Moon-Koi Scale Needle (Pointed to Residual Moonlight)
      val needlePath = Path().apply {
        moveTo(cx, cy - h * 0.44f) // Tip of the scale
        lineTo(cx + w * 0.10f, cy)
        lineTo(cx, cy + h * 0.28f) // Base
        lineTo(cx - w * 0.10f, cy)
        close()
      }

      // Silver translucent scale body
      drawPath(
        path = needlePath,
        brush = Brush.verticalGradient(
          colors = listOf(
            Color.White,
            MoonSilver,
            BlueGlass.copy(alpha = 0.6f)
          )
        )
      )

      // Scale needle outline
      drawPath(
        path = needlePath,
        color = Color.White.copy(alpha = 0.8f),
        style = Stroke(width = 1.2.dp.toPx())
      )

      // The single vermilion drop of sealing wax (like Nami's gill mark)
      drawCircle(
        color = VermilionCourier,
        radius = w * 0.055f,
        center = Offset(cx, cy - h * 0.22f)
      )

      // Center pivot brass rivet
      drawCircle(
        color = AmberLamp,
        radius = w * 0.08f,
        center = Offset(cx, cy)
      )
      drawCircle(
        color = Color(0xFF2A1C08),
        radius = w * 0.035f,
        center = Offset(cx, cy)
      )
    }
  }
}
