package com.example.justbintime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.justbintime.data.BinRepository
import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.BinWithColours
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BinViewModel(private val binRepo: BinRepository): ViewModel(), IBinHolder {
    val bwcLive: LiveData<List<BinWithColours>> = binRepo.allBinsWithColours.asLiveData()
    private val uiState = MutableStateFlow(BinUiState())
    var initialisedBins = false
    // Used when navigating to EditBinScreen, to identify the current bin being viewed, if any
    private var visibleBin: BinWithColours? = null

    init {
        addLiveDataObserver()
    }

    fun addLiveDataObserver() {
        bwcLive.observeForever { bwcList ->
            uiState.update {
                BinUiState(bwcList)
            }
        }
    }

    override fun setVisibleBin(bin: BinWithColours) { visibleBin = bin }
    override fun getVisibleBin(): BinWithColours? { return visibleBin }

    override fun getUiState(): StateFlow<BinUiState> {
        return uiState
    }

    override fun updateBin(bwc: BinWithColours) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.updateBin(bwc)
    }

    fun addBin(bwc: BinWithColours) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.addBin(bwc)
    }

    fun deleteAllBins() = viewModelScope.launch(Dispatchers.IO) {
        binRepo.deleteAllBins()
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