package com.example.justbintime.data

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.justbintime.data.dao.BinDao
import com.example.justbintime.data.BinWithColours
import com.example.justbintime.data.dao.ColourDao
import kotlinx.coroutines.flow.Flow

class BinRepository(private val binDao: BinDao, private val colourDao: ColourDao) {
    val allBinsWithColours: Flow<List<BinWithColours>> = binDao.observeAllWithColours()

    @WorkerThread
    suspend fun addBin(binWC: BinWithColours) {
        colourDao.upsert(binWC.colours)

        val retrievedColours = colourDao.getByPrimary(binWC.colours.cPrimary).first()
        Log.d("BinRepository", "Added colour \"${retrievedColours.bcId}\" successfully")

        binWC.bin.binColoursId = retrievedColours.bcId
        binDao.upsert(binWC.bin)
        Log.d("BinRepository", "Added bin \"${binWC.bin.name}\" successfully")
    }

    @WorkerThread
    suspend fun updateBin(binWC: BinWithColours) {
        colourDao.upsert(binWC.colours)
        val retrievedColours = colourDao.getByPrimary(binWC.colours.cPrimary).first()
        Log.d("BinRepository", "Updated colour \"${retrievedColours.bcId}\" successfully")
        binWC.bin.binColoursId = retrievedColours.bcId
        binDao.upsert(binWC.bin)
        Log.d("BinRepository", "Updated bin \"${binWC.bin.name}\" successfully")
    }

    @WorkerThread
    suspend fun deleteAllBins() {
        binDao.deleteAll()
        Log.e("BinRepository", "Deleted all bins")
    }
}