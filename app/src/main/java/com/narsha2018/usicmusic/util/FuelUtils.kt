package com.narsha2018.usicmusic.util

import android.content.Context
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import es.dmoral.toasty.Toasty

class FuelUtils (private val c: Context){
    init {
        Toasty.Config.reset()
    }
    fun doLogin(url : String, data : Any) : Any{
        val gson = Gson()
        val json : String = gson.toJson(data)

        "http://10.80.162.221:3000"+url.httpPost().body(json).responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Toasty.error(c, "FAILED").show()
                }
                is Result.Success -> {
                    Toasty.success(c, "SUCCESS ${result.get()}").show()
                }
            }
        }
        return Any()
    }
}