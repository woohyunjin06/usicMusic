package com.narsha2018.usicmusic.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.activity.MainActivity
import com.narsha2018.usicmusic.view.MediaPlayer
import kotlinx.android.synthetic.main.activity_main.view.*

class MusicService : Service() {

    companion object {
        val MESSAGE_KEY = "isStartCommand"
        val SONG_URL = "songUrl"
        val SONG_NAME = "songTitle"
    }

    val mediaPlayer = MediaPlayer()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val message = intent!!.extras!!.getBoolean(MESSAGE_KEY, false)
        val url = intent.extras!!.getString(SONG_URL,"")
        val song = intent.extras!!.getString(SONG_NAME, "")
        if(message){
            mediaPlayer.init()
            mediaPlayer.playMusic(url)

            val mainIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val mBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, "usicMusic_NOTIFY")
                    .setSmallIcon(R.drawable.ic_favorite)
                    .setContentTitle(song)

        }
        else{
            mediaPlayer.stopMusic()
        }
        return START_NOT_STICKY
    }
}
