package com.example.justbintime.notifications

import com.example.justbintime.data.obj.BinReminder

interface ReminderScheduler {
    fun schedule(reminder: BinReminder)
    fun cancel(reminder: BinReminder)
//    fun cancelAndReschedule(reminder: BinReminder)
}