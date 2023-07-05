package com.example.justbintime.data

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// View of a bin within the Database
// this is converted into a Bin

//@Entity(tableName="bin")

// NOTE: this class is UNUSED
data class StoredBin (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val colorPrimary: Long,
    @ColumnInfo val colorLight: Long,
    @ColumnInfo val colorDark: Long,
    @ColumnInfo val firstCollectionDate: LocalDateTime,
    @ColumnInfo val daysBetweenCollections: Int,
    @ColumnInfo val lastCollectionDate: LocalDateTime,
    @ColumnInfo val nextCollectionDate: LocalDateTime,
    @ColumnInfo val lastPutOutDate: LocalDateTime,
    @ColumnInfo val iconResStr: String
) {
    fun toBin(): Bin {
        val cols = BinColours(Color(colorPrimary), Color(colorLight), Color(colorDark))
        val b = Bin(
            uid,
            name,
            cols,
            firstCollectionDate,
            daysBetweenCollections,
            iconResStr
        )
        b.lastCollectionDate = lastCollectionDate
        b.nextCollectionDate = nextCollectionDate
        b.putOutDate = lastPutOutDate
        return b
    }
}