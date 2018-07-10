package com.narsha2018.usicmusic.util

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class BitmapUtils {
    companion object {
        fun blurBitmap(context: Context, sentBitmap: Bitmap, radius: Int): Bitmap {

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
}