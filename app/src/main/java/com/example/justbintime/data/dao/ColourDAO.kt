package com.example.justbintime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.justbintime.data.`object`.BinColours

@Dao
interface ColourDao {
    @Query("SELECT * FROM bincolours")
    fun getAll(): List<BinColours>

    @Query("SELECT * FROM bincolours WHERE bcId = :id")
    fun getById(id: Int): List<BinColours>

    @Query("SELECT * FROM bincolours WHERE cPrimary = :colour")
    fun getByPrimary(colour: Long): List<BinColours>

    @Delete
    fun delete(col: BinColours)

    @Upsert
    fun upsert(vararg cols: BinColours)

    @Query("DELETE FROM bincolours")
    fun deleteAll()
}
