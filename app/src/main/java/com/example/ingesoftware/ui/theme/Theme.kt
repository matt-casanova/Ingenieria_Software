package com.example.ingesoftware.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PetroleumBlue,
    onPrimary = Color.White,
    primaryContainer = PetroleumBlueContainer,
    onPrimaryContainer = OnPetroleumBlueContainer,
    secondary = SoftGreen,
    onSecondary = Color.White,
    secondaryContainer = SoftGreenContainer,
    onSecondaryContainer = OnSoftGreenContainer,
    tertiary = PetroleumBlueDark,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = OnSurfaceText,
    surface = LightSurface,
    onSurface = OnSurfaceText,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceTextVariant,
    outline = OutlineColor
)

@Composable
fun IngeSoftwareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
