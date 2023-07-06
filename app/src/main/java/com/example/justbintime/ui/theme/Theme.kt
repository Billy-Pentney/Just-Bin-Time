package com.example.justbintime.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    surface = GreenPrimary950,
    primary = GreenPrimary700,
    primaryVariant = GreenPrimary900,
    secondary = GreenPrimary800,
    background = GreenPrimary950,
    onPrimary = GreenPrimary50,
    onSurface = GreenPrimary50,
    onSecondary = GreenPrimary50,
    onBackground = GreenPrimary50
)

private val LightColorPalette = lightColors(
    surface = GreenPrimary50,
    primary = GreenPrimary200,
    primaryVariant = GreenPrimary300,
    secondary = GreenPrimary100,
    background = GreenPrimary50,
    onPrimary = GreenPrimary900,
    onSurface = GreenPrimary900,
    onSecondary = GreenPrimary900,
    onBackground = GreenPrimary900
)

@Composable
fun JustBinTimeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}