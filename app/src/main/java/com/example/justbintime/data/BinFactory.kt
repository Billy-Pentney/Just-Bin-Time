package com.example.justbintime.data

import com.example.justbintime.data.`object`.Bin
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinIcon
import com.example.justbintime.data.`object`.BinIcon.Companion.GARDEN_RES
import com.example.justbintime.data.`object`.BinIcon.Companion.LANDFILL_RES
import com.example.justbintime.data.`object`.BinIcon.Companion.RECYCLING_RES
import com.example.justbintime.ui.theme.BinGardenColor
import com.example.justbintime.ui.theme.BinLandfillColor
import com.example.justbintime.ui.theme.BinRecyclingColor
import java.time.LocalDateTime

const val origLandfillCollectDate = "2023-06-13T09:00:00"
const val origRecyclingCollectDate = "2023-06-20T09:00:00"
const val origGardenCollectDate = "2023-06-20T09:00:00"


// Creates (default/standard) instances of Bins, Colour Schemes and Icons
// Used when pre-populating the Database
class BinFactory {

    fun makeLandfillBin(): Bin {
        return Bin (
            name = "Landfill",
            lastCollectionDate = LocalDateTime.parse(origLandfillCollectDate),
        )
    }

    fun makeRecyclingBin(): Bin {
        return Bin (
            name = "Recycling",
            lastCollectionDate = LocalDateTime.parse(origRecyclingCollectDate),
        )
    }

    fun makeGardenBin(): Bin {
        return Bin (
            name = "Garden",
            lastCollectionDate = LocalDateTime.parse(origGardenCollectDate),
        )
    }

    fun makeLandfillBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeLandfillBin(),
            BinColours(BinLandfillColor),
            BinIcon(drawableResStr = LANDFILL_RES)
        )
    }

    fun makeRecyclingBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeRecyclingBin(),
            BinColours(BinRecyclingColor),
            BinIcon(drawableResStr = RECYCLING_RES)
        )
    }

    fun makeGardenBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeGardenBin(),
            BinColours(BinGardenColor),
            BinIcon(drawableResStr = GARDEN_RES)
        )
    }

    // Used to preview the Compose layout
    fun makeUiState(): BinUiState {
        val bins = listOf(
            makeLandfillBinWithColours(),
            makeRecyclingBinWithColours(),
            makeGardenBinWithColours()
        )
        return BinUiState(bins)
    }

    fun makeIcons(): List<BinIcon> {
        val genericIcon = BinIcon(drawableResStr = BinIcon.GENERIC_RES)
        val landfillIcon = BinIcon(drawableResStr = BinIcon.LANDFILL_RES)
        val recyclingIcon = BinIcon(drawableResStr = BinIcon.RECYCLING_RES)
        val gardenIcon = BinIcon(drawableResStr = BinIcon.GARDEN_RES)
        val medicalIcon = BinIcon(drawableResStr = BinIcon.MEDICAL_RES)
        val industrialIcon = BinIcon(drawableResStr = BinIcon.INDUSTRIAL_RES)

        return listOf(
            genericIcon, landfillIcon, recyclingIcon,
            gardenIcon, medicalIcon, industrialIcon
        )
    }
}