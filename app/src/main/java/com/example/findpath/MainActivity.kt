package com.example.findpath

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        val graph: MutableList<MutableList<Int>> = graphDistanceInit(citiesList.size)

        setupListsInit(setup, citiesList)

        setup.setupFinishBtn.setOnClickListener {
            setContentView(main.root)
        }

        main.mainGoToSetup.setOnClickListener {
            setContentView(setup.root)
        }

        setup.setupSaveCityNameChange.setOnClickListener {
            var correct = true
            for(item in citiesList){
                if(item == setup.setupCityNameEdit.text.toString()) correct = false
            }
            if(correct) {
                citiesList[setup.setupCityList.selectedItemId.toInt()] = setup.setupCityNameEdit.text.toString()
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
                        setup.setupDistanceBetweenCitiesChange.setText("")
                    }else{
                        setup.setupDistanceBetweenCitiesChange.setText("-1")
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) { }
            }

        setup.setupSaveCityDistanceChange.setOnClickListener {
            graph[setup.setupCityListFirstItem.selectedItemId.toInt()+1][setup.setupCityListSecondItem.selectedItemId.toInt()+1] = setup.setupDistanceBetweenCitiesChange.text.toString().toInt()
            graph[setup.setupCityListSecondItem.selectedItemId.toInt()+1][setup.setupCityListFirstItem.selectedItemId.toInt()+1] = setup.setupDistanceBetweenCitiesChange.text.toString().toInt()
            println(graph)
        }

        main.mainFindPath.setOnClickListener {
            TSP(graph).shortestDistance()
        }
    }

    private fun graphDistanceInit(size: Int): MutableList<MutableList<Int>> {
        val correctedSize = size + 1
        val tempGraph: MutableList<MutableList<Int>> = MutableList(correctedSize) {MutableList(correctedSize) {500}}
        for (i in 0 until correctedSize){
            tempGraph[0][i] = 0
            tempGraph[i][0] = 0
            tempGraph[i][i] = 0
        }
        println(tempGraph)
        return tempGraph
    }

    private fun setupListsInit(setup: ActivitySetupBinding, list: MutableList<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner_text, list)
        setup.setupCityList.adapter = adapter
        setup.setupCityListFirstItem.adapter = adapter
        setup.setupCityListSecondItem.adapter = adapter
        setup.setupCityListSecondItem.setSelection(1)
    }

    private fun generateDefaultList(): MutableList<String> {
        val generatedList: MutableList<String> = mutableListOf()
        for(i in 1..8){
            generatedList.add("City$i")
        }
        return generatedList
    }
}