package com.example.findpath

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.findpath.databinding.ActivityMainBinding
import com.example.findpath.databinding.ActivitySetupBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setup = ActivitySetupBinding.inflate(layoutInflater)
        val main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(setup.root)

        val citiesList = generateDefaultList()
        var graph: MutableList<MutableList<Int>> = CitiesHelper().distanceInit(citiesList)

        setupListsInit(setup, citiesList)

        setup.setupFinishBtn.setOnClickListener {
            setContentView(main.root)
        }

        main.mainGoToSetup.setOnClickListener {
            setupListsInit(setup, citiesList)
            setContentView(setup.root)
        }

        setup.setupResetBtn.setOnClickListener {
            graph = CitiesHelper().distanceInit(citiesList)
        }

        setup.setupRandomBtn.setOnClickListener {
            graph = CitiesHelper().randomDistance(graph)
        }

        setup.setupSaveCityNameChange.setOnClickListener {
            var correct = true
            for(item in citiesList){
                if(item == setup.setupCityNameEdit.text.toString()) correct = false
            }
            if(setup.setupCityNameEdit.text.toString().trim() == "") correct = false
            if(correct) {
                citiesList[setup.setupCityList.selectedItemId.toInt()] = setup.setupCityNameEdit.text.toString()
                Toast.makeText(applicationContext, "Saved New Name: ${setup.setupCityNameEdit.text}", Toast.LENGTH_SHORT).show()
                refreshList(setup, citiesList)
            }
        }

        setup.setupCityListFirstItem.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    setup.setupDistanceBetweenCitiesChange.isEnabled = id != setup.setupCityListSecondItem.selectedItemId
                    if(id != setup.setupCityListSecondItem.selectedItemId){
                        setup.setupDistanceBetweenCitiesChange.setText(graph[setup.setupCityListSecondItem.selectedItemId.toInt()+1][setup.setupCityListFirstItem.selectedItemId.toInt()+1].toString())
                    }else{
                        setup.setupDistanceBetweenCitiesChange.setText("-1")
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) { }
            }

        setup.setupCityListSecondItem.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    setup.setupDistanceBetweenCitiesChange.isEnabled = id != setup.setupCityListFirstItem.selectedItemId
                    if(id != setup.setupCityListFirstItem.selectedItemId){
                        setup.setupDistanceBetweenCitiesChange.setText(graph[setup.setupCityListFirstItem.selectedItemId.toInt()+1][setup.setupCityListSecondItem.selectedItemId.toInt()+1].toString())
                    }else{
                        setup.setupDistanceBetweenCitiesChange.setText("-1")
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) { }
            }

        setup.setupSaveCityDistanceChange.setOnClickListener {
            if(setup.setupDistanceBetweenCitiesChange.text.toString().toInt() > 0){
                graph[setup.setupCityListFirstItem.selectedItemId.toInt()+1][setup.setupCityListSecondItem.selectedItemId.toInt()+1] = setup.setupDistanceBetweenCitiesChange.text.toString().toInt()
                graph[setup.setupCityListSecondItem.selectedItemId.toInt()+1][setup.setupCityListFirstItem.selectedItemId.toInt()+1] = setup.setupDistanceBetweenCitiesChange.text.toString().toInt()
                println(graph)
                Toast.makeText(applicationContext, "Saved New Distance: ${setup.setupDistanceBetweenCitiesChange.text}", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Incorrect Distance - Not Saved", Toast.LENGTH_SHORT).show()
            }
        }

        main.mainFindPath.setOnClickListener {
            var resultPathString = ""
            val resultPath = SimpleTSP(graph).findPath()
            resultPathString += "\nDistance: ${resultPath.getDistance()}\n\n"
            for((i, index) in resultPath.getPath().withIndex()){
                var endStr = "\n\\/\n"
                println(index)
                if(i + 1 == resultPath.getPath().size) endStr = "\n"
                resultPathString += "${citiesList[index]}$endStr"
            }
            main.mainPathResult.text = resultPathString
        }

        main.mainSave.setOnClickListener {
            val screenshot = Screenshot().takeScreenshotOfView(main.root, main.root.height, main.root.width)
            val screenshotTime = System.currentTimeMillis()
            Screenshot().saveImage(screenshot, screenshotTime.toString(), contentResolver)
        }
    }

    private fun setupListsInit(setup: ActivitySetupBinding, list: MutableList<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner_text, list)
        setup.setupCityList.adapter = adapter
        setup.setupCityListFirstItem.adapter = adapter
        setup.setupCityListSecondItem.adapter = adapter
        setup.setupCityListSecondItem.setSelection(1)
        setup.setupCityNameEdit.setText("")
    }

    private fun refreshList(setup: ActivitySetupBinding, list: MutableList<String>){
        val adapter = ArrayAdapter(this, R.layout.spinner_text, list)
        setup.setupCityList.adapter = adapter
        setup.setupCityNameEdit.setText("")
    }

    private fun generateDefaultList(): MutableList<String> {
        val generatedList: MutableList<String> = mutableListOf()
        for(i in 1..8){
            generatedList.add("City$i")
        }
        return generatedList
    }
}