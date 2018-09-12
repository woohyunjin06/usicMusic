package com.narsha2018.usicmusic.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.model.LoginRequest
import com.narsha2018.usicmusic.model.LoginResponse
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_entrance.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import android.net.ConnectivityManager
import android.net.NetworkInfo



class EntranceActivity : AppCompatActivity() {
    private val fuelUtil = FuelUtils(this)
    private val gson = Gson()

    enum class NetworkState{
        WIFI, MOBILE, NONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        Toasty.Config.reset()

        if(checkNetwork() == NetworkState.NONE){
            Toasty.error(this, "네트워크를 연결해주세요").show()
            finish()
        }

        val id = PreferencesUtils(this).getData("id")
        val pw = PreferencesUtils(this).getData("pw")
        if(id.length+pw.length>0){
            progress.visibility = View.VISIBLE
            doAsync { doLogin(id, pw) }
        }
        else{
            go.visibility = View.VISIBLE
        }

        val btmDrawable: BitmapDrawable = ContextCompat.getDrawable(this, R.drawable.bg_img_splash) as BitmapDrawable
        val btmBitmap: Bitmap = blurBitmap(this, btmDrawable.bitmap, 5)
        val resultDrawable = BitmapDrawable(resources, btmBitmap)
        bg_entrance.background = resultDrawable

        go.setOnClickListener { startActivity<LoginActivity>(); finish()}
    }
    private fun doLogin(id : String, pw: String) {
        if (id == "" || pw == "") {
            Toasty.warning(this, "Please type id and password.").show()
            return
        }
        doAsync {
            fuelUtil.postData("/auth/login", LoginRequest(id, pw), FuelUtils.PostEnum.AutoLogin)
        }
    }

    fun notifyFinish(accountResponse : String){
        doAsync {
            val resultJson: LoginResponse = gson.fromJson(accountResponse, LoginResponse::class.java)
            if (resultJson.status == 200 && resultJson.message.trim() != "") { // success
                PreferencesUtils(this@EntranceActivity).apply {
                    saveData("token", resultJson.token)
                    saveData("nick", resultJson.nickname)
                }
                uiThread {
                    Toasty.success(it, resultJson.message).show()
                    it.startActivity<MainActivity>()
                    it.finish()
                }
            } else {
                uiThread {

                    Toasty.error(it, "자동로그인을 실패했습니다").show()
                    it.startActivity<LoginActivity>()
                    it.finish()
                }
            }
        }
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
    private fun checkNetwork() : NetworkState{
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                return NetworkState.WIFI
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                return NetworkState.MOBILE
            }
        }
        return NetworkState.NONE
    }
}
