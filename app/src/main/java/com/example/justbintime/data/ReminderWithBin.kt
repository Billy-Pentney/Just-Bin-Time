package com.example.justbintime.data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.obj.BinReminder

data class ReminderWithBin (
    @Embedded
    val reminder: BinReminder,
    @Relation(parentColumn = "referencedBinId", entityColumn = "binId")
    val bin: Bin,
)