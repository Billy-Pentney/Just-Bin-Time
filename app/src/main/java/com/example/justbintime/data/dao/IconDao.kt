package com.example.justbintime.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Upsert
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinIcon
import kotlinx.coroutines.flow.Flow

@Dao
interface IconDao {
    @Query("SELECT * FROM bin_icons")
    fun getAll(): List<BinIcon>

    @Query("SELECT * FROM bin_icons WHERE drawableResStr LIKE :resourceStr")
    fun getByResourceString(resourceStr: String): List<BinIcon>

    @MapInfo(keyColumn = "drawableResStr", valueColumn = "iconId")
    @Query("SELECT drawableResStr, iconId FROM bin_icons")
    fun observeMapByDrawableStr(): LiveData<Map<String, Int>>

    @Delete
    fun delete(icon: BinIcon)

    @Query("DELETE FROM bin_icons")
    fun deleteAll()

    @Upsert
    fun upsert(vararg icons: BinIcon)

    @Insert
    fun insert(vararg icons: BinIcon)

    @Upsert
    fun upsertAll(binIcons: List<BinIcon>)

}