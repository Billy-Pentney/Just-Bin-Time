package com.example.justbintime

import com.example.justbintime.ui.theme.BinGardenColor
import com.example.justbintime.ui.theme.BinLandfillColor
import com.example.justbintime.ui.theme.BinRecyclingColor
import java.time.LocalDate

class BinFactory {

    fun makeLandfillBin(): Bin {
        return Bin(
            name = "Landfill",
            color = BinLandfillColor,
            firstCollectionDate = LocalDate.parse("2023-06-13"),
//            daysBetweenCollections = 7,
            iconResId = R.drawable.bin_landfill
        )
    }

    fun makeRecyclingBin(): Bin {
        return Bin (
            name = "Recycling",
            color = BinRecyclingColor,
            firstCollectionDate = LocalDate.parse("2023-06-20"),
            iconResId = R.drawable.bin_recycle
        )
    }

    fun makeGardenBin(): Bin {
        return Bin(
            name = "Garden",
            color = BinGardenColor,
            firstCollectionDate = LocalDate.parse("2023-06-20"),
            iconResId = R.drawable.bin_garden
        )
    }

    fun makeUiState(): BinUiState {
        val binUiState = BinUiState()
        binUiState.addBin(makeLandfillBin())
        binUiState.addBin(makeRecyclingBin())
        binUiState.addBin(makeGardenBin())
        return binUiState
    }
}