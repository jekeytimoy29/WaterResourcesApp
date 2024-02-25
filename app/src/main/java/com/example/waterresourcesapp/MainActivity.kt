package com.example.waterresourcesapp

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.example.waterresourcesapp.databinding.ActivityMainBinding
import com.example.waterresourcesapp.model.WaterResource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var waterResourcesList: MutableList<WaterResource>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        waterResourcesList = mutableListOf(
            WaterResource("River Ganges", "Varanasi, India", "River", 1007.25),
            WaterResource("Lake Michigan", "Chicago, Illinois, USA", "Lake", 2147.23),
            WaterResource("Nile River", "Cairo, Egypt", "Canal", 3098.21),
            WaterResource("Colorado River", "Grand Canyon, Arizona, USA", "Reservoir", 2147.23),
            WaterResource("Lake Baikal", "Siberia, Russia", "Pond", 3098.21),
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayTableHeaders()
        displayTableRows()
    }

    private fun displayTableHeaders() {
        val row = TableRow(this)
        row.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val waterResourceName = createTextView(getString(R.string.water_resource_name))
        waterResourceName.setTypeface(null, Typeface.BOLD)
        setSolidBorders(waterResourceName)
        val location = createTextView(getString(R.string.location_title))
        location.setTypeface(null, Typeface.BOLD)
        setSolidBorders(location)
        val type = createTextView(getString(R.string.type_title))
        type.setTypeface(null, Typeface.BOLD)
        setSolidBorders(type)
        val capacity = createTextView(getString(R.string.capacity_title))
        capacity.setTypeface(null, Typeface.BOLD)
        setSolidBorders(capacity)

        row.addView(waterResourceName)
        row.addView(location)
        row.addView(type)
        row.addView(capacity)

        binding.tableWaterResources.addView(row)
    }

    private fun setSolidBorders(view: View) {
        view.setBackgroundResource(R.drawable.border)
    }

    private fun addTableRow(waterResource: WaterResource) {
        val row = TableRow(this)

        val waterResourceName = createTextView(waterResource.name)
        setSolidBorders(waterResourceName)
        val location = createTextView(waterResource.location)
        setSolidBorders(location)
        val type = createTextView(waterResource.type)
        setSolidBorders(type)
        val capacity = createTextView(waterResource.capacity.toString())
        setSolidBorders(capacity)

        row.addView(waterResourceName)
        row.addView(location)
        row.addView(type)
        row.addView(capacity)

        binding.tableWaterResources.addView(row)
    }

    private fun displayTableRows() {
        binding.tableWaterResources.removeViews(1, binding.tableWaterResources.childCount - 1)
        for (waterResource in waterResourcesList) {
            addTableRow(waterResource)
        }
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimensionPixelSize(R.dimen.text_size).toFloat()
        )
        textView.setPadding(14, 14, 14, 14)
        textView.gravity = Gravity.CENTER_VERTICAL

        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT
        )

        textView.layoutParams = params

        return textView
    }

    fun addButtonOnClick(view: View) {
        val waterResourceName = binding.editResourceName.text.toString()
        val location = binding.editLocation.text.toString()
        val type = binding.editType.text.toString()
        var capacity = binding.editCapacity.text.toString()

        if (waterResourceName.isBlank()){
            displayToast(getString(R.string.error_text_toast, getString(R.string.water_resource_name)))
            return
        }

        if(waterResourcesList.any { it.name.equals(waterResourceName, true) }) {
            displayToast(getString(R.string.error_already_exists, waterResourceName))
            return
        }

        if (location.isBlank()){
            displayToast(getString(R.string.error_text_toast, getString(R.string.location_title)))
            return
        }

        if (type.isBlank()){
            displayToast(getString(R.string.error_text_toast, getString(R.string.type_title)))
            return
        }

        if(capacity.isBlank()){
            capacity = "0.0"
        }

        val waterResource = WaterResource(
            waterResourceName,
            location,
            type,
            capacity.toDouble())

        addTableRow(waterResource)
        waterResourcesList.add(waterResource)
        displayToast(getString(R.string.resource_added_successfully, waterResourceName))
    }

    private fun displayToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val jsonResource = Gson().toJson(waterResourcesList)
        outState.putString("waterResourcesList", jsonResource)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if(savedInstanceState != null){
            val jsonString = savedInstanceState.getString("waterResourcesList")
            val type = object : TypeToken<MutableList<WaterResource>>() {}.type
            waterResourcesList = Gson().fromJson<MutableList<WaterResource>>(jsonString, type)
        }
        displayTableRows()
    }
}