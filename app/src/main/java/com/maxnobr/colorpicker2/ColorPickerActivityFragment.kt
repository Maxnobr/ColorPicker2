package com.maxnobr.colorpicker2

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_color_picker.*
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class ColorPickerActivityFragment : android.support.v4.app.Fragment() {


    private var redColor:Int = 0
    private var greenColor:Int = 0
    private var blueColor:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_color_picker, container, false)
    }

    fun getColors(): Int
    {
        return Color.rgb(redColor,greenColor,blueColor)
    }

    fun setColors(color : Int)
    {
        redColor = Color.red(color)
        greenColor = Color.green(color)
        blueColor = Color.blue(color)
        changeColor()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        randColor()

        redText.setOnClickListener {
            val x = checkVal(redText)
            if(x != -1) {
                redColor = x }
            changeColor()
        }
        greenText.setOnClickListener {
            val x = checkVal(greenText)
            if(x != -1) {
                greenColor = x }
            changeColor()
        }
        blueText.setOnClickListener {
            val x = checkVal(blueText)
            if(x != -1) {
                blueColor = x }
            changeColor()
        }

        surfaceView.setOnClickListener{randColor()}

        redSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                redColor = progress
                changeColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        greenSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                greenColor = progress
                changeColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        blueSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                blueColor = progress
                changeColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun changeColor()
    {
        redText.setText(redColor.toString())
        greenText.setText(greenColor.toString())
        blueText.setText(blueColor.toString())

        redSeekBar.progress = redColor
        greenSeekBar.progress = greenColor
        blueSeekBar.progress = blueColor

        surfaceView.setBackgroundColor(Color.rgb(redColor,greenColor,blueColor))
    }

    private fun checkVal(eText: EditText):Int
    {
        return try{
            val y = Integer.parseInt(eText.text.toString())
            if(y in 0..255) y
            else -1}
        catch( e:Exception){-1}
    }

    private fun randColor()
    {
        val rnd = Random()
        redColor = rnd.nextInt(255)
        greenColor = rnd.nextInt(255)
        blueColor = rnd.nextInt(255)
        changeColor()
    }
}
