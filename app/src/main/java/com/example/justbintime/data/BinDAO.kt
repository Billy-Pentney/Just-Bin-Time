package com.example.justbintime.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.justbintime.data.Bin
import kotlinx.coroutines.flow.Flow

@Dao
interface BinDao {
    @Query("SELECT * FROM bins")
    fun getAll(): List<Bin>

    @Query("SELECT * FROM bins WHERE name IN (:names)")
    fun loadAllByIds(names: List<String>): List<Bin>

    @Insert
    fun insertAll(vararg bin: Bin)

    @Delete
    fun delete(bin: Bin)

    @Update
    fun update(vararg bin: Bin)

    @Query("SELECT * FROM bins")
    fun observeAll(): Flow<List<Bin>>

    @Query("DELETE FROM bins")
    fun deleteAll()
}
