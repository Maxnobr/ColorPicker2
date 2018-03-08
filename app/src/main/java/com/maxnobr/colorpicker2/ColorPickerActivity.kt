package com.maxnobr.colorpicker2

import android.app.Activity
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.maxnobr.colorpicker2.adapters.LoadAdapter
import com.maxnobr.colorpicker2.data.AppDatabase
import com.maxnobr.colorpicker2.data.ColorData
import com.maxnobr.colorpicker2.interfaces.ClickListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_color_picker.*
import kotlinx.android.synthetic.main.fragment_load.*
import kotlinx.android.synthetic.main.fragment_save.*
import android.content.Intent
import android.net.Uri


class ColorPickerActivity : AppCompatActivity(), SaveFragment.SaveListener {

    private lateinit var colorFrag: ColorPickerActivityFragment
    private lateinit var saveFrag: SaveFragment
    private lateinit var loadFrag: LoadFragment
    private lateinit var db: AppDatabase
    private var allColors: MutableList<ColorData> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: LoadAdapter
    private var selectedView:View? = null
    private var selectedData:ColorData? = null
    private var initialLoaded = false
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.frame_layout, ColorPickerActivityFragment(), "colorPickFrag")
                    .add(R.id.frame_layout, SaveFragment(), "saveFrag")
                    .add(R.id.frame_layout, LoadFragment(), "loadFrag")
                    .commit()

            db = Room.databaseBuilder(applicationContext,
                    AppDatabase::class.java, "stuff").build()

            Log.i("SashaLog","on create loading colors ! ")

            db.colorDao().getAllColors()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { if(!initialLoaded){initialLoaded = true ; allColors = it}}
        }
    }

    override fun onSaveClick(name: String) {
        closeSave(false)
        val newColor = ColorData(colorFrag.getColors(), name)
        Log.i("SashaLog","Saving color '${newColor.color}' by the name '${newColor.name}'")
        allColors.add(newColor)
        mAdapter.notifyDataSetChanged()

        Single.fromCallable {
            db.colorDao().insertColor(newColor)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        //updateColors()
        //displayAllColors()
    }

    fun onLoadColor(view:View) {
        if(selectedData != null)
        {
            colorFrag.setColors((selectedData as ColorData).color)
            closeLoad(false)
        }
    }

    fun onDeleteColor(view:View) {
        if(selectedData != null) {
            selectedView?.findViewById<ImageView>(R.id.check_mark)?.visibility = View.GONE
            val colorToDelete = selectedData as ColorData
            Log.i("SashaLog","Deleting color '${colorToDelete.color}' by the name '${colorToDelete.name}'")
            Log.i("SashaLog","all colors have this color : '${allColors.contains(colorToDelete)}'")

            allColors.remove(colorToDelete)
            mAdapter.notifyDataSetChanged()
            selectedData = null
            selectedView = null

            Single.fromCallable {
                db.colorDao().deleteColor(colorToDelete)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
        }
    }

    private fun updateColors() {
        //mAdapter.notifyDataSetChanged()
        /*
        db.colorDao().getAllColors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { allColors = it; mAdapter.notifyDataSetChanged(); displayAllColors()}*/
    }

    private fun displayAllColors() {
        if(allColors.isEmpty()) {
            Log.i("sashaLog","no colors have been loaded !")
        }
        else for(color in allColors)
        {
            Log.i("sashaLog","display: name is: '${color.name}' and color is: ${color.color}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_color_picker, menu)
        if(intent.action == "msud.cs3013.ACTION_COLOR") menu.getItem(2).isVisible = true

        colorFrag = supportFragmentManager.findFragmentByTag("colorPickFrag") as ColorPickerActivityFragment
        saveFrag = supportFragmentManager.findFragmentByTag("saveFrag") as SaveFragment
        loadFrag = supportFragmentManager.findFragmentByTag("loadFrag") as LoadFragment

        recyclerView = loadFrag.recView
        mAdapter = LoadAdapter(allColors)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter

        recyclerView.addOnItemTouchListener(RecyclerTouchListener(applicationContext, recyclerView,object: ClickListener {
            override fun onClick(view: View, position: Int) {

                selectedView?.findViewById<ImageView>(R.id.check_mark)?.visibility = View.GONE
                if(selectedData?.name != allColors[position].name || selectedData?.color != allColors[position].color) {
                    selectedData = allColors[position]
                    selectedView = view
                    view.findViewById<ImageView>(R.id.check_mark)?.visibility = View.VISIBLE
                }
                else {
                    selectedData = null
                    selectedView = null
                }
            }
            override fun onLongClick(view: View, position: Int) {}
        }))

        supportFragmentManager.beginTransaction()
                .hide(loadFrag)
                .hide(saveFrag)
                .commit()

        //updateColors()
        return true
    }

    fun closeSave(view: View) {
        closeSave(false)
    }

    private fun closeSave(slideDown: Boolean) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_top, if (slideDown) R.anim.abc_slide_out_bottom; else R.anim.abc_slide_out_top)
                .hide(saveFrag)
                .commit()
        hideKeyboard()
    }

    fun closeLoad(view: View) {
        closeLoad(false)
    }

    private fun closeLoad(slideDown: Boolean) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_top, if (slideDown) R.anim.abc_slide_out_bottom; else R.anim.abc_slide_out_top)
                .hide(loadFrag)
                .commit()
    }

    private fun openSave() {
        if (!saveFrag.isVisible) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_top)
                    .show(saveFrag)
                    .commit()
            editText.requestFocus()
        }
        else
            closeSave(false)
        closeLoad(true)
    }

    private fun openLoad() {
        if (!loadFrag.isVisible) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_top)
                    .show(loadFrag)
                    .commit()
            //updateColors()
        }
        else
            closeLoad(false)
        closeSave(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                openSave()
                true
            }
            R.id.action_load -> {
                openLoad()
                //displayAllColors()
                true
            }
            R.id.action_return -> {
                returnColor()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) view = View(applicationContext)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun returnColor()
    {
        val result = Intent("com.example.RESULT_ACTION", Uri.parse(colorFrag.getColors().toString()))
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}