package com.example.justbintime.viewmodel

import androidx.lifecycle.ViewModel
import com.example.justbintime.BinUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime

class BinViewModel: ViewModel() {
    private var uiStateMut = MutableStateFlow(BinUiState())
    val uiState: StateFlow<BinUiState> = uiStateMut.asStateFlow()

    fun update(newUiState: BinUiState) {
        uiStateMut.value = newUiState
    }

    fun getBinStatusText(now: LocalDateTime): String {
        return uiStateMut.value.getBinStatus(now)
    }
}