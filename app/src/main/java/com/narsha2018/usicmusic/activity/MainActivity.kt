package com.narsha2018.usicmusic.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.narsha2018.usicmusic.R
import android.widget.ImageView
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.view.MediaPlayer
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivityForResult


class MainActivity : AppCompatActivity(), OnPlayListener{ //rank activity

    val mediaPlayer : MediaPlayer = MediaPlayer()

    override fun onClickPlay(idx: Int,title: String, uri: String, btn: ImageView) {
        mediaPlayer.playMusic(uri) //return boolean
        song.text = title
        val play : Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        val pause : Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_pause)
        if(btn.drawable.constantState == play?.constantState){ // 재생중이지 않음
            btn.imageResource = R.drawable.ic_pause
        }
        else{ //끄기
            btn.imageResource = R.drawable.ic_play
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer.init()
        music.onClick { startActivityForResult<MusicActivity>(1) }
        favorite.onClick { startActivityForResult<FavoriteActivity>(1)}


    }
    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent) {

    if (requestCode == 1) {
        if(resultCode == Activity.RESULT_OK){
            //val result = data.getStringExtra("result")
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
        }
    }
}
}
