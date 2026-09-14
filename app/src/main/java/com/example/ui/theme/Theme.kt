package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SkyboundDarkColorScheme = darkColorScheme(
  primary = BlueGlass,
  onPrimary = VoidDark,
  primaryContainer = BlueGlassDeep,
  onPrimaryContainer = MoonSilver,
  secondary = AmberLamp,
  onSecondary = VoidDark,
  secondaryContainer = AmberLampWarm,
  onSecondaryContainer = AmberLampSoft,
  tertiary = VermilionCourier,
  onTertiary = Color.White,
  background = VoidDark,
  onBackground = TextPrimaryNight,
  surface = DeepNight,
  onSurface = TextPrimaryNight,
  surfaceVariant = NightSurface,
  onSurfaceVariant = TextSecondaryNight,
  outline = NightCardBorder
)

private val SkyboundParchmentColorScheme = lightColorScheme(
  primary = BlueGlassDeep,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFD0E8FF),
  onPrimaryContainer = BlueGlassDeep,
  secondary = AmberLampWarm,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFFFECC2),
  onSecondaryContainer = Color(0xFF5C3600),
  tertiary = VermilionCourier,
  onTertiary = Color.White,
  background = ParchmentBg,
  onBackground = ParchmentText,
  surface = ParchmentSurface,
  onSurface = ParchmentText,
  surfaceVariant = Color(0xFFE4DCCB),
  onSurfaceVariant = ParchmentTextSecondary,
  outline = Color(0xFFC7BCA7)
)

@Composable
fun MyApplicationTheme(
  isParchment: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (isParchment) SkyboundParchmentColorScheme else SkyboundDarkColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
