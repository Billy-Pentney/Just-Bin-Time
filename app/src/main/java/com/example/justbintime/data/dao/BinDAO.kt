package com.example.justbintime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.DisplayableBin
import kotlinx.coroutines.flow.Flow

@Dao
interface BinDao {
    @Query("SELECT * FROM bins")
    fun getAll(): List<Bin>

    @Query("SELECT * FROM bins WHERE name IN (:names)")
    fun loadAllByIds(names: List<String>): List<Bin>

    @Insert
    fun insert(vararg bin: Bin)
    @Insert
    fun insertMany(bins: List<Bin>)

    @Delete
    fun delete(bin: Bin)

    @Update
    fun update(vararg bin: Bin)

    @Upsert
    fun upsert(vararg bin: Bin)

    @Query("DELETE FROM bins")
    fun deleteAll()

    @Query("SELECT * FROM bins " +
            "WHERE sendReminder IS 1 " +
            "ORDER BY nextCollectionDate DESC LIMIT 1")
    fun getNextBinForReminder(): List<Bin>

    // Migrating to DisplayableBin

    @Transaction
    @Query("SELECT * FROM bins")
    fun getAllDisplayableBins(): List<DisplayableBin>

    @Transaction
    @Query("SELECT * FROM bins ORDER BY nextCollectionDate")
    fun observeAllDisplayableBins(): Flow<List<DisplayableBin>>
}
