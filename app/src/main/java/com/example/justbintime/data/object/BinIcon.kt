package com.example.justbintime.data.`object`

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.justbintime.ui.theme.BinLandfillColorLight

@Entity(tableName = "bin_icons")
data class BinIcon(
    @PrimaryKey (autoGenerate = true) val iconId: Int = 0,
    @ColumnInfo val drawableResStr: String,
    @ColumnInfo var drawableName: String = "None"
) {
    init {
        if (MAP_RES_TO_NAME[drawableResStr] != null) {
            drawableName = MAP_RES_TO_NAME[drawableResStr]!!
        }
    }

    companion object {
        fun getIconIndex(iconResStr: String): Int {
            for (i in 0..DRAWABLE_RES_LIST.size) {
                val drawableStr = DRAWABLE_RES_LIST[i]
                if (iconResStr == drawableStr) {
                    return i
                }
            }
            return 0
        }

        fun nameToResourceString(iconName: String): String? {
            return when (iconName) {
                GENERIC_NAME -> GENERIC_RES
                LANDFILL_NAME -> LANDFILL_RES
                RECYCLING_NAME -> RECYCLING_RES
                GARDEN_NAME -> GARDEN_RES
                INDUSTRIAL_NAME -> INDUSTRIAL_RES
                MEDICAL_NAME -> MEDICAL_RES
                else -> null
            }
        }

        const val GENERIC_RES = "bin_generic"
        const val LANDFILL_RES = "bin_landfill"
        const val RECYCLING_RES = "bin_recycling"
        const val GARDEN_RES = "bin_garden"
        const val INDUSTRIAL_RES = "bin_industrial"
        const val MEDICAL_RES = "bin_medical"

        const val GENERIC_NAME = "Generic"
        const val LANDFILL_NAME = "Landfill"
        const val RECYCLING_NAME = "Recycling"
        const val GARDEN_NAME = "Garden"
        const val MEDICAL_NAME = "Medical"
        const val INDUSTRIAL_NAME = "Industrial"

        val MAP_RES_TO_NAME = mapOf(
            GENERIC_RES     to GENERIC_NAME,
            LANDFILL_RES    to LANDFILL_NAME,
            RECYCLING_RES   to RECYCLING_NAME,
            GARDEN_RES      to GARDEN_NAME,
            INDUSTRIAL_RES  to INDUSTRIAL_NAME,
            MEDICAL_RES     to MEDICAL_NAME
        )

        val DRAWABLE_RES_LIST = listOf(
            GENERIC_RES, LANDFILL_RES, RECYCLING_RES, GARDEN_RES, INDUSTRIAL_RES, MEDICAL_RES
        )
        val NAME_LIST = listOf(
            GENERIC_NAME, LANDFILL_NAME, RECYCLING_NAME, GARDEN_NAME, INDUSTRIAL_NAME, MEDICAL_NAME
        )
    }

    fun getIconId(context: Context): Int? {
        return try {
            // Attempt to retrieve the resource ID for this bin's icon
            Log.e("DisplayableBin", "Try to load resource with string \"${drawableResStr}\"")
            context.resources.getIdentifier(drawableResStr, "drawable", context.packageName)
        } catch (ex: Resources.NotFoundException) {
            Log.e("DisplayableBin", "Cannot load resource with string \"${drawableResStr}\"")
            null
        }
    }
}
