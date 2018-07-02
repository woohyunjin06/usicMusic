package com.narsha2018.usicmusic.`interface`

import android.widget.ImageView

interface OnPlayListener {
    fun onClickPlay(idx: Int, title : String, uri : String, btn: ImageView)
}