package com.narsha2018.usicmusic.model

data class MusicRank(val title: String, val artist: String, val cover: String, val music: String, val rate:Int) : Comparable<MusicRank> {
    override fun compareTo(other: MusicRank): Int {
        return when {
            this.rate == other.rate -> 0
            this.rate < other.rate -> 1
            else -> -1
        }
    }
}