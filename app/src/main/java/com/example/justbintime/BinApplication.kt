package com.example.justbintime

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.justbintime.data.AppDatabase
import com.example.justbintime.data.BinRepository
import com.example.justbintime.notifications.ReminderNotificationService

class BinApplication: Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }

    val repo by lazy { BinRepository(database.binDao(), database.colourDao(), database.iconDao()) }


    override fun onCreate() {
        super.onCreate()

        // Set-up a Local Notification Channel to schedule Bin-reminder messages
        val binReminderChannel = NotificationChannel(
            ReminderNotificationService.REMINDER_CHANNEL_ID,
            "Bin Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        binReminderChannel.description = "Sends an optional reminder 24 hours before each bin is due for collection"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(binReminderChannel)
    }
}