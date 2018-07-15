package com.narsha2018.usicmusic.model

import org.json.JSONArray

data class MusicResponse(val isMusic: Boolean, val _id : String,
                         val title : String, val music : String,
                         val rate : JSONArray, val date : String,
                         val artist : String, val thumbnail : String)