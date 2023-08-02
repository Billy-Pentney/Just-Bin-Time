package com.example.justbintime.data

import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.obj.BinColours
import com.example.justbintime.data.obj.BinIcon
import com.example.justbintime.ui.theme.BinGardenColor
import com.example.justbintime.ui.theme.BinLandfillColor
import com.example.justbintime.ui.theme.BinRecyclingColor
import java.time.LocalDateTime


// Creates (default/standard) instances of Bins, Colour Schemes and Icons
// Used when pre-populating the Database
class BinFactory {
    companion object {
        const val origLandfillCollectDate = "2023-06-13T09:00:00"
        const val origRecyclingCollectDate = "2023-06-20T09:00:00"
        const val origGardenCollectDate = "2023-06-20T09:00:00"
    }

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
            BinIcon(BinIcon.LANDFILL_RES, BinIcon.LANDFILL_NAME),
            emptyList()
        )
    }

    fun makeRecyclingBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeRecyclingBin(),
            BinColours(BinRecyclingColor),
            BinIcon(BinIcon.RECYCLING_RES, BinIcon.RECYCLING_NAME),
            emptyList()
        )
    }

    fun makeGardenBinWithColours(): DisplayableBin {
        return DisplayableBin(
            makeGardenBin(),
            BinColours(BinGardenColor),
            BinIcon(BinIcon.GARDEN_RES, BinIcon.GARDEN_NAME),
            emptyList()
        )
    }

    // Used to preview the Compose layout
    fun makeUiState(): BinUiState {
        val bins = listOf(
            makeLandfillBinWithColours(),
            makeRecyclingBinWithColours(),
            makeGardenBinWithColours()
        )
        val uiState = BinUiState(bins)
        uiState.binDuePhrase = "Test Bin Phrase"
        return uiState
    }

    // Constructs the default set of BinIcons
    fun makeIcons(): List<BinIcon> {
        val genericIcon = BinIcon(BinIcon.GENERIC_RES, BinIcon.GENERIC_NAME)
        val landfillIcon = BinIcon(BinIcon.LANDFILL_RES, BinIcon.LANDFILL_NAME)
        val recyclingIcon = BinIcon(BinIcon.RECYCLING_RES, BinIcon.RECYCLING_NAME)
        val gardenIcon = BinIcon(BinIcon.GARDEN_RES, BinIcon.GARDEN_NAME)
        val medicalIcon = BinIcon(BinIcon.MEDICAL_RES, BinIcon.MEDICAL_NAME)
        val industrialIcon = BinIcon(BinIcon.INDUSTRIAL_RES, BinIcon.INDUSTRIAL_NAME)
        val wheelieIcon = BinIcon(BinIcon.WHEELIE_RES, BinIcon.WHEELIE_NAME)
        val wheelieFullIcon = BinIcon(BinIcon.WHEELIE_FULL_RES, BinIcon.WHEELIE_FULL_NAME)
        val trashIcon = BinIcon(BinIcon.TRASH_BAG_RES, BinIcon.TRASH_BAG_NAME)

        return listOf(
            genericIcon, landfillIcon, recyclingIcon,
            gardenIcon, medicalIcon, industrialIcon,
            wheelieIcon, wheelieFullIcon, trashIcon
        )
    }
}