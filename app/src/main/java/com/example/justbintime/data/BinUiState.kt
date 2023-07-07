package com.example.justbintime.data

import java.time.LocalDateTime

class BinUiState(var bwcList: List<BinWithColours> = listOf()) {
    var numBinsDue: Int = 0
    var binDuePhrase: String = "?"

    fun getMainBinStatusPhrase(): String {
        binDuePhrase = BinPhraseGenerator.getPhraseForState(numBinsDue)
        return binDuePhrase
    }

    fun getSortedBins(now: LocalDateTime): List<BinWithColours> {
        bwcList.sortedBy { bwc -> bwc.bin.determineNextCollectionDate(now) }
        return bwcList
    }

    // Count the number of bins which will be collected in the next 24 hours
    fun numBinsCollectedSoon(now: LocalDateTime): Int {
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
        val numBins = numBinsCollectedSoon(now)
        if (numBins == 1) {
            return "$numBins bin collected soon"
        }
        return "$numBins bins collected soon"
    }
}
