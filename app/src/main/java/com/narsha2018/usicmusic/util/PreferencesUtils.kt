package com.narsha2018.usicmusic.util

import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE

class PreferencesUtils(c: Context){
    private var pref : SharedPreferences? = null
    init {
        pref = c.getSharedPreferences("pref", MODE_PRIVATE)
    }
    fun saveData(key : String , value: String){
        pref!!.edit().apply{
            putString(key, value)
            apply()
        }
    }
    fun getData(key : String) : String{
        return pref!!.getString(key, "")
    }
}