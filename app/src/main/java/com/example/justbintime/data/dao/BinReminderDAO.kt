package com.example.justbintime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.justbintime.data.ReminderWithBin
import com.example.justbintime.data.obj.BinReminder
import kotlinx.coroutines.flow.Flow

@Dao
interface BinReminderDao {
    @Query("SELECT * FROM bin_reminder")
    fun getAll(): List<BinReminder>
    @Transaction
    @Query("SELECT * FROM bin_reminder")
    fun getAllWithBin(): List<ReminderWithBin>

    @Query("SELECT * FROM bin_reminder ORDER BY alarmEpochTimeMillis")
    fun getAllSoonestFirst(): List<BinReminder>
    @Query("SELECT * FROM bin_reminder WHERE alarmEpochTimeMillis <= :currTimeMillis " +
            "ORDER BY alarmEpochTimeMillis")
    fun getAllExpiredReminders(currTimeMillis: Long): List<BinReminder>
    @Query("SELECT * FROM bin_reminder WHERE referencedBinId = :binId")
    fun getByBinId(binId: Int): List<BinReminder>

    @Delete
    fun delete(binReminder: BinReminder): Int

    @Upsert
    fun upsert(binReminder: BinReminder)
    @Upsert
    fun upsertAll(updatedReminders: MutableList<BinReminder>)

    @Query("SELECT * FROM bin_reminder ORDER BY alarmEpochTimeMillis LIMIT 1")
    fun observeSoonestReminder(): Flow<BinReminder?>
    @Query("SELECT * FROM bin_reminder ORDER BY alarmEpochTimeMillis")
    fun observeAllOrderedSoonestFirst(): Flow<List<BinReminder>>
}
