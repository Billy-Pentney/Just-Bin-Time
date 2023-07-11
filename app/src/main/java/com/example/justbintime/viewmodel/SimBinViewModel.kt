package com.example.justbintime.viewmodel

import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.DisplayableBin
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Simulates a BinViewModel, i.e. a holder of a Bin Ui State
// Should *only* be used for Compose Previews
class SimBinViewModel(private val binUiState: BinUiState): IBinHolder {
    override fun getUiState(): StateFlow<BinUiState> {
        return MutableStateFlow(binUiState)
    }
    override fun updateBin(dispBin: DisplayableBin): Job {
        return Job()
    }

    override fun setVisibleBin(dispBin: DisplayableBin) {
        return
    }
    override fun getVisibleBin(): DisplayableBin? {
        return null
    }
//    override fun addBin(dispBin: Bin): Job {
//        return Job()
//    }
}