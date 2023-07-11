package com.example.justbintime.data

import android.content.Context
import android.util.Log
import com.example.justbintime.R
import kotlin.random.Random

class BinPhraseGenerator {
    companion object {
        var binNotDuePhrases: Array<String>? = null
        var binDuePhrases: Array<String>? = null

        fun initArrays(context: Context) {
            binDuePhrases = context.resources.getStringArray(R.array.BinDueCollectPhrases)
            binNotDuePhrases = context.resources.getStringArray(R.array.BinDueCollectPhrases)
        }

        private fun pickRandomPhraseFromArray(arr: Array<String>?): String {
            return if (arr.isNullOrEmpty()) {
                Log.e("BinPhraseGenerator", "Attempt to get phrase from null/empty array")
                "??"
            } else {
                arr[Random.nextInt(arr.size)]
            }
        }

        fun getPhraseForState(numBinsDue: Int): String {
            return if (numBinsDue > 0)
                pickRandomPhraseFromArray(binDuePhrases)
            else
                pickRandomPhraseFromArray(binNotDuePhrases)
        }
    }
}