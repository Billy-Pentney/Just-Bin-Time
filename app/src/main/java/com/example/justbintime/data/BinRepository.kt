package com.example.justbintime.data

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.justbintime.data.dao.BinDao
import com.example.justbintime.data.dao.BinReminderDao
import com.example.justbintime.data.dao.ColourDao
import com.example.justbintime.data.dao.IconDao
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.obj.BinColours
import com.example.justbintime.data.obj.BinIcon
import com.example.justbintime.data.obj.BinReminder
import kotlinx.coroutines.flow.Flow

class BinRepository(
    private val binDao: BinDao,
    private val colourDao: ColourDao,
    private val iconDao: IconDao,
    private val reminderDao: BinReminderDao
) {
    val allDisplayableBins: Flow<List<DisplayableBin>> = binDao.observeAllDisplayableBins()
    val allBinColours: Flow<List<BinColours>> = colourDao.observeAll()
    val allBinIcons: Flow<List<BinIcon>> = iconDao.observeAll()
    val reminders: Flow<List<BinReminder>> = reminderDao.observeAllOrderedSoonestFirst()
    // Stores the BinReminder which is next to be scheduled and to be triggered
    val soonestReminder: Flow<BinReminder?> = reminderDao.observeSoonestReminder()

    @WorkerThread
    suspend fun insertBin(bin: Bin) {
        binDao.insert(bin)
        Log.d("BinRepository", "Inserted bin \"${bin.name}\" successfully")
    }

    @WorkerThread
    suspend fun updateBin(bin: Bin) {
        binDao.update(bin)
        Log.d("BinRepository", "Updated bin \"${bin.name}\" successfully")
    }

    @WorkerThread
    suspend fun deleteAllBins() {
        binDao.deleteAll()
        Log.e("BinRepository", "Deleted all bins")
    }

    @WorkerThread
    suspend fun deleteBin(bin: Bin) {
        binDao.delete(bin)
        Log.e("BinRepository", "Deleted bin ${bin.name}")
    }

    @WorkerThread
    suspend fun addColours(colours: BinColours) {
        colourDao.insert(colours)
    }

    @WorkerThread
    suspend fun addIcon(icon: BinIcon) {
        iconDao.insert(icon)
    }

    // Clear the contents of the database and pre-populate it with the default bins
    fun resetBinsToDefault(context: Context) {
        val db = AppDatabase.getDatabase(context)
        AppDatabase.initDatabase(db)
    }

    @WorkerThread
    suspend fun upsertBinReminder(binReminder: BinReminder) {
        reminderDao.upsert(binReminder)
    }

    @WorkerThread
    suspend fun deleteBinReminder(binReminder: BinReminder) {
        reminderDao.delete(binReminder)
    }
}