package com.example.justbintime.viewmodel

import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.BinWithColours
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

// Represents an object which interfaces with a state of the bins
interface IBinHolder {
    fun getUiState(): StateFlow<BinUiState>
    fun updateBin(bwc: BinWithColours): Job
    fun setVisibleBin(bwc: BinWithColours)
    fun getVisibleBin(): BinWithColours?
}