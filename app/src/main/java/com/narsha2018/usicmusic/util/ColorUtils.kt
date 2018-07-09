package com.narsha2018.usicmusic.util

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import com.narsha2018.usicmusic.R

class ColorUtils {
    companion object {
        fun getColors(c: Context) : IntArray{
            return intArrayOf(ContextCompat.getColor(c, R.color.colorGradientStart),ContextCompat.getColor(c, R.color.colorGradientStart))
        }
    }
}