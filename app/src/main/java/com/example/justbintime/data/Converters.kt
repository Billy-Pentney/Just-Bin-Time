package com.example.justbintime.data

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.example.justbintime.data.obj.BinColours
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(str: String?): LocalDateTime? {
        return str?.let { LocalDateTime.parse(str) }
    }

    @TypeConverter
    fun fromColour(col: Color): Long {
        return BinColours.fromColorToLong(col)
    }
    @TypeConverter
    fun toColour(value: Long): Color {
        return BinColours.fromLongToColor(value)
    }
}
