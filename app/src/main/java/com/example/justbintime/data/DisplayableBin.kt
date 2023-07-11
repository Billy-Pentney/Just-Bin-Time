package com.example.justbintime.data

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.util.Log
import androidx.room.Embedded
import androidx.room.Relation
import com.example.justbintime.data.`object`.Bin
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinIcon
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


data class DisplayableBin(
    @Embedded val bin: Bin,
    @Relation(
        parentColumn = "binColoursId",
        entityColumn = "bcId"
    )
    val colours: BinColours,
    @Relation(
        parentColumn = "binIconId",
        entityColumn = "iconId"
    )
    val icon: BinIcon
) {
    fun getName(): String {
        return bin.name
    }

    fun getIconId(context: Context): Int? {
        return try {
            // Attempt to retrieve the resource ID for this bin's icon
            Log.e("DisplayableBin", "Try to load resource with string \"${icon.drawableResStr}\"")
            context.resources.getIdentifier(icon.drawableResStr, "drawable", context.packageName)
        } catch (ex: Resources.NotFoundException) {
            Log.e("DisplayableBin", "Cannot load resource with string \"${icon.drawableResStr}\"")
            null
        }
    }

    fun getStatusText(now: LocalDateTime): String {
        return bin.getStatusText(now)
    }

    fun getActionText(now: LocalDateTime): String? {
        return bin.getActionText(now)
    }

    fun getNextCollectionDateStr(now: LocalDateTime): String {
        if (hasBeenCollected(now)) {
            determineNextCollectionDate(now)
        }
        return bin.getNextCollectionDateStr()
    }

    fun hasBeenCollected(now: LocalDateTime): Boolean {
        return bin.hasBeenCollected(now)
    }

    fun determineNextCollectionDate(now: LocalDateTime) {
        bin.determineNextCollectionDate(now)
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

    fun getIconResStr(): String {
        return icon.drawableResStr
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
