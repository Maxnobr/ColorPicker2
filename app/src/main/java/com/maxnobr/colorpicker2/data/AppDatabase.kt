package com.maxnobr.colorpicker2.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(ColorData::class)], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun colorDao(): ColorDao
}