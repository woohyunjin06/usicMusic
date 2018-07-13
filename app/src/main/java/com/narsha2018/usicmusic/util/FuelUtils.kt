package com.narsha2018.usicmusic.util

import android.content.Context
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.narsha2018.usicmusic.activity.LoginActivity
import com.narsha2018.usicmusic.activity.RegisterActivity
import com.narsha2018.usicmusic.model.AccountResponse
import es.dmoral.toasty.Toasty

class FuelUtils (private val c: Context){
    init {
        Toasty.Config.reset()
        FuelManager.instance.basePath = "http://10.80.162.221:3000"
    }
    fun postData(url : String, data : Any) {
        val gson = Gson()
        var json : String = gson.toJson(data)
        var resultJson : Any
        resultJson = if(url.contains("auth"))
            AccountResponse(600, "", "")
        else
            AccountResponse(600, "", "")
        url.httpPost().body(json, Charsets.UTF_8).header("Content-Type" to "application/json").responseJson { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    if(url.contains("login")) {
                        (c as LoginActivity).notifyFinish(resultJson as AccountResponse)
                    }
                    else if(url.contains("register")) {
                        (c as RegisterActivity).notifyFinish(resultJson as AccountResponse)
                    }
                }
                is Result.Success -> {
                    if(url.contains("auth")) {
                        resultJson = gson.fromJson(result.get().content, AccountResponse::class.java)
                        if (url.contains("login")) {
                            (c as LoginActivity).notifyFinish(resultJson as AccountResponse)
                        }
                        if (url.contains("register")) {
                            (c as RegisterActivity).notifyFinish(resultJson as AccountResponse)
                        }
                    }
                }
            }
        }
    }
}