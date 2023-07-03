package com.example.justbintime

import com.example.justbintime.ui.theme.BinGardenColor
import com.example.justbintime.ui.theme.BinGardenColorDark
import com.example.justbintime.ui.theme.BinGardenColorLight
import com.example.justbintime.ui.theme.BinLandfillColor
import com.example.justbintime.ui.theme.BinLandfillColorDark
import com.example.justbintime.ui.theme.BinLandfillColorLight
import com.example.justbintime.ui.theme.BinRecyclingColor
import com.example.justbintime.ui.theme.BinRecyclingColorDark
import com.example.justbintime.ui.theme.BinRecyclingColorLight
import java.time.LocalDate
import java.time.LocalDateTime

const val origLandfillCollectDate = "2023-06-13T09:00:00"
const val origRecyclingCollectDate = "2023-06-20T09:00:00"
const val origGardenCollectDate = "2023-06-20T09:00:00"

class BinFactory {

    fun makeLandfillBin(): Bin {
        return Bin(
            name = "Landfill",
            color = BinLandfillColor,
            colorLight = BinLandfillColorLight,
            colorDark = BinLandfillColorDark,
            firstCollectionDate = LocalDateTime.parse(origLandfillCollectDate),
//            daysBetweenCollections = 7,
            iconResId = R.drawable.bin_landfill
        )
    }

    fun makeRecyclingBin(): Bin {
        return Bin (
            name = "Recycling",
            color = BinRecyclingColor,
            colorLight = BinRecyclingColorLight,
            colorDark = BinRecyclingColorDark,
            firstCollectionDate = LocalDateTime.parse(origRecyclingCollectDate),
            iconResId = R.drawable.bin_recycle
        )
    }

    fun makeGardenBin(): Bin {
        return Bin(
            name = "Garden",
            color = BinGardenColor,
            colorLight = BinGardenColorLight,
            colorDark = BinGardenColorDark,
            firstCollectionDate = LocalDateTime.parse(origGardenCollectDate),
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