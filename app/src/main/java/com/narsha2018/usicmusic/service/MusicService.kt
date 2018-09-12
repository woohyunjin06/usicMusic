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

class MusicService : Service() {
    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        const val SONG_URL = "songUrl"
        const val SONG_NAME = "songTitle"
    }
    private var url : String? = null
    var title : String? = null


    private val mediaPlayer = MediaPlayer()
    private var mNotifyManager: NotificationManager? = null


    override fun onCreate() {
        mediaPlayer.init()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        url = intent!!.extras!!.getString(SONG_URL, "")
        title = intent.extras!!.getString(SONG_NAME, "")
        mediaPlayer.playMusic(url!!)

        mediaPlayer.setOnCompletionListener {
            sendBroadcast(Intent("com.narsha2018.usicmusic.finish"))
        }

        val mainIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "usicMusic_NOTIFY")
                .setSmallIcon(R.drawable.ic_favorite)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyManager!!.notify(1, mBuilder.build())

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        sendBroadcast(Intent("com.narsha2018.usicmusic.finish"))
        mediaPlayer.stopMusic()
        mNotifyManager!!.cancel(1)
        super.onDestroy()
    }
}
