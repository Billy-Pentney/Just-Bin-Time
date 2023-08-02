package com.example.justbintime.viewmodel

import androidx.compose.ui.graphics.Color
import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.obj.BinColours
import com.example.justbintime.data.obj.BinIcon
import com.example.justbintime.data.obj.BinReminder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/*
* This class simulates a BinViewModel, i.e. a holder of a Bin Ui State
* Its functions deliberately have no effect, since
* IT MUST ONLY BE USED FOR COMPOSE PREVIEWS
*/
class SimBinViewModel(private val binUiState: BinUiState): IBinHolder {

    constructor() : this(BinUiState())

    override fun getUiState(): StateFlow<BinUiState> {
        return MutableStateFlow(binUiState)
    }

    override fun updateBin(bin: Bin): Job = Job()
    override fun insertBin(bin: Bin): Job = Job()
    override fun deleteBin(bin: Bin): Job = Job()

    override fun insertColour(binColours: BinColours) = Job()
    override fun insertIcon(icon: BinIcon) = Job()

    override fun setVisibleBin(dispBin: DisplayableBin) { return }
    override fun getVisibleBin(): DisplayableBin? { return null }

    override fun getIconForNewBin(): BinIcon { return BinIcon.makeDefault() }

    override fun getBinColoursId(primaryColour: Color): Int { return 0 }
    override fun getBinIconId(iconName: String): Int { return 0 }

    override fun getColours(): List<Color> { return BinColours.ALL_COLORS }

    override fun upsertBinReminder(binReminder: BinReminder) = Job()
    override fun deleteBinReminder(binReminder: BinReminder) = Job()
    override fun deleteReminderForBin(bin: Bin): Job = Job()
}