package com.narsha2018.usicmusic.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.narsha2018.usicmusic.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import com.google.gson.Gson
import com.narsha2018.usicmusic.model.LoginRequest
import com.narsha2018.usicmusic.model.LoginResponse
import com.narsha2018.usicmusic.model.RegisterRequest
import com.narsha2018.usicmusic.model.RegisterResponse
import com.narsha2018.usicmusic.util.BitmapUtils
import com.narsha2018.usicmusic.util.FuelUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread


class RegisterActivity : AppCompatActivity() {

    val gson = Gson()
    var progressDialog : ProgressDialog? = null
    private val fuelUtil = FuelUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btmDrawable: BitmapDrawable = ContextCompat.getDrawable(this, R.drawable.bg_img_login) as BitmapDrawable
        val btmBitmap: Bitmap = BitmapUtils.blurBitmap(this, btmDrawable.bitmap, 25)
        val resultDrawable = BitmapDrawable(resources, btmBitmap)
        logo_login.background = resultDrawable
        Toasty.Config.reset()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")

        back.onClick { finish() }
        start.onClick{ doRegister() }
    }
    private fun doRegister() {
        val strId : String = id.text.toString()
        val strPw : String = pw1.text.toString()
        val strNick : String = nick.text.toString()
        if(strId=="" || strPw=="" || strNick=="") {
            Toasty.warning(this, "Please type your id or password").show()
            return
        }
        progressDialog?.show()
        doAsync {
            fuelUtil.postData("/auth/register", RegisterRequest(strId, strPw, strNick), false)
        }
    }

    fun notifyFinish(accountResponse : String){
        doAsync {
            val resultJson: RegisterResponse = gson.fromJson(accountResponse, RegisterResponse::class.java)
            if (resultJson.status == 200 && resultJson.message.trim() != "") {
                uiThread {
                    Toasty.success(it, resultJson.message).show()
                    it.startActivity<LoginActivity>()
                    it.finish()
                }
            } else {
                uiThread {
                    Toasty.error(it, "ID가 중복되었습니다.").show()
                    it.id.setText("")
                    it.nick.setText("")
                }
            }
        }
    }
}
