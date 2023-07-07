package com.example.justbintime.data

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import org.json.JSONObject
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

//    @TypeConverter
//    // TODO - replace this with a new Colours table in the DB
//    fun fromBinColours(binCols: BinColours): String {
//        return JSONObject().apply {
//            put("primary", binCols.primary.value.toLong())
//            put("light", binCols.light.value.toLong())
//            put("dark", binCols.dark.value.toLong())
//        }.toString()
//    }
//    @TypeConverter
//    fun toBinColours(source: String): BinColours {
//        val json = JSONObject(source)
//        val primary = json.getLong("primary")
//        val light = json.getLong("light")
//        val dark = json.getLong("dark")
//        return BinColours(
//            Color(primary.toULong()),
//            Color(light.toULong()),
//            Color(dark.toULong())
//        )
//    }

    @TypeConverter
    fun fromColour(col: Color): Long {
        return col.value.toLong()
    }
    @TypeConverter
    fun toColour(value: Long): Color {
        return Color(value.toULong())
    }
}
