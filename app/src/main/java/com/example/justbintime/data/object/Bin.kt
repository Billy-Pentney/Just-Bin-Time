package com.example.justbintime.data.`object`

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Entity(tableName="bins")
data class Bin(
    @PrimaryKey(autoGenerate=true) val binId: Int = 0,
    @ColumnInfo var name: String,
    @ColumnInfo var binColoursId: Int = 0,
    @ColumnInfo var lastCollectionDate: LocalDateTime,
    @ColumnInfo var daysBetweenCollections: Int = DEFAULT_COLLECT_INTERVAL,
    @ColumnInfo var binIconId: Int = 0,
    @ColumnInfo val isDefault: Boolean = false
) {
    companion object {
        // Maximum number of hours till collection before the warning sign is displayed for a bin
        const val HOURS_IMMINENT_COLLECTION: Int = 24
        // The default number of days between collections of a specific bin
        const val DEFAULT_COLLECT_INTERVAL: Int = 14
        const val COLLECT_TIME_TODAY: String = "TODAY"
        const val COLLECT_TIME_TOMORROW: String = "TOMORROW"

        // Enums representing the status of the bin
        const val BIN_INSIDE: Int = 0
        const val BIN_OUT_UNCOLLECTED: Int = 1
        const val BIN_OUT_COLLECTED: Int = 2
    }

    @ColumnInfo var nextCollectionDate: LocalDateTime = lastCollectionDate.plusDays(daysBetweenCollections.toLong())
    @ColumnInfo var stateUpdatedAt: LocalDateTime = LocalDateTime.now()
    @ColumnInfo var state: Int = BIN_INSIDE

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
        return state == BIN_OUT_UNCOLLECTED || state == BIN_OUT_COLLECTED
    }

    fun getStatusText(now: LocalDateTime): String {
        return when {
            isPutOut() && isCollectionImminent(now) -> "Out, due for collection"
            isPutOut()                              -> "Out, ready to bring in"
            isCollectionImminent(now)               -> "Not out, due for collection"
            else                                    -> "Not due for collection"
        }
    }

    // Returns a nullable string describing the permitted action at the given time
    // based on whether the bin is put out/ready to be collected
    // If null, then no action is possible
    fun getActionText(now: LocalDateTime): String? {
        return when {
            isCollectionImminent(now) && !isPutOut() -> "I've put the bin out!"
            isPutOut()                               -> "I've brought the bin in!"
            else                                     -> null
        }
    }

    fun hasBeenCollected(now: LocalDateTime): Boolean {
        if (nextCollectionDate.isBefore(now)) {
            determineNextCollectionDate(now)
        }
        if (isPutOut()) {
            if (stateUpdatedAt.isBefore(lastCollectionDate) && lastCollectionDate.isBefore(now)) {
                state = BIN_OUT_COLLECTED
            }
            return state == BIN_OUT_COLLECTED
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
            stateUpdatedAt = now
            state = BIN_OUT_UNCOLLECTED
        }
    }

    fun bringInAt(now: LocalDateTime) {
        if (now.isAfter(lastCollectionDate)) {
            stateUpdatedAt = now
            state = BIN_INSIDE
            determineNextCollectionDate(now)
        }
    }

    fun getWhenCollectionStr(now: LocalDateTime): String? {
        val hours = hoursTillCollection(now)
        // Same day as the collection (but before it)
        if (hours <= nextCollectionDate.hour)
            return COLLECT_TIME_TODAY
        // Less than 24 hours to go
        else if (hours <= HOURS_IMMINENT_COLLECTION)
            return COLLECT_TIME_TOMORROW

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

    fun setCollectionDate(collectAt: LocalDateTime) {
        lastCollectionDate = collectAt
        nextCollectionDate = collectAt
        determineNextCollectionDate(LocalDateTime.now())
    }
}