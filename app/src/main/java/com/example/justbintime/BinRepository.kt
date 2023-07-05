package com.example.justbintime

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import com.example.justbintime.data.Bin
import com.example.justbintime.data.BinDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class BinRepository(private val binDao: BinDao) {
    val allBinItems: Flow<List<Bin>> = binDao.observeAll()

    @WorkerThread
    suspend fun addBin(bin: Bin) {
        binDao.insertAll(bin)
        Log.d("BinRepository", "Added bin \"${bin.name}\" successfully")
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
}