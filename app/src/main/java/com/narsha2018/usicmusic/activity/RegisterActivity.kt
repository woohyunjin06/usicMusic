package com.narsha2018.usicmusic.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.narsha2018.usicmusic.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import com.narsha2018.usicmusic.model.AccountRequest
import com.narsha2018.usicmusic.model.AccountResponse
import com.narsha2018.usicmusic.util.BitmapUtils
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btmDrawable: BitmapDrawable = ContextCompat.getDrawable(this, R.drawable.bg_img_login) as BitmapDrawable
        val btmBitmap: Bitmap = BitmapUtils.blurBitmap(this, btmDrawable.bitmap, 25)
        val resultDrawable = BitmapDrawable(resources, btmBitmap)
        logo_login.background = resultDrawable

        back.onClick { finish() }
        start.onClick{ doRegister() }
    }
    private fun doRegister(){
        val id : String = id.text.toString()
        val strPw1 : String = pw1.text.toString()
        val strPw2 : String = pw2.text.toString()
        if(id=="" || strPw1=="" || strPw2=="") {
            snackbar(parentLayout, "아이디나 비밀번호를 입력해주세요")
            return
        }
        if(strPw1 != strPw2) {
            snackbar(parentLayout, "비밀번호를 확인해주세요")
            return
        }
        val registerInfo = AccountRequest(id, strPw1)

    }
}
