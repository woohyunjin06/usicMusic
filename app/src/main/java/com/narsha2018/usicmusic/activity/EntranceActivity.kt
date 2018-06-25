package com.narsha2018.usicmusic.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.narsha2018.usicmusic.R
import kotlinx.android.synthetic.main.activity_entrance.*
import org.jetbrains.anko.startActivity


class EntranceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        go.setOnClickListener { startActivity<LoginActivity>() }
    }
}
