package com.example.justbintime.data

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.justbintime.data.dao.BinDao
import com.example.justbintime.data.dao.ColourDao
import com.example.justbintime.data.dao.IconDao
import com.example.justbintime.data.`object`.Bin
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinIcon
import kotlinx.coroutines.flow.Flow

class BinRepository(
    private val binDao: BinDao,
    private val colourDao: ColourDao,
    private val iconDao: IconDao
) {
    val allDisplayableBins: Flow<List<DisplayableBin>> = binDao.observeAllWithColours()
    val allBinColours: Flow<List<BinColours>> = colourDao.observeAll()
    val allBinIcons: Flow<List<BinIcon>> = iconDao.observeAll()
//    private val iconResourceMapLive: LiveData<Map<String, Int>> = iconDao.observeMapByDrawableStr()

//    @WorkerThread
//    suspend fun addBin(dbin: DisplayableBin) {
//        iconDao.upsert(dbin.icon)
//        colourDao.upsert(dbin.colours)
//        val icon = iconDao.getByResourceString(dbin.icon.drawableResStr).first()
//        val retrievedColours = colourDao.getByPrimary(dbin.colours.cPrimary).first()
//        dbin.bin.binIconId = icon.iconId
//        dbin.bin.binColoursId = retrievedColours.bcId
//
//        binDao.upsert(dbin.bin)
//        Log.d("BinRepository", "Added bin \"${dbin.bin.name}\" successfully")
//    }

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

}