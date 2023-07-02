package com.example.justbintime

import java.time.LocalDateTime

class BinUiState {
    private val bins = ArrayList<Bin>()

    fun addBin(bin: Bin) {
        bins.add(bin)
    }

    fun getSortedBins(now: LocalDateTime): ArrayList<Bin> {
        bins.sortBy { b -> b.getNextCollectionDate(now) }
        return bins
    }
}
