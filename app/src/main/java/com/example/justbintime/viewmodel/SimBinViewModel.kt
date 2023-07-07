package com.example.justbintime.viewmodel

import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.BinWithColours
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Simulates a BinViewModel, i.e. a holder of a Bin Ui State
// Should *only* be used for Compose Previews
class SimBinViewModel(private val binUiState: BinUiState): IBinHolder {
    override fun getUiState(): StateFlow<BinUiState> {
        return MutableStateFlow(binUiState)
    }
    override fun updateBin(bwc: BinWithColours): Job {
        return Job()
    }

    override fun setVisibleBin(bwc: BinWithColours) {
        return
    }
    override fun getVisibleBin(): BinWithColours? {
        return null
    }
//    override fun addBin(bin: Bin): Job {
//        return Job()
//    }
}