package com.example.justbintime.data.`object`

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.justbintime.ui.theme.BinGardenColor
import com.example.justbintime.ui.theme.BinLandfillColor
import com.example.justbintime.ui.theme.BinRecyclingColor

@Entity(tableName = "bin_colours")
data class BinColours(
    // Need to use Longs to represent colours, since Compose.Color is an inline class which Room doesn't accept
    @ColumnInfo var cPrimary: Long = GREY.value.toLong(),
    @ColumnInfo var cLight: Long = boost(cPrimary).value.toLong(),
    @ColumnInfo var cDark: Long = drop(cPrimary).value.toLong(),
    @PrimaryKey(autoGenerate = true) var bcId: Int = 0,
) {
    // Custom getters allow the color to update itself (e.g. if Room manually sets the long values)
    @Ignore var colorPrimary: Color = fromLongToColor(cPrimary)
        get() {
            field = fromLongToColor(cPrimary)
            return field
        }
    @Ignore var colorLight: Color = fromLongToColor(cLight)
        get() {
            field = fromLongToColor(cLight)
            return field
        }
    @Ignore var colorDark: Color = fromLongToColor(cDark)
        get() {
            field = fromLongToColor(cDark)
            return field
        }

    companion object {
        val GREY = Color(0.7f, 0.7f, 0.7f, 1.0f)
        val RED = Color(0.85f, 0.2f, 0.2f, 1.0f)
        val ORANGE = Color(0.85f, 0.45f, 0.1f, 1.0f)
        val YELLOW = Color(0.9f, 0.8f, 0.2f, 1.0f)
        val GREEN = Color(0.2f, 0.7f, 0.3f, 1.0f)
        val TEAL = Color(0f, 0.5f, 0.5f, 1.0f)
        val CYAN = Color(0.1f,0.6f,0.8f,1f)
        val BLUE = Color(0.1f, 0.3f, 0.6f, 1.0f)
        val PURPLE = Color(0.5f, 0.3f, 0.85f, 1.0f)
        val BROWN = Color(0.4f, 0.2f, 0.0f, 1.0f)
        val BLACK = Color(0.2f, 0.2f, 0.2f, 1.0f)
        val PINK = Color(0.7f, 0.2f, 0.5f, 1.0f)

        val ALL_COLORS = listOf(
            RED, ORANGE, YELLOW, GREEN,
            TEAL, CYAN, BLUE, PURPLE,
            PINK, BROWN, GREY, BLACK,
            BinLandfillColor, BinRecyclingColor, BinGardenColor
        )

        // Return the index of the given colour in the list of available colours
        // This can be used to set the starting choice for a ColorDialogPicker, given
        // the initial colour of a Bin
        // TODO - rework this to use the BinColoursID and prepopulate the BC Table with *all* colours
        fun findIndexOfColour(col: Color): Int {
            for (i in 0..ALL_COLORS.size) {
                if (ALL_COLORS[i] == col) {
                    return i
                }
            }
            return 0
        }

        // Halves brightness of the given colour
        fun drop(col: Color): Color {
            val r = col.red / 2.0f
            val g = col.green / 2.0f
            val b = col.blue / 2.0f
            return Color(r,g,b,col.alpha)
        }

        fun drop(colVal: Long): Color {
            return drop(fromLongToColor(colVal))
        }

        // Doubles brightness of the given colour
        fun boost(col: Color): Color {
            val r = col.red + (1f - col.red) / 2f
            val g = col.green + (1f - col.green) / 2f
            val b = col.blue + (1f - col.blue) / 2f
            return Color(r,g,b,col.alpha)
        }

        fun boost(colVal: Long): Color {
            return boost(fromLongToColor(colVal))
        }

        fun fromLongToColor(long: Long): Color {
            return Color(long.toULong())
        }

        fun fromColorToLong(color: Color): Long {
            return color.value.toLong()
        }
    }

    constructor (primaryColour: Color): this(fromColorToLong(primaryColour))

    fun getForegroundColor(darkTheme: Boolean): Color {
        if (darkTheme)
            return colorLight
        return colorDark
    }

    fun getBackgroundColor(darkTheme: Boolean): Color {
        if (darkTheme)
            return colorDark
        return colorLight
    }
}