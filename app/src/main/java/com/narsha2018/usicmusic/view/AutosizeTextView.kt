package com.narsha2018.usicmusic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.opengl.ETC1.getHeight
import android.util.AttributeSet
import android.widget.TextView


class AutosizeTextView : TextView {
    internal var fit = false

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    fun setFitTextToBox(fit: Boolean?) {
        this.fit = fit!!
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (fit) _shrinkToFit()
    }

    protected fun _shrinkToFit() {
        val height = this.height
        val lines = this.lineCount
        val r = Rect()
        val y1 = this.getLineBounds(0, r)
        val y2 = this.getLineBounds(lines - 1, r)
        val size = this.textSize
        if (y2 > height && size >= 8.0f) {
            this.textSize = size - 2.0f
            _shrinkToFit()
        }
    }

    companion object {
        internal val TAG = "TextFitTextView"
    }
}