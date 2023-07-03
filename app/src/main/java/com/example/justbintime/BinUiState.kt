package com.example.justbintime

import android.os.LocaleList
import java.time.LocalDateTime

class BinUiState {
    private val bins = ArrayList<Bin>()

    fun addBin(bin: Bin) {
        bins.add(bin)
    }

    fun getSortedBins(now: LocalDateTime): ArrayList<Bin> {
        bins.sortBy { b -> b.determineNextCollectionDate(now) }
        return bins
    }

    fun getBinStatus(now: LocalDateTime): String {
        if (numBinsCollectedSoon(now) > 0) return "It's BIN time!"
        return "All clear!"
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
