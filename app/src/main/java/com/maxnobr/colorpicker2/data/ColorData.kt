package com.maxnobr.colorpicker2.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Maxnobr on 2/19/2018.
 */

@Entity(primaryKeys = ["color","name"])
data class ColorData(@ColumnInfo var color: Int, @ColumnInfo var name: String)