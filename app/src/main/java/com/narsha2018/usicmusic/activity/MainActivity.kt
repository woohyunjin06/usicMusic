package com.narsha2018.usicmusic.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.narsha2018.usicmusic.R
import android.view.View
import android.widget.ImageView
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.adapter.FragmentAdapter
import com.narsha2018.usicmusic.fragment.ChartFragment
import com.narsha2018.usicmusic.fragment.RankFragment
import com.narsha2018.usicmusic.view.MediaPlayer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_rank.view.*
import org.jetbrains.anko.imageResource


class MainActivity : AppCompatActivity(), OnPlayListener{

    val rankFragment : RankFragment = RankFragment()
    val chartFragment : ChartFragment = ChartFragment()
    val mediaPlayer : MediaPlayer = MediaPlayer()

    override fun onClickPlay(idx: Int,title: String, uri: String, btn: ImageView) {
        val v : View? = rankFragment.getLayoutView()
        mediaPlayer.playMusic(uri) //return boolean
        v!!.song.text = title
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
        initViewPager()
        mediaPlayer.init()
    }

    private fun initViewPager() {
        viewPager.adapter = FragmentAdapter.Holder(supportFragmentManager)
                .add(rankFragment)
                .add(chartFragment)
                .set()
        viewPager.overScrollMode = View.OVER_SCROLL_NEVER
    }

}
