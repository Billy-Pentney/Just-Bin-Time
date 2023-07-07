package com.example.justbintime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.justbintime.BinRepository
import com.example.justbintime.BinUiState
import com.example.justbintime.data.Bin
import com.example.justbintime.data.BinFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BinViewModel(private val binRepo: BinRepository): ViewModel(), IModelBinUIState {
    val binsLive: LiveData<List<Bin>> = binRepo.allBinItems.asLiveData()
    private val uiState = MutableStateFlow(BinUiState())
    var initialisedBins = false

    init {
        addLiveDataObserver()
    }

    fun addLiveDataObserver() {
        binsLive.observeForever { binList ->
            uiState.update {
                BinUiState(binList)
            }
        }
    }

    override fun getUiState(): StateFlow<BinUiState> {
        return uiState
    }

    override fun updateBin(bin: Bin) = viewModelScope.launch(Dispatchers.IO) {
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