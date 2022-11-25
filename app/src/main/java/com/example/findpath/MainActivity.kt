package com.example.findpath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.findpath.databinding.ActivityMainBinding
import com.example.findpath.databinding.ActivitySetupBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setup = ActivitySetupBinding.inflate(layoutInflater)
        val main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(setup.root)

        val citiesList = generateDefaultList()

        setupListsInit(setup, citiesList)

        setup.setupFinishBtn.setOnClickListener {
            setContentView(main.root)
        }
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
        for(i in 1..15){
            generatedList.add("City$i")
        }
        return generatedList
    }
}