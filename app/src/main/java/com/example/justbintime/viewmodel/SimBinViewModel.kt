package com.example.justbintime.viewmodel

import com.example.justbintime.BinUiState
import com.example.justbintime.data.Bin
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Simulates a BinViewModel, i.e. a holder of a Bin Ui State
// Should only be used for Compose Previews
class SimBinViewModel(private val binUiState: BinUiState): IModelBinUIState {
    override fun getUiState(): StateFlow<BinUiState> {
        return MutableStateFlow(binUiState)
    }
    override fun updateBin(bin: Bin): Job {
        return Job()
    }
//    override fun addBin(bin: Bin): Job {
//        return Job()
//    }
}