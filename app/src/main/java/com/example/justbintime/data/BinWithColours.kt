package com.example.justbintime.data

import android.content.Context
import androidx.room.Embedded
import androidx.room.Relation
import com.example.justbintime.data.`object`.Bin
import com.example.justbintime.data.`object`.BinColours
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class BinWithColours(
    @Embedded val bin: Bin,
    @Relation(
        parentColumn = "binColoursId",
        entityColumn = "bcId"
    )
    val colours: BinColours
) {
    fun getName(): String {
        return bin.name
    }

    fun getStatusText(now: LocalDateTime): String {
        return bin.getStatusText(now)
    }

    fun getActionText(now: LocalDateTime): String? {
        return bin.getActionText(now)
    }

    fun getNextCollectionDateStr(): String {
        return bin.getNextCollectionDateStr()
    }

    fun hasBeenCollected(now: LocalDateTime): Boolean {
        return bin.hasBeenCollected(now)
    }

    fun determineNextCollectionDate(now: LocalDateTime) {
        bin.determineNextCollectionDate(now)
    }

    fun getIconId(context: Context): Int? {
        return bin.getIconId(context)
    }

    fun getIconDescription(): String {
        return bin.getIconDescription()
    }

    fun updateState(now: LocalDateTime) {
        bin.updateState(now)
    }

    fun getWhenCollectionStr(now: LocalDateTime): String? {
        return bin.getWhenCollectionStr(now)
    }

    fun getIconResStr(): String? {
        return bin.iconResStr
    }

    fun getLastCollectionDate(): LocalDate {
        return bin.lastCollectionDate.toLocalDate()
    }

    fun getLastCollectionTime(): LocalTime {
        return bin.lastCollectionDate.toLocalTime()
    }

    fun getLastCollection(): LocalDateTime {
        return bin.lastCollectionDate
    }

    fun getCollectInterval(): Int {
        return bin.daysBetweenCollections
    }

    fun updateColours(newColours: BinColours) {
        bin.binColoursId = newColours.bcId
    }
}
