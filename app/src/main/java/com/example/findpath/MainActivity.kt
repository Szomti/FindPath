package com.example.findpath

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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

        var citiesList = ListsHelper().generateDefaultList()
        var graph: MutableList<MutableList<Int>> = CitiesHelper().distanceInit(citiesList)

        ListsHelper().setupListsInit(this, setup, citiesList)

        setup.setupFinishBtn.setOnClickListener {
            setContentView(main.root)
        }

        main.mainGoToSetup.setOnClickListener {
            ListsHelper().setupListsInit(this, setup, citiesList)
            setContentView(setup.root)
        }

        setup.setupResetNamesBtn.setOnClickListener {
            citiesList = ListsHelper().generateDefaultList()
            shortFullRefresh(setup, citiesList)
        }

        setup.setupResetDistanceBtn.setOnClickListener {
            graph = CitiesHelper().distanceInit(citiesList)
            shortFullRefresh(setup, citiesList)
        }

        setup.setupRandomDistanceBtn.setOnClickListener {
            graph = CitiesHelper().randomDistance(graph)
            shortFullRefresh(setup, citiesList)
        }

        setup.setupSaveCityNameChange.setOnClickListener {
            var correct = true
            val newNameText = setup.setupCityNameEdit.text.toString().trim()
            for(item in citiesList){
                if(item == newNameText) correct = false
            }
            if(newNameText == "") correct = false
            if(correct) {
                citiesList[setup.setupCityList.selectedItemId.toInt()] = newNameText
                Toast.makeText(applicationContext, "Saved New Name: $newNameText", Toast.LENGTH_SHORT).show()
                shortFullRefresh(setup, citiesList)
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
                    val distanceInput = setup.setupDistanceBetweenCitiesChange
                    val secondId = setup.setupCityListSecondItem.selectedItemId
                    distanceInput.isEnabled = id != secondId
                    if(id != secondId){
                        distanceInput.setText(graph[secondId.toInt()][id.toInt()].toString())
                    }else{
                        distanceInput.setText("-1")
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
                    val distanceInput = setup.setupDistanceBetweenCitiesChange
                    val firstId = setup.setupCityListFirstItem.selectedItemId
                    distanceInput.isEnabled = id != firstId
                    if(id != firstId){
                        distanceInput.setText(graph[firstId.toInt()][id.toInt()].toString())
                    }else{
                        distanceInput.setText("-1")
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) { }
            }

        setup.setupSaveCityDistanceChange.setOnClickListener {
            val distance = setup.setupDistanceBetweenCitiesChange.text.toString().toInt()
            if(distance > 0){
                val firstIdPlusOne = setup.setupCityListFirstItem.selectedItemId.toInt()
                val secondIdPlusOne = setup.setupCityListSecondItem.selectedItemId.toInt()
                graph[firstIdPlusOne][secondIdPlusOne] = distance
                graph[secondIdPlusOne][firstIdPlusOne] = distance
                println(graph)
                Toast.makeText(applicationContext, "Saved New Distance: $distance", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Incorrect Distance - Not Saved", Toast.LENGTH_SHORT).show()
            }
        }

        main.mainFindPath.setOnClickListener {
            var resultPathString = ""
            val resultPath = SimpleTSP(graph).findPath()
            resultPathString += "\nDistance: ${resultPath.getDistance()}\n\n"
            for((i, index) in resultPath.getPath().withIndex()){
                var endStr = "\n\u142F\n"
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

    private fun shortFullRefresh(setup: ActivitySetupBinding, citiesList:MutableList<String>){
        ListsHelper().fullRefresh(
            this,
            setup,
            citiesList,
            SpinnersSelectedId(
                setup.setupCityList.selectedItemId,
                setup.setupCityListFirstItem.selectedItemId,
                setup.setupCityListSecondItem.selectedItemId,
            ),
        )
    }
}