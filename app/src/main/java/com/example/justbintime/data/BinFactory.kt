package com.example.justbintime.data

import com.example.justbintime.data.`object`.Bin
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.ui.theme.BinGardenColor
import com.example.justbintime.ui.theme.BinLandfillColor
import com.example.justbintime.ui.theme.BinRecyclingColor
import java.time.LocalDateTime

const val origLandfillCollectDate = "2023-06-13T09:00:00"
const val origRecyclingCollectDate = "2023-06-20T09:00:00"
const val origGardenCollectDate = "2023-06-20T09:00:00"

class BinFactory {

    fun makeLandfillBinWithColours(): BinWithColours {
        val b = Bin(
            name = "Landfill",
            lastCollectionDate = LocalDateTime.parse(origLandfillCollectDate),
            iconResStr = "bin_landfill"
        )
        val c = BinColours(BinLandfillColor)
        return BinWithColours(b,c)
    }

    fun makeRecyclingBinWithColours(): BinWithColours {
        val b = Bin (
            name = "Recycling",
            lastCollectionDate = LocalDateTime.parse(origRecyclingCollectDate),
            iconResStr = "bin_recycle"
        )
        val c = BinColours(BinRecyclingColor)
        return BinWithColours(b,c)
    }

    fun makeGardenBinWithColours(): BinWithColours {
        val b = Bin(
            name = "Garden",
            lastCollectionDate = LocalDateTime.parse(origGardenCollectDate),
            iconResStr = "bin_garden"
        )
        val c = BinColours(BinGardenColor)
        return BinWithColours(b,c)
    }

    fun makeUiState(): BinUiState {
        val bins = listOf(makeLandfillBinWithColours(), makeRecyclingBinWithColours(), makeGardenBinWithColours())
        return BinUiState(bins)
    }
}