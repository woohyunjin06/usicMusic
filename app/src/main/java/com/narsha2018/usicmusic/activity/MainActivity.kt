package com.narsha2018.usicmusic.activity

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.narsha2018.usicmusic.R
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.adapter.MusicItem
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.DateUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import com.narsha2018.usicmusic.view.MediaPlayer
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.json.JSONObject

@Suppress("DEPRECATION") // for progressDialog
class MainActivity : AppCompatActivity(){ //rank activity

    val fuelUtils = FuelUtils(this)
    val mediaPlayer: MediaPlayer = MediaPlayer()
    var progressDialog: ProgressDialog? = null

    var songUrl: String? = null

    var myService: MusicService? = null
    var isBound = false


    fun onClickPlay(idx: String?, title: String, uri: String, btn: ImageView) {
        mediaPlayer.playMusic(uri) //return boolean
        song.text = title
        val play: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        val pause: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_pause)
        if (btn.drawable.constantState == play?.constantState) { // 재생중이지 않음
            btn.imageResource = R.drawable.ic_pause
        } else { //끄기
            btn.imageResource = R.drawable.ic_play
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (PreferencesUtils(this).getData("notificationChannel") != "yes")
            createNotificationChannel()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        music.onClick { startActivityForResult<MusicActivity>(1) }
        favorite.onClick { startActivityForResult<FavoriteActivity>(1) }
        find.onClick { startActivityForResult<SearchActivity>(1) }
        community.onClick { startActivity<CommunityActivity>() }
        loadRank()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel("usicMusic_NOTIFY", "usicMusic Music Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "usicMusic Music Player"
                enableLights(true)
                lightColor = Color.GREEN
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun loadRank() {
        progressDialog?.show()
        doAsync {
            fuelUtils.getRankData()
        }
    }

    fun notifyFinish(rankInfo: String) {
        val arr = JSONObject(rankInfo).getJSONArray("music")
        Log.d("ARRAY", arr.toString())
        var a = 0
        var b = 0
        var c = 0
        var objectA: JSONObject? = null
        var objectB: JSONObject? = null
        var objectC: JSONObject? = null

        for (idx: Int in 0 until arr.length()) { // 한개의 음악에 한해
            val item: JSONObject = arr.getJSONObject(idx)
            if (item.getBoolean("isMusic")) { // 소스가 아니고 음악이면
                val rateArr = item.getJSONArray("rate")
                val count = rateArr.length() // 좋아요 갯수를 가져옴
                if (count > a) {
                    c = b
                    objectC = objectB
                    b = a
                    objectB = objectA
                    a = count
                    objectA = item
                }
                else if (count in (b)..(a + 1)) {
                    c = b
                    objectC = objectB
                    b = count
                    objectB = item
                }
                else if (count in (c)..(b + 1)) {
                    c = count
                    objectC = item
                }
            }
        }
        musicTitle1.text = objectA?.getString("title")
        musicTitle2.text = objectB?.getString("title")
        musicTitle3.text = objectC?.getString("title")

        if (objectA != null) {
            if (objectA!!.has("artist"))
                artist1.text = objectA.getString("artist")
            else
                artist1.text = "No Artist"
            Glide.with(this)
                    .load("http://10.80.162.221:3000/" + objectA.getString("cover"))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile1_img)
        }
        if (objectB != null) {
            if (objectB!!.has("artist"))
                artist2.text = objectB.getString("artist")
            else
                artist2.text = "No Artist"
            Glide.with(this)
                    .load("http://10.80.162.221:3000/" + objectB.getString("cover"))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile2_img)
        }
        if (objectC != null) {
            if (objectC!!.has("artist"))
                artist3.text = objectC.getString("artist")
            else
                artist3.text = "No Artist"
            Glide.with(this)
                    .load("http://10.80.162.221:3000/" + objectC.getString("cover"))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile3_img)
        }
        if(musicTitle1.text.trim()=="")
            musicTitle1.text = "No Music"
        if(musicTitle2.text.trim()=="")
            musicTitle2.text = "No Music"
        if(musicTitle3.text.trim()=="")
            musicTitle3.text = "No Music"
        progressDialog?.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        //val play: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        //val pause: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_pause)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //val result = data.getStringExtra("activity")
                if(data.getBooleanExtra("isPlaying", false)) {
                    song.text = data.getStringExtra("songTitle")
                    btn_play.imageResource = R.drawable.ic_pause
                    btn_play.onClick {stopService(Intent(this@MainActivity, MusicService::class.java))
                        btn_play.imageResource = R.drawable.ic_play
                        song.text = "Select music"}
                }
            }
        }
    }
}
