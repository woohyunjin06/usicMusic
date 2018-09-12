package com.narsha2018.usicmusic.activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Log.d
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.model.MusicRank
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
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
    private var btnPrev : ImageView? = null

    private var isPlaying = false
    var titles : String? = null
    private var uris : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (PreferencesUtils(this).getData("notificationChannel") != "yes")
            createNotificationChannel()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        Toasty.Config.reset()

        mapClickListener()
        loadRank()
    }

    private fun mapClickListener(){
        music.setOnClickListener { startActivityForResult<MusicActivity>(1) }
        favorite.setOnClickListener { startActivityForResult<FavoriteActivity>(1) }
        find.setOnClickListener { startActivityForResult<SearchActivity>(1) }
        community.setOnClickListener { startActivity<CommunityActivity>() }
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
        play1.setOnClickListener { Toasty.error(this, "노래가 없습니다").show() }
        play2.setOnClickListener { Toasty.error(this, "노래가 없습니다").show() }
        play3.setOnClickListener { Toasty.error(this, "노래가 없습니다").show() }
        progressDialog?.show()
        doAsync {
            fuelUtils.getMusicData(FuelUtils.MusicEnum.Rank)
        }
    }

    fun notifyFinish(rankInfo: String) {
        val arr = JSONObject(rankInfo).getJSONArray("music")

        val rankArray = ArrayList<MusicRank>()

        for(idx: Int in 0 until arr.length()){
            val item: JSONObject = arr.getJSONObject(idx)
            if(item.getBoolean("isMusic")){
                rankArray.add(MusicRank(item.getString("title"), item.getString("artist"),
                        item.getString("cover"), item.getString("music"),
                        item.getJSONArray("rate").length()))
            }
        }
        rankArray.sort()

        if(rankArray.size >= 0){
            musicTitle1.text = rankArray[0].title
            artist1.text = rankArray[0].artist
            Glide.with(this)
                    .load(getString(R.string.server_url) + rankArray[0].cover)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile1_img)

            play1.setOnClickListener {
                onClickPlay(rankArray[0].title,rankArray[0].music,play1)
            }
        }
        if(rankArray.size >= 1){
            musicTitle2.text = rankArray[1].title
            artist2.text = rankArray[1].artist
            Glide.with(this)
                    .load(getString(R.string.server_url) + rankArray[1].cover)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile2_img)
            play2.setOnClickListener {
                onClickPlay(rankArray[1].title,rankArray[1].music,play2)
            }
        }
        if(rankArray.size >= 2){
            musicTitle3.text = rankArray[2].title
            artist3.text = rankArray[2].artist
            Glide.with(this)
                    .load(getString(R.string.server_url) + rankArray[2].cover)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_launcher) //로딩
                            .error(R.drawable.ic_launcher) //실패
                            .fallback(R.drawable.ic_launcher)) //없음
                    .into(profile3_img)
            play3.setOnClickListener {
                onClickPlay(rankArray[2].title,rankArray[2].music,play3)
            }
        }

        progressDialog?.dismiss()
    }

    private fun onClickPlay(title: String, uri: String, btn: ImageView) {
        val play: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        if (btn.drawable.constantState == play?.constantState) { // 켜기
            if(btnPrev!=null)
                btnPrev!!.imageResource= R.drawable.ic_play
            btn.imageResource = R.drawable.ic_pause
            isPlaying = true
            titles = title
            uris = uri

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            startService(i)
            song.text = title
            btn_play.imageResource = R.drawable.ic_pause
            btn_play.setOnClickListener {stopService(Intent(this@MainActivity, MusicService::class.java))
                btn_play.imageResource = R.drawable.ic_play
                song.text = getString(R.string.sel_music)}
            btnPrev = btn
        } else { //끄기
            btn.imageResource = R.drawable.ic_play
            isPlaying = false

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            stopService(i)
        }
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
