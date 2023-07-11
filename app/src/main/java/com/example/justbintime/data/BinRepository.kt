package com.example.justbintime.data

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.justbintime.data.dao.BinDao
import com.example.justbintime.data.dao.ColourDao
import com.example.justbintime.data.dao.IconDao
import kotlinx.coroutines.flow.Flow

class BinRepository(
    private val binDao: BinDao,
    private val colourDao: ColourDao,
    private val iconDao: IconDao
) {
    val allDisplayableBins: Flow<List<DisplayableBin>> = binDao.observeAllWithColours()
    private val iconResourceMapLive: LiveData<Map<String, Int>> = iconDao.observeMapByDrawableStr()

    @WorkerThread
    suspend fun addBin(dbin: DisplayableBin) {
//        val iconResourceMap = iconResourceMapLive.value
//
//        if (iconResourceMap != null && iconResourceMap.containsKey(dbin.icon.drawableResStr)) {
//            dbin.bin.binIconId = iconResourceMap[dbin.icon.drawableResStr]!!
//        }
//        else {
            iconDao.upsert(dbin.icon)
            val icon = iconDao.getByResourceString(dbin.icon.drawableResStr).first()
            dbin.bin.binIconId = icon.iconId
//        }

        colourDao.upsert(dbin.colours)

        val retrievedColours = colourDao.getByPrimary(dbin.colours.cPrimary).first()
//        Log.d("BinRepository", "Added colour \"${retrievedColours.bcId}\" successfully")

        dbin.bin.binColoursId = retrievedColours.bcId

        binDao.upsert(dbin.bin)
        Log.d("BinRepository", "Added bin \"${dbin.bin.name}\" successfully")
    }

    @WorkerThread
    suspend fun updateBin(dbin: DisplayableBin) {
        iconDao.upsert(dbin.icon)
        colourDao.upsert(dbin.colours)

        // Now get the ID of the colour scheme (so we can create the association from bin to colours)
        val retrievedColours = colourDao.getByPrimary(dbin.colours.cPrimary).first()
//        Log.d("BinRepository", "Updated colour \"${retrievedColours.bcId}\" successfully")
        val icon = iconDao.getByResourceString(dbin.icon.drawableResStr).first()

        dbin.bin.binColoursId = retrievedColours.bcId
        dbin.bin.binIconId = icon.iconId
        binDao.upsert(dbin.bin)
        Log.d("BinRepository", "Updated bin \"${dbin.bin.name}\" successfully")
    }

    @WorkerThread
    suspend fun deleteAllBins() {
        binDao.deleteAll()
        Log.e("BinRepository", "Deleted all bins")
    }

    @WorkerThread
    suspend fun deleteBin(binWC: DisplayableBin) {
        binDao.delete(binWC.bin)
        Log.e("BinRepository", "Deleted bin ${binWC.getName()}")
    }

}