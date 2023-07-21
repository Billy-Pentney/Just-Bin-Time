package com.example.justbintime.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.justbintime.data.BinRepository
import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.obj.BinColours
import com.example.justbintime.data.obj.BinIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BinViewModel(private val binRepo: BinRepository): ViewModel(), IBinHolder {
    private val displayBinLive: LiveData<List<DisplayableBin>> = binRepo.allDisplayableBins.asLiveData()
    private val coloursListLive = binRepo.allBinColours.asLiveData()
    private val iconListLive = binRepo.allBinIcons.asLiveData()

    private val uiState = MutableStateFlow(BinUiState())
    var initialisedBins = false
    // Used when navigating to EditBinScreen, to identify the current bin being viewed, if any
    private var visibleBin: DisplayableBin? = null

    init {
        addLiveDataObserver()
    }

    private fun addLiveDataObserver() {
        displayBinLive.observeForever { displayBinList ->
            Log.e("BinViewModel", "BinList updated! Size: {${displayBinList.size}")
            uiState.update {
                it.copy(displayBinList)
            }
        }

        // These observers are MANDATORY, otherwise the livedata isn't updated for some reason...
        coloursListLive.observeForever {
            // Do Nothing
            Log.e("BinViewModel", "ColoursList updated! Size: {${it.size}}")
        }
        iconListLive.observeForever {
            // Do Nothing
            Log.e("BinViewModel", "IconList updated! Size: {${it.size}}")
        }
    }

    override fun setVisibleBin(dispBin: DisplayableBin) { visibleBin = dispBin }
    override fun getVisibleBin(): DisplayableBin? { return visibleBin }

    override fun getUiState(): StateFlow<BinUiState> {
        return uiState
    }

    override fun updateBin(bin: Bin) = viewModelScope.launch(Dispatchers.IO) { binRepo.updateBin(bin) }

    override fun insertBin(bin: Bin) = viewModelScope.launch(Dispatchers.IO) { binRepo.insertBin(bin) }

    override fun deleteBin(bin: Bin) = viewModelScope.launch(Dispatchers.IO) { binRepo.deleteBin(bin) }

    fun deleteAllBins() = viewModelScope.launch(Dispatchers.IO) {
        binRepo.deleteAllBins()
    }



    fun initDefaultBins(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        if (!initialisedBins) {
            initialisedBins = true
            Toast.makeText(context, "Resetting to default bins", Toast.LENGTH_SHORT).show()
            binRepo.resetBinsToDefault(context)
        }
    }

    // For the given colour, check all the existing colours and
    // return the id of the first BinColours object which has that primary colour
    // Return null if none exist with the given colour
    override fun getBinColoursId(primaryColour: Color): Int? {
        val coloursList = coloursListLive.value
        if (coloursList != null) {
            for (binColour in coloursList) {
                if (binColour.colorPrimary == primaryColour) {
                    return binColour.bcId
                }
            }
        }
        return null
    }

    // For the given name of a drawable icon, check all the existing icons and
    // return the id of the first BinIcon object which has that name
    // Return null if none exist with the given icon name
    override fun getBinIconId(iconName: String): Int? {
        val iconList = iconListLive.value
        if (iconList != null) {
            for (icon in iconList) {
                if (icon.drawableName == iconName) {
                    return icon.iconId
                }
            }
        }
        return null
    }

    override fun getColours(): List<Color> {
        val bcList = coloursListLive.value ?: return listOf(Color.Gray)
        return bcList.map { it.colorPrimary }
    }

    override fun insertColour(binColours: BinColours) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.addColours(binColours)
    }

    override fun insertIcon(icon: BinIcon) = viewModelScope.launch(Dispatchers.IO) {
        binRepo.addIcon(icon)
    }

    override fun getIconForNewBin(): BinIcon {
        val iconList = iconListLive.value
        if (iconList.isNullOrEmpty()) {
            val icon = BinIcon.makeDefault()
            insertIcon(icon)
            return icon
        }
        return iconList[0]
    }

}