package com.narsha2018.usicmusic.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.support.v4.content.ContextCompat
import com.narsha2018.usicmusic.R
import kotlinx.android.synthetic.main.activity_entrance.*
import org.jetbrains.anko.startActivity


class EntranceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        val btmDrawable: BitmapDrawable = ContextCompat.getDrawable(this, R.drawable.bg_img_splash) as BitmapDrawable
        val btmBitmap: Bitmap = blurBitmap(this, btmDrawable.bitmap, 5)
        val resultDrawable = BitmapDrawable(resources, btmBitmap)
        bg_entrance.background = resultDrawable

        go.setOnClickListener { startActivity<LoginActivity>() }
    }

    private fun blurBitmap(context: Context, sentBitmap: Bitmap, radius: Int): Bitmap {

        val bitmap = sentBitmap.copy(sentBitmap.config, true)

        val rs = RenderScript.create(context)
        val input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(radius.toFloat()) //0.0f ~ 25.0f
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)
        return bitmap
    }
}
