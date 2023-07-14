package com.example.justbintime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.justbintime.data.BinRepository
import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.DisplayableBin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BinViewModel(private val binRepo: BinRepository): ViewModel(), IBinHolder {
    val displayBinLive: LiveData<List<DisplayableBin>> = binRepo.allDisplayableBins.asLiveData()
    private val uiState = MutableStateFlow(BinUiState())
    var initialisedBins = false
    // Used when navigating to EditBinScreen, to identify the current bin being viewed, if any
    private var visibleBin: DisplayableBin? = null

    init {
        addLiveDataObserver()
    }

    private fun addLiveDataObserver() {
        displayBinLive.observeForever { displayBinList ->
            uiState.update {
                it.copy(displayBinList)
            }
        }
    }

    override fun setVisibleBin(bin: DisplayableBin) { visibleBin = bin }
    override fun getVisibleBin(): DisplayableBin? { return visibleBin }

    override fun getUiState(): StateFlow<BinUiState> {
        return uiState
    }

    override fun updateBin(dispBin: DisplayableBin) = upsertBin(dispBin)

    fun upsertBin(dispBin: DisplayableBin) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.upsertBin(dispBin)
    }

    fun addBin(dispBin: DisplayableBin) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.addBin(dispBin)
    }

    fun deleteAllBins() = viewModelScope.launch(Dispatchers.IO) {
        binRepo.deleteAllBins()
    }

    fun deleteBin(dispBin: DisplayableBin) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.deleteBin(dispBin)
    }

    fun initDefaultBins() = viewModelScope.launch(Dispatchers.IO) {
        if (!initialisedBins) {
            initialisedBins = true
            deleteAllBins()
            val binFactory = BinFactory()
            addBin(binFactory.makeLandfillBinWithColours())
            addBin(binFactory.makeRecyclingBinWithColours())
            addBin(binFactory.makeGardenBinWithColours())
        }
    }
}