package com.example.justbintime.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.justbintime.data.AppDatabase
import com.example.justbintime.data.obj.BinReminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidReminderScheduler(val context: Context): ReminderScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
//    private val REQUEST_CODE = 919

    override fun schedule(reminder: BinReminder) {
        val intent = Intent(context, ReminderNotificationService::class.java).apply {
            putExtra(ReminderNotificationService.EXTRA_BIN_NAME, reminder.binName)
            putExtra(
                ReminderNotificationService.EXTRA_BIN_COLLECT_TIME,
                reminder.formattedAlarmTimeStr
            )
        }
        // Use the BinId as the request_code for the alarm sender
        // this allows the bin to be cancelled if it is disabled later
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.referencedBinId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Get the time for the alarm as the number of milliseconds since UNIX time
        val alarmTimeMillis = reminder.alarmEpochTimeMillis
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTimeMillis,
//            AlarmManager.INTERVAL_DAY * binReminder.repeatIntervalDays,
            pendingIntent
        )

        Log.d("ReminderScheduler", "Set alarm for \"{reminder.binName}\" @ $alarmTimeMillis millis")

        val repeatIntervalDays = reminder.getRepeatIntervalDays()
        var toastText = "Scheduled reminder for bin \"${reminder.binName}\" " +
                "at ${reminder.formattedAlarmTimeStr} " +
                "on ${reminder.formattedAlarmDateStr}"
        if (repeatIntervalDays != null)
            toastText += " and repeating every $repeatIntervalDays days"

        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
    }

    override fun cancel(reminder: BinReminder) {
        // Check for Intent equality doesn't care about extras
        val intent = Intent(context, ReminderNotificationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, reminder.referencedBinId, intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            Log.d("ReminderScheduler", "Disabled reminder alarm for bin ${reminder.binName}")
        }
        else {
            Log.d("ReminderScheduler", "Cannot disable next alarm. Reason: no intent found")
        }
        Toast.makeText(context, "Disabled reminder for bin \"${reminder.binName}\"", Toast.LENGTH_SHORT).show()
    }

//    override fun cancelAndReschedule(reminder: BinReminder) {
//        cancel(reminder)
//        schedule(reminder)
//    }

    suspend fun updateSoonestReminder() = coroutineScope {
        val database = AppDatabase.getDatabase(context)
        val reminderDao = database.reminderDao()
        val now = LocalDateTime.now()
        val epochMillisNow = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        // Get all reminders in order of their date, earliest first

        val expiredReminders = reminderDao.getAllExpiredReminders(epochMillisNow)
        val updatedReminders = mutableListOf<BinReminder>()
        for (reminder in expiredReminders) {
            val updatedReminder = BinReminder.incrementUntilFuture(reminder, epochMillisNow)
            if (updatedReminder != null) {
                updatedReminders.add(updatedReminder)
            }
        }
        reminderDao.upsertAll(updatedReminders)
    }
}