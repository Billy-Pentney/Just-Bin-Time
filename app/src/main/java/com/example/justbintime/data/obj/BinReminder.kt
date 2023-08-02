package com.example.justbintime.data.obj

import android.app.AlarmManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity(tableName = "bin_reminder")
data class BinReminder(
    @PrimaryKey val reminderId: Int = 0,
    @ColumnInfo val alarmEpochTimeMillis: Long,
    @ColumnInfo val formattedAlarmTimeStr: String,
    @ColumnInfo val formattedAlarmDateStr: String,
    @ColumnInfo val binName: String,
    @ColumnInfo val referencedBinId: Int,
    @ColumnInfo val repeatIntervalMillis: Long = -1
) {
    fun getRepeatIntervalDays(): Int? {
        if (repeatIntervalMillis >= MIN_REPEAT_INTERVAL_MILLIS) {
            return (repeatIntervalMillis / AlarmManager.INTERVAL_DAY).toInt()
        }
        return null
    }

    companion object {
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")!!
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")!!
        const val MIN_REPEAT_INTERVAL_MILLIS = 1000

        // Given a reminder for a time in the past,
        // increment it by its repeatIntervalMillis, until
        // it is in the future
        // Returns null if the given reminder has repeatIntervalMillis < 1
        fun incrementUntilFuture(reminder: BinReminder, currEpochMillis: Long): BinReminder? {
            if (reminder.repeatIntervalMillis < MIN_REPEAT_INTERVAL_MILLIS) {
                return null
            }

            var reminderMillis = reminder.alarmEpochTimeMillis
            while (reminderMillis <= currEpochMillis) {
                reminderMillis += reminder.repeatIntervalMillis
            }
            // Convert the new reminder's alarm time, after incrementing (so we can format it)
            val newAlarmDateTime = Instant.ofEpochMilli(reminderMillis)
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime()
            return BinReminder(
                reminder.reminderId,
                newAlarmDateTime,
                reminder.binName,
                reminder.referencedBinId,
                reminder.repeatIntervalMillis
            )
        }
    }

    constructor(reminderId: Int,
                alarmLocalDateTime: LocalDateTime,
                binName: String,
                referencedBinId: Int,
                repeatIntervalMillis: Long
    ): this(
        reminderId,
        alarmEpochTimeMillis = alarmLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        formattedAlarmTimeStr = alarmLocalDateTime.format(timeFormatter),
        formattedAlarmDateStr = alarmLocalDateTime.format(dateFormatter),
        binName,
        referencedBinId,
        repeatIntervalMillis
    )

    constructor(bin: Bin) :
            this(
                reminderId = 0,
                alarmLocalDateTime = bin.getNextReminderTime(),
                binName = bin.name,
                referencedBinId = bin.binId,
                repeatIntervalMillis= bin.daysBetweenCollections * AlarmManager.INTERVAL_DAY
            )
}
