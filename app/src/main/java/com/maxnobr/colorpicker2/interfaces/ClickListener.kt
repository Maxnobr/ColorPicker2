package com.maxnobr.colorpicker2.interfaces

import android.view.View

/**
 * Created by Maxnobr on 2/21/2018.
 */
interface ClickListener {
    fun onClick(view: View, position: Int)
    fun onLongClick(view: View, position: Int)
}