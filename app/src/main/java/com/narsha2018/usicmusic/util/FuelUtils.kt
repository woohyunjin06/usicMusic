package com.narsha2018.usicmusic.util

import android.content.Context
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.narsha2018.usicmusic.activity.*
import com.narsha2018.usicmusic.model.LoginResponse
import com.narsha2018.usicmusic.model.MusicResponse
import es.dmoral.toasty.Toasty
import org.json.JSONArray

class FuelUtils (private val c: Context){
    init {
        Toasty.Config.reset()
        FuelManager.instance.basePath = "http://115.68.22.74/api"
    }

    private val gson = Gson()

    enum class PostEnum {
        Login, AutoLogin, Register, Comment, Write, MusicFavorite, SearchFavorite
    }

    enum class MusicEnum {
        Music, Favorite, Rank, Search
    }

    enum class BoardEnum {
        Board, Content
    }

    enum class DeleteEnum {
        Board, Comment, MusicFavorite, SearchFavorite
    }

    fun postData(url: String, data: Any, classify: PostEnum) {
        val json : String = gson.toJson(data)
        val resultJson = LoginResponse(600, "", "", "")
        url.httpPost().body(json, Charsets.UTF_8).header("Content-Type" to "application/json").responseJson { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    when (classify) {
                        PostEnum.AutoLogin -> (c as EntranceActivity).notifyFinish(gson.toJson(resultJson))
                        PostEnum.Login -> (c as LoginActivity).notifyFinish(gson.toJson(resultJson))
                        PostEnum.Register -> (c as RegisterActivity).notifyFinish(gson.toJson(resultJson))
                        PostEnum.Comment -> (c as DetailActivity).notifyCommentFinish(gson.toJson(resultJson))
                        PostEnum.Write -> (c as WriteActivity).notifyFinish(gson.toJson(resultJson))
                        PostEnum.MusicFavorite -> (c as MusicActivity).notifyFavoriteFinish(false)
                        PostEnum.SearchFavorite -> (c as SearchActivity).notifyFavoriteFinish(false)
                    }
                }
                is Result.Success -> {
                    when (classify) {
                        PostEnum.AutoLogin -> (c as EntranceActivity).notifyFinish(result.get().content)
                        PostEnum.Login -> (c as LoginActivity).notifyFinish(result.get().content)
                        PostEnum.Register -> (c as RegisterActivity).notifyFinish(result.get().content)
                        PostEnum.Comment -> (c as DetailActivity).notifyCommentFinish(result.get().content)
                        PostEnum.Write -> (c as WriteActivity).notifyFinish(result.get().content)
                        PostEnum.MusicFavorite -> (c as MusicActivity).notifyFavoriteFinish(true)
                        PostEnum.SearchFavorite -> (c as SearchActivity).notifyFavoriteFinish(true)
                    }
                }
            }
        }
    }

    fun getMusicData(classify: MusicEnum) {
        val resultJson = MusicResponse(true, "error", "", "", JSONArray(), "","","")
        "/music".httpGet().responseJson { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    when (classify) {
                        MusicEnum.Music -> (c as MusicActivity).notifyFinish(gson.toJson(resultJson))
                        MusicEnum.Favorite -> (c as FavoriteActivity).notifyFinish(gson.toJson(resultJson))
                        MusicEnum.Rank -> (c as MainActivity).notifyFinish(gson.toJson(resultJson))
                        MusicEnum.Search -> (c as SearchActivity).notifyFinish(gson.toJson(resultJson))
                    }
                }
                is Result.Success -> {
                    when (classify) {
                        MusicEnum.Music -> (c as MusicActivity).notifyFinish(result.get().content)
                        MusicEnum.Favorite -> (c as FavoriteActivity).notifyFinish(result.get().content)
                        MusicEnum.Rank -> (c as MainActivity).notifyFinish(result.get().content)
                        MusicEnum.Search -> (c as SearchActivity).notifyFinish(result.get().content)
                    }
                }
            }
        }
    }

    fun getCommunityData(classify: BoardEnum, id: String?) {
        val resultJson = MusicResponse(true, "error", "", "", JSONArray(), "","","")
        ("/board" + ("/$id".takeIf { id != null } ?: "")).httpGet().responseJson { _, _, result ->
            // id가 null 이 아닐때 id 를 붙임
            when (result) {
                is Result.Failure -> {
                    when (classify) {
                        BoardEnum.Board -> (c as CommunityActivity).notifyFinish(gson.toJson(resultJson))
                        BoardEnum.Content -> (c as DetailActivity).notifyFinish(gson.toJson(resultJson))
                    }
                }
                is Result.Success -> {
                    when (classify) {
                        BoardEnum.Board -> (c as CommunityActivity).notifyFinish(result.get().content)
                        BoardEnum.Content -> (c as DetailActivity).notifyFinish(result.get().content)
                    }
                }
            }
        }
    }
    // "/music/$id/rate/${PreferencesUtils(c).getData("id")}" : favorite
    // "/board/$bid/comment/$id" : comment
    // "/board/$bid" : board

    fun delete(requestUrl: String, classify: DeleteEnum) {
        requestUrl.httpDelete().responseJson { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    when (classify) {
                        DeleteEnum.MusicFavorite -> (c as MusicActivity).notifyFavoriteFinish(false)
                        DeleteEnum.SearchFavorite -> (c as SearchActivity).notifyFavoriteFinish(false)
                        DeleteEnum.Board -> (c as DetailActivity).notifyDeleteFinish(result.get().content)
                        DeleteEnum.Comment -> {
                        }
                    }

                }
                is Result.Success -> {
                    when (classify) {
                        DeleteEnum.MusicFavorite -> (c as MusicActivity).notifyFavoriteFinish(true)
                        DeleteEnum.SearchFavorite -> (c as SearchActivity).notifyFavoriteFinish(true)
                        DeleteEnum.Board -> (c as DetailActivity).notifyDeleteFinish(result.get().content)
                        DeleteEnum.Comment -> {
                        }
                    }
                }
            }
        }

    }
}
