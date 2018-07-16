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
import com.narsha2018.usicmusic.util.BitmapUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.uiThread


class LoginActivity : AppCompatActivity() {

    val gson = Gson()
    var progressDialog : ProgressDialog? = null
    private val fuelUtil = FuelUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btmDrawable:BitmapDrawable = ContextCompat.getDrawable(this, R.drawable.bg_img_login) as BitmapDrawable
        val btmBitmap: Bitmap = BitmapUtils.blurBitmap(this, btmDrawable.bitmap, 25)
        val resultDrawable = BitmapDrawable(resources, btmBitmap)
        logo_login.background = resultDrawable
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
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
        progressDialog?.show()
        doAsync {
            fuelUtil.postData("/auth/login", LoginRequest(id, pw))
        }
    }

    fun notifyFinish(accountResponse : String){
        doAsync {
            val resultJson: LoginResponse = gson.fromJson(accountResponse, LoginResponse::class.java)
            if (resultJson.status == 200 && resultJson.message.trim() != "") { // success
                PreferencesUtils(this@LoginActivity).apply {
                    saveData("token", resultJson.token)
                    saveData("id", id.text.toString())
                    saveData("nick", resultJson.nickname)
                }
                uiThread {
                    Toasty.success(it, resultJson.message).show()
                    it.startActivity<MainActivity>()
                    it.finish()
                }
            } else {
                uiThread {
                    Toasty.error(it, "비밀번호를 확인해주세요").show()
                    pw.setText("")
                }
            }
            uiThread {
                progressDialog?.dismiss()
            }
        }
    }
}
