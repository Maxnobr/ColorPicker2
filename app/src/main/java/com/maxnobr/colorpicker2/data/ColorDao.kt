package com.maxnobr.colorpicker2.data

import android.arch.persistence.room.*
import io.reactivex.Flowable


/**
* Created by Maxnobr on 2/19/2018.
*/

@Dao
interface ColorDao {

    //@Query("SELECT * FROM ColorData WHERE id IN (:userIds)")
    //fun loadAllByIds(userIds: IntArray): List<ColorData>

    @Query("select * from ColorData")
    fun getAllColors(): Flowable<MutableList<ColorData>>

    //@Query("select * from ColorData where id = id")
    //fun findColorById(id: Int): ColorData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertColor(color: ColorData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateColor(color: ColorData)

    @Delete
    fun deleteColor(color: ColorData)
}