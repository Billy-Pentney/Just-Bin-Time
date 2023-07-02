package com.example.justbintime

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun makeDefaultTime(): LocalDate {
    return LocalDate.of(2023, 6,6)
}

// The default number of days between collections of a specific bin
const val defaultCollectionInterval: Long = 14


data class Bin(val name: String,
               val color: Color,
               val firstCollectionDate: LocalDate = makeDefaultTime(),
               val daysBetweenCollections: Long = defaultCollectionInterval,
               var markedPutOut: Boolean = false,
               var markedCollected: Boolean = false,
               val iconResId: Int = -1
) {
//    var lastCollectionDate: LocalDate = firstCollectionDate
    var nextCollectionDate: LocalDate = firstCollectionDate

//    var collectionDay: DayOfWeek = firstCollectionDate.dayOfWeek

    init {
        nextCollectionDate.plusDays(daysBetweenCollections)
    }

    fun getNextCollectionDate(now: LocalDateTime): LocalDate {
//        val millsDiff = today.until() - lastCollection.timeInMillis
//        val millsInWeek = (1000*60*60*24*7)
//        // Number of milliseconds since the last time this bin was collected
//        val millsSinceLastCollection = millsDiff % millsInWeek

//        val daysDiff = nextCollectionDate.until(today).days
//        val daysSinceCollection = daysDiff % daysBetweenCollections
//        val daysLeft = daysBetweenCollections - daysSinceCollection
//        nextCollectionDate = today.plusDays(daysLeft)
//        Log.e("Testing", nextCollectionDate.toString())

        while (nextCollectionDate.isBefore(now.toLocalDate())) {
            nextCollectionDate = nextCollectionDate.plusDays(daysBetweenCollections)
        }
        return nextCollectionDate
    }

//    fun getNextCollectionDayStr(now: LocalDateTime): String {
//        return formatDate(getNextCollectionDate(now), "yyyy-mm-dd HH:MM")
//    }

    fun formatDate(date: LocalDate, formatString: String): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(formatString)
        return date.format(dateTimeFormatter)
    }

    fun getStatusText(): String {
        return if (markedPutOut && !markedCollected) {
            "Out for collection"
        } else if (markedPutOut) {
            "Out after collection"
        } else {
            "Not out"
        }
    }

    fun hasIcon(): Boolean {
        return this.iconResId > -1
    }

    fun getIconDescription(): String {
        return this.name + " Bin Icon"
    }

//    fun daysTillNextCollection(): Long {
//        val now = LocalDateTime.now()
//        return now.until(getNextCollectionDate(now), ChronoUnit.DAYS)
//    }
}