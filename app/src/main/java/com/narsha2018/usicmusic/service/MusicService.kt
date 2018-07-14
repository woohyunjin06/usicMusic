package com.narsha2018.usicmusic.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
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

    private val mediaPlayer = MediaPlayer()
    private var mNotifyManager : NotificationManager? = null
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        mediaPlayer.init()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent!!.extras!!.getString(SONG_URL,"")
        val song = intent.extras!!.getString(SONG_NAME, "")
            mediaPlayer.playMusic(url)

            val mainIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val mBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, "usicMusic_NOTIFY")
                    .setSmallIcon(R.drawable.ic_favorite)
                    .setContentTitle(song)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setContentText("playing")
            mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotifyManager!!.notify(1, mBuilder.build())

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mNotifyManager!!.cancel(1)
        super.onDestroy()
    }
}
