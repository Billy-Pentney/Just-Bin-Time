package com.example.justbintime.data

import android.content.Context
import android.util.Log
import com.example.justbintime.R
import kotlin.random.Random

class BinPhraseGenerator {
    companion object {
        var binNotDuePhrases: Array<String>? = null
        var binDuePhrases: Array<String>? = null
        var lastChosenIndex: Int = 0

        fun initArrays(context: Context) {
            binDuePhrases = context.resources.getStringArray(R.array.BinDueCollectPhrases)
            binNotDuePhrases = context.resources.getStringArray(R.array.BinNotDuePhrases)
        }

        private fun pickRandomPhraseFromArray(arr: Array<String>?): String {
            if (arr.isNullOrEmpty()) {
                Log.e("BinPhraseGenerator", "Attempt to get phrase from null/empty array")
                return "??"
            }
            val shift = if (arr.size > 1) Random.nextInt(1,arr.size-1)
                        else 1
            // Avoid picking the same index again
            lastChosenIndex = (lastChosenIndex + shift) % arr.size
            return arr[lastChosenIndex]
        }

        fun getPhraseForState(numBinsDue: Int): String {
            return if (numBinsDue > 0)
                pickRandomPhraseFromArray(binDuePhrases)
            else
                pickRandomPhraseFromArray(binNotDuePhrases)
        }
    }
}