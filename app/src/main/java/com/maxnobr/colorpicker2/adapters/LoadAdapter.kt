package com.maxnobr.colorpicker2.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.maxnobr.colorpicker2.data.ColorData
import android.view.ViewGroup
import android.view.LayoutInflater
import com.maxnobr.colorpicker2.R
import com.maxnobr.colorpicker2.R.layout.item_color


/**
 * Created by Maxnobr on 2/20/2018.
 */
class LoadAdapter(private var colorsList: MutableList<ColorData>) : RecyclerView.Adapter<LoadAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textField = view.findViewById(R.id.itemTextView) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(item_color, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return colorsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder?.textField?.text = colorsList[position].name
        holder?.textField?.setBackgroundColor(colorsList[position].color)
        holder?.textField?.setTextColor(Color.rgb(255-Color.red(colorsList[position].color),255-Color.green(colorsList[position].color),255-Color.blue(colorsList[position].color)))
    }

}