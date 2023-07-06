package com.example.justbintime.data

import androidx.compose.ui.graphics.Color
import java.lang.Float.min

data class BinColours(
    val primary: Color,
    val light: Color = lighten(primary, 1.2f),
    val dark: Color = lighten(primary, 0.75f)
) {
    companion object {
        val RED = Color(0.8f,0.1f,0.05f,1f)
        val ORANGE = Color(0.8f,0.4f,0.2f,1f)
        val YELLOW = Color(0.7f,0.6f,0.2f,1f)
        val GREEN = Color(0.2f,0.6f,0.2f,1f)
        val BLUE = Color(0.1f,0.3f,0.6f,1f)
        val PURPLE = Color(0.3f, 0.1f, 0.6f, 1f)
        val ALL_COLORS = listOf(Color.White, RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE)

        fun lighten(col: Color, mult: Float): Color {
            val r = min(col.red*mult, 1f)
            val g = min(col.green*mult, 1f)
            val b = min(col.blue*mult, 1f)
            return Color(r,g,b,col.alpha)
        }
    }

    constructor (col: Color): this(lighten(col, 0.9f), lighten(col, 1.5f), lighten(col, 0.6f))

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