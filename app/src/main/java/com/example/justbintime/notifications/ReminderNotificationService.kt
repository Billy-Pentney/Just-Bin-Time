package com.example.justbintime.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.justbintime.MainActivity
import com.example.justbintime.R
import com.example.justbintime.data.AppDatabase
import com.example.justbintime.data.obj.BinReminder
import java.time.LocalDateTime
import java.time.ZoneId


/**
 * Handles the displaying of Local Notifications, for example optional BinReminders.
 * Can be triggered by an Intent, e.g. by the Alarm Manager
 */
class ReminderNotificationService: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("NotificationService", "Notification Intent received")
        val binName = intent.getStringExtra(EXTRA_BIN_NAME) ?: "Untitled"
        val binCollectTime = intent.getStringExtra(EXTRA_BIN_COLLECT_TIME) ?: "Unknown time"
        showBinReminderNotification(context, notificationManager, binName, binCollectTime)

//        val reminderScheduler = AndroidReminderScheduler(context)
//        reminderScheduler.updateSoonestReminder()
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "bin_reminder_channel"
        const val REMINDER_NOTIFICATION_TITLE = "Bin Reminder"
        const val EXTRA_BIN_NAME = "BIN_NAME"
        const val EXTRA_BIN_COLLECT_TIME = "BIN_COLLECT_TIME"

        private var notificationIndex = 0

        // Display a notification for the bin with the given name
        fun showBinReminderNotification(context: Context, notificationManager: NotificationManager,
                                        binName: String, binCollectTime: String) {
            Log.d("NotificationService","Displaying notification for bin $binName")
            // Make an intent to start this app when the notification is clicked
            val openAppIntent = Intent(context, MainActivity::class.java)
            val openAppPendingIntent = PendingIntent.getActivity(
                context, 1, openAppIntent, PendingIntent.FLAG_IMMUTABLE
            )
            val notification = Notification.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.bin_generic)
                .setContentTitle(REMINDER_NOTIFICATION_TITLE)
                .setContentText("Your bin \"$binName\" is due for collection tomorrow at $binCollectTime")
                .setContentIntent(openAppPendingIntent)
                .build()

            notificationManager.notify(notificationIndex++, notification)
        }
    }
}