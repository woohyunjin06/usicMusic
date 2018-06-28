package com.narsha2018.usicmusic.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.narsha2018.usicmusic.R
import android.view.View
import com.narsha2018.usicmusic.adapter.FragmentAdapter
import com.narsha2018.usicmusic.fragment.ChartFragment
import com.narsha2018.usicmusic.fragment.RankFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewPager()

    }

    private fun initViewPager() {
        viewPager.adapter = FragmentAdapter.Holder(supportFragmentManager)
                .add(RankFragment())
                .add(ChartFragment())
                .set()
        viewPager.overScrollMode = View.OVER_SCROLL_NEVER
    }
}
