package com.example.justbintime.notifications;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.justbintime.data.AppDatabase
import com.example.justbintime.data.obj.BinReminder
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmSchedulerResetService: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // When the device reboots, re-set all the alarms stored in the database
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
//            val reminderScheduler = AndroidReminderScheduler(context)
//            reminderScheduler.updateSoonestReminder()
            Log.d("AlarmSchedulerReset", "Device booted")
        }
    }
}
