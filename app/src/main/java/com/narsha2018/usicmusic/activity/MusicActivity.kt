package com.narsha2018.usicmusic.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.adapter.MusicAdapter
import com.narsha2018.usicmusic.adapter.MusicItem
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.DateUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_music.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class MusicActivity : AppCompatActivity(), OnPlayListener {
    var progressDialog : ProgressDialog? = null


    var btn_prev : ImageView? = null
    private var isPlaying = false
    var titles : String? = null
    var uris : String? = null

    override fun onClickPlay(idx: String?, title: String, uri: String, btn: ImageView) {
        val play: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        if (btn.drawable.constantState == play?.constantState) { // 켜기
            if(btn_prev!=null)
                btn_prev!!.imageResource= R.drawable.ic_play
            btn.imageResource = R.drawable.ic_pause
            isPlaying = true
            titles = title
            uris = uri

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            startService(i)
            btn_prev = btn
        } else { //끄기
            btn.imageResource = R.drawable.ic_play
            isPlaying = false

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            stopService(i)
        }
    }

    private val fuelUtils = FuelUtils(this)
    private val mItems = ArrayList<MusicItem>()
    private var adapter: MusicAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        initRecyclerView()
        Toasty.Config.reset()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        loadMusic()
        swipe_layout.setOnRefreshListener { loadMusic() }
    }

    private fun initRecyclerView() { // RecyclerView 기본세팅
        // 변경될 가능성 o : false 로 , 없다면 true.
        list.setHasFixedSize(false)

        adapter = MusicAdapter(mItems, this, this, false)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    private fun loadMusic() {
        mItems.clear()
        progressDialog?.show()
        doAsync {
            fuelUtils.getMusicData(FuelUtils.MusicEnum.Music)
        }
    }

    fun notifyFinish(musicInfo: String) {
        doAsync {
            val arr = JSONObject(musicInfo).getJSONArray("music")
            for (idx: Int in 0 until arr.length()) { // 한개의 음악에 한해
                val item: JSONObject = arr.getJSONObject(idx)
                if (item.getBoolean("isMusic")) { // 소스가 아니고 음악이면
                    val rateArr = item.getJSONArray("rate")
                    var isLike = false
                    for (idx2: Int in 0 until rateArr.length()) { // 좋아요 한 사람중 자신의 이름을 찾으면 is Like = true
                        var authorID: String? = null
                        val authorObject = rateArr.getJSONObject(idx2)
                        if (authorObject.has("username"))
                            authorID = authorObject.getString("username")
                        if (authorID != null && authorID == PreferencesUtils(this@MusicActivity).getData("id"))
                            isLike = true
                    }
                    if (item.has("artist"))
                        uiThread {
                            mItems.add(MusicItem(item.getString("_id"),
                                    item.getString("title"),
                                    DateUtils.fromISO(item.getString("date"))!!,
                                    "http://192.168.43.94:3000/" + item.getString("music"),
                                    "http://192.168.43.94:3000/" + item.getString("cover"),
                                    isLike,
                                    item.getString("artist")
                            ))
                        }
                    else
                        uiThread {
                            mItems.add(MusicItem(item.getString("_id"),
                                    item.getString("title"),
                                    DateUtils.fromISO(item.getString("date"))!!,
                                    "http://192.168.43.94:3000/" + item.getString("music"),
                                    "http://192.168.43.94:3000/" + item.getString("cover"),
                                    isLike,
                                    "No Artist"
                            ))
                        }
                }
            }
            uiThread {
                adapter!!.notifyDataSetChanged()
                progressDialog?.dismiss()
                swipe_layout.isRefreshing = false
            }
        }
    }

    fun notifyFavoriteFinish(status : Boolean){
        if(!status)
            Toasty.success(this, "Failed to rate")
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra("isPlaying", isPlaying)
        returnIntent.putExtra("songUrl", uris)
        returnIntent.putExtra("songTitle", titles)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
