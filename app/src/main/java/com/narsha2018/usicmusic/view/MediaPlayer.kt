package com.narsha2018.usicmusic.view

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer

class MediaPlayer : MediaPlayer() {
    fun init() {
        setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
    }
    fun playMusic(uri : String) : Boolean{
        return try {
            reset()
            setDataSource(uri)
            prepare()
            start()
            true
        }
        catch (e: Exception){
            false
        }
    }
}