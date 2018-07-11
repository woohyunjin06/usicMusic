package com.narsha2018.usicmusic.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.util.RestUtils
import android.content.Context
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.renderscript.Element
import android.support.v4.content.ContextCompat
import com.narsha2018.usicmusic.util.BitmapUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btmDrawable:BitmapDrawable = ContextCompat.getDrawable(this, R.drawable.bg_img_login) as BitmapDrawable
        val btmBitmap: Bitmap = BitmapUtils.blurBitmap(this, btmDrawable.bitmap, 25)
        val resultDrawable = BitmapDrawable(resources, btmBitmap)
        logo_login.background = resultDrawable

        go.setOnClickListener { startActivity<MainActivity>() }
        start.setOnClickListener { startActivity<RegisterActivity>() }

    }
}
