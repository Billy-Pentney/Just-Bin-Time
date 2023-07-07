package com.example.justbintime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.justbintime.data.BinRepository

class BinViewModelFactory(private val binRepository: BinRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BinViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BinViewModel(binRepository) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}