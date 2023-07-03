package com.example.justbintime

import android.content.Context
import android.content.res.Resources
import java.time.LocalDateTime
import kotlin.random.Random

class BinPhraseGenerator {
//    companion object {
//        val binNotDuePhrases: Array<String> = Resources.getSystem().getStringArray(R.array.BinNotDuePhrases)
//        val binDuePhrases: Array<String> = Resources.getSystem().getStringArray(R.array.BinDueCollectPhrases)
//    }

    fun getBinDuePhrase(context: Context): String {
        val binDuePhrases = context.resources.getStringArray(R.array.BinDueCollectPhrases)
        if (binDuePhrases.isEmpty())
            return "??"
        val index = Random.nextInt(binDuePhrases.size)
        return binDuePhrases[index]
    }

    fun getBinNotDuePhrase(context: Context): String {
        val binNotDuePhrases = context.resources.getStringArray(R.array.BinNotDuePhrases)
        if (binNotDuePhrases.isEmpty())
            return "??"
        val index = Random.nextInt(binNotDuePhrases.size)
        return binNotDuePhrases[index]
    }

    fun getPhraseForState(context: Context, binState: BinUiState): String {
        val numBinsDue = binState.numBinsCollectedSoon(LocalDateTime.now())
        return if (numBinsDue > 0)
            getBinDuePhrase(context)
        else
            getBinNotDuePhrase(context)
    }

}