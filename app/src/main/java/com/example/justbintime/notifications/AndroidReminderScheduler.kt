package com.example.justbintime.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.justbintime.data.obj.Bin
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AndroidReminderScheduler(val context: Context): ReminderScheduler {
    private val alarmManager =
        context.getSystemService(AlarmManager::class.java)

    override fun schedule(bin: Bin) {
        val intent = Intent(context, ReminderNotificationService::class.java).apply {
            putExtra(ReminderNotificationService.EXTRA_BIN_NAME, bin.name)
            putExtra(ReminderNotificationService.EXTRA_BIN_COLLECT_TIME, bin.getFormattedCollectTime())
        }
        // Use the BinId as the request_code for the alarm sender
        // this allows the bin to be cancelled if it is disabled later
        val pendingIntent = PendingIntent.getBroadcast(
            context, bin.binId, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Set the Alarm for exactly 24 hours before the next collection
        val alarmTimeMillis = bin.getNextReminderTimeInMillis()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeMillis,
            pendingIntent,
        )

        // Convert the alarm time back to a LocalDateTime (so it can be displayed as a toast)
        val alarmDateTime = Instant.ofEpochMilli(alarmTimeMillis)
                                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        val alarmTimeStr = alarmDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val alarmDateStr = alarmDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        Toast.makeText(context, "Scheduled reminder for bin \"${bin.name}\" " +
                                        "at $alarmTimeStr on $alarmDateStr", Toast.LENGTH_LONG)
             .show()
    }

    override fun cancel(bin: Bin) {
        val intent = Intent(context, ReminderNotificationService::class.java).apply {
            putExtra("BIN_NAME", bin.name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, bin.binId, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Disabled reminder for bin \"${bin.name}\"", Toast.LENGTH_SHORT)
             .show()
    }
}