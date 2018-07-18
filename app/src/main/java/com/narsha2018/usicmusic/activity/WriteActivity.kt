package com.narsha2018.usicmusic.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.model.WriteRequest
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_write.*
import org.json.JSONObject

class WriteActivity : AppCompatActivity(){
    val fuelUtils = FuelUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        post.setOnClickListener { fuelUtils.postData("/board", WriteRequest(titles.text.toString(), content.text.toString(),
                PreferencesUtils(this).getData("id")), false) }
    }
    fun notifyFinish(result : String){
        val objects = JSONObject(result)
        if(objects.getString("status")!="200")
            Toasty.error(this, "작성할 수 없습니다").show()
        else {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }
}