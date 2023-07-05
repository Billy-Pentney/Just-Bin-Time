package com.example.justbintime.data

import androidx.compose.ui.graphics.Color

data class BinColours(
    val primary: Color,
    val light: Color = primary,
    val dark: Color = primary
) {

    fun getForegroundColor(darkTheme: Boolean): Color {
        if (darkTheme)
            return light
        return dark
    }

    fun getBackgroundColor(darkTheme: Boolean): Color {
        if (darkTheme)
            return dark
        return light
    }

    fun toArray(): Array<String> {
        return arrayOf(primary.toString(), light.toString(), dark.toString())
    }
}