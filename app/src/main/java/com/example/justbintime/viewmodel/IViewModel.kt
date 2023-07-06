package com.example.justbintime.viewmodel

import com.example.justbintime.BinUiState
import com.example.justbintime.data.Bin
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

// Represents an object which interfaces with a state of the bins
interface IModelBinUIState {
    fun getUiState(): StateFlow<BinUiState>
    fun updateBin(bin: Bin): Job
//    fun addBin(bin: Bin): Job
}