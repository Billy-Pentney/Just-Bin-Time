package com.example.justbintime

import android.app.Application
import com.example.justbintime.data.AppDatabase
import com.example.justbintime.data.BinRepository

class BinApplication: Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }

    val repo by lazy { BinRepository(database.binDao(), database.colourDao()) }
}