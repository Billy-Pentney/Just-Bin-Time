package com.example.justbintime.viewmodel

import androidx.lifecycle.ViewModel
import com.example.justbintime.BinUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BinViewModel: ViewModel() {
    //    private var binList = ArrayList<Bin>()
    private var uiStateMut = MutableStateFlow(BinUiState())
    val uiState: StateFlow<BinUiState> = uiStateMut.asStateFlow()
}