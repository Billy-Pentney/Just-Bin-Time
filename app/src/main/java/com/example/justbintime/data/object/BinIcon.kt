package com.example.justbintime.data.`object`

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.justbintime.ui.theme.BinLandfillColorLight

@Entity(tableName = "bin_icons")
data class BinIcon(
    @ColumnInfo val drawableResStr: String,
    @ColumnInfo var drawableName: String,
    @PrimaryKey (autoGenerate = true) val iconId: Int = 0
) {
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
                GENERIC_NAME        -> GENERIC_RES
                LANDFILL_NAME       -> LANDFILL_RES
                RECYCLING_NAME      -> RECYCLING_RES
                GARDEN_NAME         -> GARDEN_RES
                INDUSTRIAL_NAME     -> INDUSTRIAL_RES
                MEDICAL_NAME        -> MEDICAL_RES
                WHEELIE_NAME        -> WHEELIE_RES
                WHEELIE_FULL_NAME   -> WHEELIE_FULL_RES
                TRASH_BAG_NAME      -> TRASH_BAG_RES
                else -> null
            }
        }

        fun resourceStringToName(resourceString: String): String? {
            return when (resourceString) {
                GENERIC_RES        -> GENERIC_NAME
                LANDFILL_RES       -> LANDFILL_NAME
                RECYCLING_RES      -> RECYCLING_NAME
                GARDEN_RES         -> GARDEN_NAME
                INDUSTRIAL_RES     -> INDUSTRIAL_NAME
                MEDICAL_RES        -> MEDICAL_NAME
                WHEELIE_RES        -> WHEELIE_NAME
                WHEELIE_FULL_RES   -> WHEELIE_FULL_NAME
                TRASH_BAG_RES      -> TRASH_BAG_NAME
                else -> null
            }
        }

        fun getDrawableResourceId(context: Context, drawableResStr: String): Int? {
            return try {
                context.resources.getIdentifier(drawableResStr, "drawable", context.packageName)
            }
            catch (ex: Resources.NotFoundException) {
                null
            }
        }

        fun makeDefault(): BinIcon {
            return BinIcon(GENERIC_RES, GENERIC_NAME)
        }

        const val GENERIC_RES = "bin_generic"
        const val LANDFILL_RES = "bin_landfill"
        const val RECYCLING_RES = "bin_recycling"
        const val GARDEN_RES = "bin_garden"
        const val INDUSTRIAL_RES = "bin_industrial"
        const val MEDICAL_RES = "bin_medical"
        const val WHEELIE_RES = "bin_wheelie"
        const val WHEELIE_FULL_RES = "bin_wheelie_full"
        const val TRASH_BAG_RES = "bin_trash"

        const val GENERIC_NAME = "Generic"
        const val LANDFILL_NAME = "Landfill"
        const val RECYCLING_NAME = "Recycling"
        const val GARDEN_NAME = "Garden"
        const val MEDICAL_NAME = "Medical"
        const val INDUSTRIAL_NAME = "Industrial"
        const val WHEELIE_NAME = "Wheelie Bin"
        const val WHEELIE_FULL_NAME = "Wheelie Bin (Full)"
        const val TRASH_BAG_NAME = "Trash Bag"

        private val DRAWABLE_RES_LIST = listOf(
            GENERIC_RES, LANDFILL_RES, RECYCLING_RES, GARDEN_RES, INDUSTRIAL_RES, MEDICAL_RES,
            WHEELIE_RES, WHEELIE_FULL_RES, TRASH_BAG_RES
        )
        val NAME_LIST = listOf(
            GENERIC_NAME, LANDFILL_NAME, RECYCLING_NAME, GARDEN_NAME, INDUSTRIAL_NAME, MEDICAL_NAME,
            WHEELIE_NAME, WHEELIE_FULL_NAME, TRASH_BAG_NAME
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
