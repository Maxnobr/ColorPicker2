package com.maxnobr.colorpicker2

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.maxnobr.colorpicker2.R
import com.maxnobr.colorpicker2.interfaces.ClickListener
import kotlinx.android.synthetic.main.fragment_load.*

/**
 * Created by Maxnobr on 2/16/2018.
 */
class LoadFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_load, container, false)
    }
}