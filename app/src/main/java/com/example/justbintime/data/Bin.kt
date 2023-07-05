package com.example.justbintime.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Entity(tableName="bins")
data class Bin(
    @PrimaryKey(autoGenerate=true) val id: Int = 0,
    @ColumnInfo var name: String,
    @ColumnInfo var colors: BinColours,
    @ColumnInfo var firstCollectionDate: LocalDateTime,
    @ColumnInfo var daysBetweenCollections: Int = defaultCollectionInterval,
    @ColumnInfo var iconResStr: String? = null,
) {
    companion object {
        // Maximum number of hours till collection before the warning sign is displayed for a bin
        const val HOURS_IMMINENT_COLLECTION: Int = 24
        // The default number of days between collections of a specific bin
        const val defaultCollectionInterval: Int = 14
        const val COLLECT_TIME_TODAY: String = "TODAY"
        const val COLLECT_TIME_TOMORROW: String = "TOMORROW"

        // Enums representing the status of the bin
        const val BIN_INSIDE: Int = 0
        const val BIN_OUT_UNCOLLECTED: Int = 1
        const val BIN_OUT_COLLECTED: Int = 2
    }

    @ColumnInfo var lastCollectionDate: LocalDateTime = firstCollectionDate
    @ColumnInfo var nextCollectionDate: LocalDateTime
    @ColumnInfo var putOutDate: LocalDateTime? = null

//    var state: Int = BIN_INSIDE

    init {
        nextCollectionDate = lastCollectionDate.plusDays(daysBetweenCollections.toLong())
    }

    fun determineNextCollectionDate(now: LocalDateTime): LocalDateTime {
        while (nextCollectionDate.isBefore(now)) {
            lastCollectionDate = nextCollectionDate
            nextCollectionDate = nextCollectionDate.plusDays(daysBetweenCollections.toLong())
        }
        return nextCollectionDate
    }

    fun formatDate(date: LocalDateTime, formatString: String): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(formatString)
        return date.format(dateTimeFormatter)
    }

    fun isPutOut(): Boolean {
        // Returns true iff putOutDate is not null and it
        return putOutDate?.isAfter(lastCollectionDate) ?: false
    }

    fun getStatusText(now: LocalDateTime): String {
//        val place = if (isPutOut()) "Out, " else "Not out"
//        val collectDue =
//            when {
//                isCollectionImminent(now) -> "due for collection"
//                isPutOut() && nextCollectionDate.isBefore(now) -> "ready to bring in"
//                else -> "not due for collection"
//            }

        return when {
            isPutOut() && isCollectionImminent(now) -> "Out, due for collection"
            isPutOut() -> "Out, ready to bring in"
            isCollectionImminent(now) -> "Not out, due for collection"
            else -> "Not due for collection"
        }

//        return "$place, $collectDue"

//        return if (isPutOut() && now.isBefore(nextCollectionDate)) {
//            "Ready for collection"
//        } else if (isPutOut()) {
//            "Ready to bring in"
//        } else {
//            "Not out"
//        }
    }

    fun getActionText(now: LocalDateTime): String? {
        if (isCollectionImminent(now)) {
            if (!isPutOut()) {
                return "I put the bin out"
            }
//        else if (hasBeenCollected(now)) {
            return "I brought the bin in"
//        }
        }
        // No Action
        return null
    }

    fun getIconId(context: Context): Int? {
        return try {
            // Attempt to retrieve the resource ID for this bin's icon
            context.resources.getIdentifier(iconResStr, "drawable", context.packageName)
        } catch (ex: RuntimeException) {
            null
        }
    }

    fun hasBeenCollected(now: LocalDateTime): Boolean {
        if (nextCollectionDate.isBefore(now)) {
            determineNextCollectionDate(now)
        }
        if (isPutOut()) {
            return putOutDate!!.isBefore(lastCollectionDate) && lastCollectionDate.isBefore(now)
        }
        return false
    }

    fun getIconDescription(): String {
        return this.name + " Bin Icon"
    }

    fun hoursTillCollection(now: LocalDateTime): Int {
        return now.until(nextCollectionDate, ChronoUnit.HOURS).toInt()
    }

    fun isCollectionImminent(now: LocalDateTime): Boolean {
        return now.until(nextCollectionDate, ChronoUnit.HOURS) < HOURS_IMMINENT_COLLECTION
    }

    fun updateState(now: LocalDateTime) {
        if (!isPutOut()) {
            putOutAt(now)
        }
        else {
            bringInAt(now)
        }
    }

    fun putOutAt(now: LocalDateTime) {
        if (!isPutOut()) {
            putOutDate = now
        }
    }

    fun bringInAt(now: LocalDateTime) {
        if (now.isAfter(lastCollectionDate)) {
            putOutDate = null
            determineNextCollectionDate(now)
        }
    }

    fun getWhenCollectionStr(now: LocalDateTime): String? {
        val hours = hoursTillCollection(now)
        if (hours <= nextCollectionDate.hour)
            return "TODAY"
        // 24
        else if (hours <= HOURS_IMMINENT_COLLECTION)
            return "TOMORROW"

        return null
    }

    fun updateLastCollection(y: Int, m: Int, d: Int) {
        val date = LocalDate.of(y,m,d)
        val time = lastCollectionDate.toLocalTime()
        lastCollectionDate = LocalDateTime.of(date, time)
        nextCollectionDate = lastCollectionDate
        determineNextCollectionDate(LocalDateTime.now())
    }

    fun getNextCollectionDateStr(): String {
        return formatDate(nextCollectionDate, "EEEE, dd MMMM yyyy HH:mm")
    }

//    fun toStoredBin(): StoredBin {
//        return StoredBin(
//            name = this.name,
//            colorPrimary = this.colors.primary.value.toLong(),
//            colorLight = this.colors.light.value.toLong(),
//            colorDark = this.colors.dark.value.toLong(),
//            firstCollectionDate = this.firstCollectionDate,
//            daysBetweenCollections = this.daysBetweenCollections,
//            lastPutOutDate
//            nextCollectionDate = this.nextCollectionDate,
//            iconResStr = this.iconResStr
//        )
//    }
}