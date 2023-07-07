package com.example.justbintime.data

import androidx.compose.ui.graphics.Color
import java.lang.Float.min

data class BinColours(
    val primary: Color,
    val light: Color,
    val dark: Color
) {
    companion object {
        val GRAY = Color(0.7f, 0.7f, 0.7f, 1.0f)
        val RED = Color(0.85f, 0.2f, 0.2f, 1.0f)
        val ORANGE = Color(0.85f, 0.45f, 0.1f, 1.0f)
        val YELLOW = Color(0.9f, 0.8f, 0.2f, 1.0f)
        val GREEN = Color(0.2f, 0.7f, 0.3f, 1.0f)
        val BLUE = Color(0.1f,0.6f,0.8f,1f)
        val PURPLE = Color(0.4f, 0.3f, 0.85f, 1.0f)
        val BROWN = Color(0.4f, 0.2f, 0.0f, 1.0f)
        val BLACK = Color(0.2f, 0.2f, 0.2f, 1.0f)
        val ALL_COLORS = listOf(GRAY, RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, BROWN, BLACK)

        fun augment(col: Color, mult: Float): Color {
            val r = min(col.red*mult, 1f)
            val g = min(col.green*mult, 1f)
            val b = min(col.blue*mult, 1f)
            return Color(r,g,b,col.alpha)
        }

        fun drop(col: Color): Color {
            val r = col.red / 2.0f
            val g = col.green / 2.0f
            val b = col.blue / 2.0f
            return Color(r,g,b,col.alpha)
        }

        fun boost(col: Color): Color {
            val r = col.red + (1f - col.red) / 2f
            val g = col.green + (1f - col.green) / 2f
            val b = col.blue + (1f - col.blue) / 2f
            return Color(r,g,b,col.alpha)
        }
    }

    constructor (col: Color): this(col, boost(col), drop(col))

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