package com.example.justbintime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BinViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BinViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BinViewModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}