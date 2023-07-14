package com.example.justbintime.data

import com.example.justbintime.data.`object`.Bin
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinIcon
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
            isDefault = true
        )
    }

    fun makeRecyclingBin(): Bin {
        return Bin (
            name = "Recycling",
            lastCollectionDate = LocalDateTime.parse(origRecyclingCollectDate),
            isDefault = true
        )
    }

    fun makeGardenBin(): Bin {
        return Bin (
            name = "Garden",
            lastCollectionDate = LocalDateTime.parse(origGardenCollectDate),
            isDefault = true
        )
    }

    fun makeLandfillBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeLandfillBin(),
            BinColours(BinLandfillColor),
            BinIcon(drawableResStr = BinIcon.LANDFILL_RES, drawableName = "Landfill")
        )
    }

    fun makeRecyclingBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeRecyclingBin(),
            BinColours(BinRecyclingColor),
            BinIcon(drawableResStr = BinIcon.RECYCLING_RES, drawableName = "Recycling")
        )
    }

    fun makeGardenBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeGardenBin(),
            BinColours(BinGardenColor),
            BinIcon(drawableResStr = BinIcon.GARDEN_RES, drawableName = "Garden")
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
        val genericIcon = BinIcon(drawableResStr = BinIcon.GENERIC_RES, drawableName = "General")
        val landfillIcon = BinIcon(drawableResStr = BinIcon.LANDFILL_RES, drawableName = "Landfill")
        val recyclingIcon = BinIcon(drawableResStr = BinIcon.RECYCLING_RES, drawableName = "Recycling")
        val gardenIcon = BinIcon(drawableResStr = BinIcon.GARDEN_RES, drawableName = "Garden")
        val medicalIcon = BinIcon(drawableResStr = BinIcon.MEDICAL_RES, drawableName = "Medical")
        val industrialIcon = BinIcon(drawableResStr = BinIcon.INDUSTRIAL_RES, drawableName = "Industrial")

        return listOf(
            genericIcon, landfillIcon, recyclingIcon,
            gardenIcon, medicalIcon, industrialIcon
        )
    }
}