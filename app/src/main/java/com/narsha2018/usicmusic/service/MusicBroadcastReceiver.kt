package com.narsha2018.usicmusic.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.toast

class MusicBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        val name : String = intent.action

        if(name == "com.narsha2018.usicmusic.finish"){
            context.toast("노래 끝")
        }
    }
}