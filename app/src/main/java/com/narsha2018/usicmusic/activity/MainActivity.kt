package com.narsha2018.usicmusic.activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.json.JSONObject

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(){ //rank activity

    private val fuelUtils = FuelUtils(this)
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (PreferencesUtils(this).getData("notificationChannel") != "yes")
            createNotificationChannel()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        music.setOnClickListener { startActivityForResult<MusicActivity>(1) }
        favorite.setOnClickListener { startActivityForResult<FavoriteActivity>(1) }
        find.setOnClickListener { startActivityForResult<SearchActivity>(1) }
        community.setOnClickListener { startActivity<CommunityActivity>() }
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
            fuelUtils.getMusicData(FuelUtils.MusicEnum.Rank)
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
                when {
                    count > a -> {
                        c = b
                        objectC = objectB
                        b = a
                        objectB = objectA
                        a = count
                        objectA = item
                    }
                    count in (b)..(a + 1) -> {
                        c = b
                        objectC = objectB
                        b = count
                        objectB = item
                    }
                    count in (c)..(b + 1) -> {
                        c = count
                        objectC = item
                    }
                }
            }
        }
        musicTitle1.text = objectA?.getString("title")
        musicTitle2.text = objectB?.getString("title")
        musicTitle3.text = objectC?.getString("title")

        if (objectA != null) {
            if (objectA.has("artist"))
                artist1.text = objectA.getString("artist")
            else
                artist1.text = getString(R.string.artist_no)
            Glide.with(this)
                    .load(getString(R.string.server_url) + objectA.getString("cover"))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile1_img)
        }
        if (objectB != null) {
            if (objectB.has("artist"))
                artist2.text = objectB.getString("artist")
            else
                artist2.text = getString(R.string.artist_no)
            Glide.with(this)
                    .load(getString(R.string.server_url) + objectB.getString("cover"))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile2_img)
        }
        if (objectC != null) {
            if (objectC.has("artist"))
                artist3.text = objectC.getString("artist")
            else
                artist3.text = getString(R.string.artist_no)
            Glide.with(this)
                    .load(getString(R.string.server_url) + objectC.getString("cover"))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile3_img)
        }
        if(musicTitle1.text.trim()=="")
            musicTitle1.text = getString(R.string.music_no)
        if(musicTitle2.text.trim()=="")
            musicTitle2.text = getString(R.string.music_no)
        if(musicTitle3.text.trim()=="")
            musicTitle3.text = getString(R.string.music_no)
        progressDialog?.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if(data.getBooleanExtra("isPlaying", false)) {
                    song.text = data.getStringExtra("songTitle")
                    btn_play.imageResource = R.drawable.ic_pause
                    btn_play.setOnClickListener {stopService(Intent(this@MainActivity, MusicService::class.java))
                        btn_play.imageResource = R.drawable.ic_play
                        song.text = getString(R.string.sel_music)}
                }
            }
        }
    }
}
