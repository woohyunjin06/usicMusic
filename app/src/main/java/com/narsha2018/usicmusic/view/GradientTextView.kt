package com.narsha2018.usicmusic.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Shader
import android.graphics.LinearGradient
import android.util.AttributeSet
import android.widget.TextView
import com.narsha2018.usicmusic.util.ColorUtils


class GradientTextView : TextView {
    constructor(context: Context) : super(context, null, -1)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, -1)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight.toFloat()
        val width = measuredWidth.toFloat()

        paint.shader = LinearGradient(
                0f, 0f, width, height,
                ColorUtils.getColors(context),
                null,
                Shader.TileMode.CLAMP
        )
    }

}