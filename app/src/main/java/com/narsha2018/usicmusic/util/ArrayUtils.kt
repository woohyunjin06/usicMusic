package com.narsha2018.usicmusic.util


object ArrayUtils {
    fun ToInts(list: List<Int>): IntArray {
        val ints = IntArray(list.size)
        for (i in list.indices) {
            ints[i] = list[i]
        }
        return ints
    }
}