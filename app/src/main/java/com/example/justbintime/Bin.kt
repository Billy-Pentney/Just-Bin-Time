package com.example.justbintime

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalField


data class Bin(val name: String,
               val color: Color,
               val colorLight: Color,
               val colorDark: Color,
               val firstCollectionDate: LocalDateTime,
               val daysBetweenCollections: Long = defaultCollectionInterval,
               var markedPutOut: Boolean = false,
               var markedCollected: Boolean = false,
               val iconResId: Int = -1
) {

    companion object {
        // Maximum number of hours till collection before the warning sign is displayed for a bin
        const val HOURS_IMMINENT_COLLECTION: Int = 24
        // The default number of days between collections of a specific bin
        const val defaultCollectionInterval: Long = 14
        const val COLLECT_TIME_TODAY: String = "TODAY"
        const val COLLECT_TIME_TOMORROW: String = "TOMORROW"
    }


//    val collectHour: Int = firstCollectionDate.get(ChronoField.HOUR_OF_DAY)
//    val collectMin: Int = firstCollectionDate.get(ChronoField.MINUTE_OF_HOUR)
    var lastCollectionDate: LocalDateTime = firstCollectionDate
    var nextCollectionDate: LocalDateTime

    val collectedSincePutOut: Boolean = false
    var putOutDate: LocalDateTime? = null

    init {
        nextCollectionDate = lastCollectionDate.plusDays(daysBetweenCollections)
    }

    fun determineNextCollectionDate(now: LocalDateTime): LocalDateTime {
        while (nextCollectionDate.isBefore(now)) {
            lastCollectionDate = nextCollectionDate
            nextCollectionDate = nextCollectionDate.plusDays(daysBetweenCollections)
        }
        return nextCollectionDate
    }

    fun formatDate(date: LocalDateTime, formatString: String): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(formatString)
        return date.format(dateTimeFormatter)
    }

    fun getStatusText(now: LocalDateTime): String {
        return if (markedPutOut && now.isBefore(nextCollectionDate)) {
            "Ready for collection"
        } else if (markedPutOut) {
            "Ready to bring in"
        } else {
            "Not out"
        }
    }

    fun getActionText(now: LocalDateTime): String? {
        if (!markedPutOut && isCollectionImminent(now)) {
            return "I put the bin out"
        }
        else if (hasBeenCollected(now)) {
            return "I brought the bin in"
        }
        return null
    }

    fun hasIcon(): Boolean {
        return this.iconResId > -1
    }

    fun hasBeenCollected(now: LocalDateTime): Boolean {
        if (nextCollectionDate.isBefore(now)) {
            determineNextCollectionDate(now)
        }
        if (markedPutOut && putOutDate != null) {
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

    fun updateState(now: LocalDateTime) {
        if (!markedPutOut) {
            putOutAt(now)
        }
        else {
            bringInAt(now)
        }
    }

    fun putOutAt(now: LocalDateTime) {
        if (putOutDate == null) {
            putOutDate = now
            markedPutOut = true
        }
    }

    fun bringInAt(now: LocalDateTime) {
        if (now.isAfter(lastCollectionDate)) {
            markedPutOut = false
            putOutDate = null
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

//    fun updateState(now: LocalDateTime): Boolean {
//        if (markedPutOut && putOutDate != null && lastCollectionDate.isAfter(putOutDate)) {
//            determineNextCollectionDate(now)
//        }
//        else if (markedPutOut) {
//            // do nothing
//        }
//        else {
//            markedPutOut = true
//        }
//    }
}