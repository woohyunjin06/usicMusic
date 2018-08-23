package com.narsha2018.usicmusic.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtils{
    companion object {
        fun fromISO(dateStr: String): String? {
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
            val toDf = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
            val a = df.parse(dateStr)
            return toDf.format(a)
        }
    }
}