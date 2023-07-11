package com.example.justbintime.data

import java.time.LocalDateTime

class BinUiState(
    var bwcList: List<DisplayableBin> = listOf(),
    var numBinsDue: Int = 0,
    var binDuePhrase: String? = null
) {
    init {
        numBinsDue = countNumBinsCollectedSoon(LocalDateTime.now())
    }

    fun copy(newBwcList: List<DisplayableBin> = bwcList): BinUiState {
        return BinUiState(newBwcList, numBinsDue, binDuePhrase)
    }

    fun getMainBinStatusPhrase(): String {
        if (binDuePhrase == null) {
            binDuePhrase = BinPhraseGenerator.getPhraseForState(numBinsDue)
        }
        return binDuePhrase ?: "?"
    }

    fun getSortedBins(now: LocalDateTime): List<DisplayableBin> {
        bwcList.sortedBy { bwc -> bwc.bin.determineNextCollectionDate(now) }
        return bwcList
    }

    // Count the number of bins which will be collected in the next 24 hours
    private fun countNumBinsCollectedSoon(now: LocalDateTime): Int {
        var numBins = 0;
        for (bwc in bwcList) {
            val b = bwc.bin
            b.determineNextCollectionDate(now)
            if (b.isCollectionImminent(now)) {
                numBins += 1
            }
        }
        return numBins
    }

    fun getNumBinsToBeCollectedSoonText(now: LocalDateTime): String {
        val numBins = countNumBinsCollectedSoon(now)
        if (numBins == 1) {
            return "$numBins bin collected soon"
        }
        return "$numBins bins collected soon"
    }

    fun update(bwcList: List<DisplayableBin>) {
        this.bwcList = bwcList
        numBinsDue = countNumBinsCollectedSoon(LocalDateTime.now())
        binDuePhrase = BinPhraseGenerator.getPhraseForState(numBinsDue)
    }
}
