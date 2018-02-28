package com.maxnobr.colorpicker2

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxnobr.colorpicker2.R
import kotlinx.android.synthetic.main.fragment_save.*

/**
 * Created by Maxnobr on 2/13/2018.
 */
class SaveFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save,
                container, false)
    }

    private var activityCallback: SaveListener? = null

    interface SaveListener {
        fun onSaveClick(name: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as SaveListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement ToolbarListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        save_button.setOnClickListener {
            if(editText.text.isNotBlank()) {
                activityCallback?.onSaveClick(editText.text.toString())
                editText.text.clear()
            }
        }
    }
}