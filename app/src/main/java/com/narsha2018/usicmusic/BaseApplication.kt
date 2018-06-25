package com.narsha2018.usicmusic

import android.app.Application
import android.content.Context
import android.graphics.Typeface

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setDefaultFont(this, "SANS_SERIF", "font/ClementPDae.ttf")
    }

    companion object {

        fun setDefaultFont(ctx: Context,
                                   staticTypefaceFieldName: String, fontAssetName: String) {
            val regular = Typeface.createFromAsset(ctx.assets,
                    fontAssetName)
            replaceFont(staticTypefaceFieldName, regular)
        }

        private fun replaceFont(staticTypefaceFieldName: String,
                                newTypeface: Typeface) {
            try {
                val StaticField = Typeface::class.java
                        .getDeclaredField(staticTypefaceFieldName)
                StaticField.isAccessible = true
                StaticField.set(null, newTypeface)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }
}