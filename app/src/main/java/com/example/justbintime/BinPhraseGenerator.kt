package com.example.justbintime

import android.content.Context
import android.content.res.Resources
import com.example.justbintime.data.Bin
import java.time.LocalDateTime
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