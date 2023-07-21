package com.example.justbintime.notifications

import com.example.justbintime.data.obj.Bin

interface ReminderScheduler {
    fun schedule(bin: Bin)
    fun cancel(bin: Bin)
}