package com.example.justbintime.viewmodel

import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.DisplayableBin
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

// Represents an object which interfaces with a state of the bins
interface IBinHolder {
    fun getUiState(): StateFlow<BinUiState>
    fun updateBin(dispBin: DisplayableBin): Job
    fun setVisibleBin(dispBin: DisplayableBin)
    fun getVisibleBin(): DisplayableBin?
}