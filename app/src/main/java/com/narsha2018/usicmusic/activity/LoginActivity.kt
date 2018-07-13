package com.narsha2018.usicmusic.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.narsha2018.usicmusic.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.util.Log
import com.narsha2018.usicmusic.model.AccountRequest
import com.narsha2018.usicmusic.model.AccountResponse
import com.narsha2018.usicmusic.util.BitmapUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import es.dmoral.toasty.Toasty


class LoginActivity : AppCompatActivity() {
    private val fuelUtil = FuelUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btmDrawable:BitmapDrawable = ContextCompat.getDrawable(this, R.drawable.bg_img_login) as BitmapDrawable
        val btmBitmap: Bitmap = BitmapUtils.blurBitmap(this, btmDrawable.bitmap, 25)
        val resultDrawable = BitmapDrawable(resources, btmBitmap)
        logo_login.background = resultDrawable
        Toasty.Config.reset()
        go.onClick{ doLogin() }
        start.onClick { startActivity<RegisterActivity>() }
    }
    private fun doLogin() {
        val id: String = id.text.toString()
        val pw: String = pw.text.toString()
        if (id == "" || pw == "") {
            Toasty.warning(this, "Please type id and password.").show()
            return
        }
        fuelUtil.postData("/api/auth/login", AccountRequest(id, pw))
    }

    fun notifyFinish(accountResponse : AccountResponse){
        if(accountResponse.status==200 && accountResponse.message.trim()!=""){ // success
            PreferencesUtils(this).saveData("token",accountResponse.token)
            Toasty.success(this, accountResponse.message).show()
            startActivity<MainActivity>()
            finish()
        } else{
            Toasty.error(this, "Login failed").show()
            id.setText("")
            pw.setText("")
        }
    }
}
