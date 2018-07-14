package com.narsha2018.usicmusic.util

import android.content.Context
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.narsha2018.usicmusic.activity.LoginActivity
import com.narsha2018.usicmusic.activity.MainActivity
import com.narsha2018.usicmusic.activity.MusicActivity
import com.narsha2018.usicmusic.activity.RegisterActivity
import com.narsha2018.usicmusic.model.AccountResponse
import com.narsha2018.usicmusic.model.MusicResponse
import es.dmoral.toasty.Toasty
import org.json.JSONArray

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
                        (c as LoginActivity).notifyFinish(gson.toJson(resultJson))
                    }
                    else if(url.contains("register")) {
                        (c as RegisterActivity).notifyFinish(gson.toJson(resultJson))
                    }
                }
                is Result.Success -> {
                    if(url.contains("auth")) {
                        //
                        if (url.contains("login")) {
                            (c as LoginActivity).notifyFinish(result.get().content)
                        }
                        if (url.contains("register")) {
                            (c as RegisterActivity).notifyFinish(result.get().content)
                        }
                    }

                }
            }
        }

    }
    fun getMusicData() {
        val gson = Gson()
        val resultJson = MusicResponse(true, "error", "", "", JSONArray(), "")
        "/api/music".httpGet().responseJson { _, _, result ->
            //.header("Content-Type" to "application/json")
            when (result) {
                is Result.Failure -> {
                    (c as MusicActivity).notifyFinish(gson.toJson(resultJson))
                }
                is Result.Success -> {
                    (c as MusicActivity).notifyFinish(result.get().content)
                }
            }
        }
    }
}