package com.narsha2018.usicmusic.`interface`

import android.widget.ImageView

interface OnPlayListener {
    fun onClickPlay(idx: String?, title : String, uri : String, btn: ImageView)
}