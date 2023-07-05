package com.example.justbintime.viewmodel

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.justbintime.BinPhraseGenerator
import com.example.justbintime.BinRepository
import com.example.justbintime.BinUiState
import com.example.justbintime.data.Bin
import com.example.justbintime.data.BinFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BinViewModel(private val binRepo: BinRepository): ViewModel() {
    val binsLive: LiveData<List<Bin>> = binRepo.allBinItems.asLiveData()
    var initialisedBins = false

    fun updateBin(bin: Bin) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.updateBin(bin)
    }

    fun addBin(bin: Bin) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.addBin(bin)
    }

    fun deleteAllBins() = viewModelScope.launch(Dispatchers.IO) {
        binRepo.deleteAllBins()
    }

    fun initDefaultBins() = viewModelScope.launch(Dispatchers.IO) {
        if (!initialisedBins) {
            initialisedBins = true
            deleteAllBins()
            val binFactory = BinFactory()
            addBin(binFactory.makeLandfillBin())
            addBin(binFactory.makeRecyclingBin())
            addBin(binFactory.makeGardenBin())
        }
    }
}