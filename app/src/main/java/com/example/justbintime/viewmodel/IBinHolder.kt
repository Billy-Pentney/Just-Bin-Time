package com.example.justbintime.viewmodel

import androidx.compose.ui.graphics.Color
import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.data.`object`.Bin
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinIcon
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

// Represents an object which interfaces with a state of the bins
// The BinViewModel should extend this interface
// This allows the SimBinViewModel to be used to act as a BinViewModel in Compose Previews
interface IBinHolder {
    fun getUiState(): StateFlow<BinUiState>

    fun updateBin(bin: Bin): Job
    fun insertBin(bin: Bin): Job
    fun deleteBin(bin: Bin): Job

    fun insertColour(binColours: BinColours): Job
    fun insertIcon(icon: BinIcon): Job

    fun setVisibleBin(dispBin: DisplayableBin)
    fun getVisibleBin(): DisplayableBin?

    fun getIconForNewBin(): BinIcon
    fun getBinColoursId(primaryColour: Color): Int?
    fun getBinIconId(iconName: String): Int?

    fun getColours(): List<Color>

}