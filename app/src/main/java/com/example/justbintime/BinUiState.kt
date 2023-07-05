package com.example.justbintime

import com.example.justbintime.data.Bin
import java.time.LocalDateTime

class BinUiState(var bins: List<Bin> = listOf()) {
    var numBinsDue: Int = 0
    var binDuePhrase: String = "?"

    fun getMainBinStatusPhrase(): String {
        binDuePhrase = BinPhraseGenerator.getPhraseForState(numBinsDue)
        return binDuePhrase
    }

    fun getSortedBins(now: LocalDateTime): List<Bin> {
//        bins.sortedBy { b -> b.determineNextCollectionDate(now) }
        return bins
    }

    // Count the number of bins which will be collected in the next 24 hours
    fun numBinsCollectedSoon(now: LocalDateTime): Int {
        var numBins = 0;
        for (b in bins) {
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
