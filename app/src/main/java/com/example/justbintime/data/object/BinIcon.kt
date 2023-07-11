package com.example.justbintime.data.`object`

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bin_icons")
data class BinIcon(
    @PrimaryKey (autoGenerate = true) val iconId: Int = 0,
    @ColumnInfo val drawableResStr: String
) {
    companion object {
        const val GENERIC_RES = "bin_generic"
        const val LANDFILL_RES = "bin_landfill"
        const val RECYCLING_RES = "bin_recycling"
        const val GARDEN_RES = "bin_garden"
        const val INDUSTRIAL_RES = "bin_industrial"
        const val MEDICAL_RES = "bin_medical"
    }
}
