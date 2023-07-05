package com.example.justbintime.data

import com.example.justbintime.BinUiState
import com.example.justbintime.R
import com.example.justbintime.ui.theme.BinGardenColor
import com.example.justbintime.ui.theme.BinGardenColorDark
import com.example.justbintime.ui.theme.BinGardenColorLight
import com.example.justbintime.ui.theme.BinLandfillColor
import com.example.justbintime.ui.theme.BinLandfillColorDark
import com.example.justbintime.ui.theme.BinLandfillColorLight
import com.example.justbintime.ui.theme.BinRecyclingColor
import com.example.justbintime.ui.theme.BinRecyclingColorDark
import com.example.justbintime.ui.theme.BinRecyclingColorLight
import java.time.LocalDateTime

const val origLandfillCollectDate = "2023-06-13T09:00:00"
const val origRecyclingCollectDate = "2023-07-20T09:00:00"
const val origGardenCollectDate = "2023-06-20T09:00:00"

class BinFactory {

    fun makeLandfillBin(): Bin {
        return Bin(
            name = "Landfill",
            colors = BinColours(BinLandfillColor, BinLandfillColorLight, BinLandfillColorDark),
            firstCollectionDate = LocalDateTime.parse(origLandfillCollectDate),
            iconResStr = "bin_landfill"
        )
    }

    fun makeRecyclingBin(): Bin {
        return Bin (
            name = "Recycling",
            colors = BinColours(BinRecyclingColor, BinRecyclingColorLight, BinRecyclingColorDark),
            firstCollectionDate = LocalDateTime.parse(origRecyclingCollectDate),
            iconResStr = "bin_recycle"
        )
    }

    fun makeGardenBin(): Bin {
        return Bin(
            name = "Garden",
            colors = BinColours(BinGardenColor, BinGardenColorLight, BinGardenColorDark),
            firstCollectionDate = LocalDateTime.parse(origGardenCollectDate),
            iconResStr = "bin_garden"
        )
    }

    fun makeUiState(): BinUiState {
        val bins = listOf(makeLandfillBin(), makeRecyclingBin(), makeGardenBin())
        return BinUiState(bins)
    }
}