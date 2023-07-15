package com.example.justbintime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Upsert
import com.example.justbintime.data.`object`.BinColours
import kotlinx.coroutines.flow.Flow

@Dao
interface ColourDao {
    @Query("SELECT * FROM bin_colours")
    fun getAll(): List<BinColours>

    @Query("SELECT * FROM bin_colours")
    fun observeAll(): Flow<List<BinColours>>

    @Query("SELECT * FROM bin_colours WHERE bcId = :id")
    fun getById(id: Int): List<BinColours>

    @Query("SELECT * FROM bin_colours WHERE cPrimary = :colourValue")
    fun getByPrimary(colourValue: Long): List<BinColours>

    @Insert
    fun insert(colours: BinColours)
    @Insert
    fun insertMany(colours: List<BinColours>)

    @Delete
    fun delete(col: BinColours)

    @Upsert
    fun upsert(vararg cols: BinColours)

    @Query("DELETE FROM bin_colours")
    fun deleteAll()

}
