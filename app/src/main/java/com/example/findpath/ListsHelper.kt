package com.example.findpath

import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.findpath.databinding.ActivitySetupBinding

class ListsHelper {
    fun setupListsInit(context: AppCompatActivity, setup: ActivitySetupBinding, list: MutableList<String>) {
        val adapter = ArrayAdapter(context, R.layout.spinner_text, list)
        setup.setupCityList.adapter = adapter
        setup.setupCityListFirstItem.adapter = adapter
        setup.setupCityListSecondItem.adapter = adapter
        setup.setupCityListSecondItem.setSelection(1)
        setup.setupCityNameEdit.setText("")
    }

    fun refreshList(context: AppCompatActivity, setup: ActivitySetupBinding, list: MutableList<String>){
        val adapter = ArrayAdapter(context, R.layout.spinner_text, list)
        setup.setupCityList.adapter = adapter
        setup.setupCityNameEdit.setText("")
    }

    fun generateDefaultList(): MutableList<String> {
        val generatedList: MutableList<String> = mutableListOf()
        for(i in 1..8){
            generatedList.add("City$i")
        }
        return generatedList
    }

    fun fullRefresh(context: AppCompatActivity, setup: ActivitySetupBinding, list: MutableList<String>) {
        val adapter = ArrayAdapter(context, R.layout.spinner_text, list)
        setup.setupCityList.adapter = adapter
        setup.setupCityNameEdit.setText("")
    }
}