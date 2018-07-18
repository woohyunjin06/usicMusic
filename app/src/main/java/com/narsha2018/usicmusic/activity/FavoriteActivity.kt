package com.narsha2018.usicmusic.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.adapter.FavoriteAdapter
import com.narsha2018.usicmusic.adapter.FavoriteItem
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.DateUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_favorite.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.util.*

class FavoriteActivity : AppCompatActivity(), OnPlayListener {
    private var isPlaying = false
    var titles : String? = null
    var uris : String? = null
    var btn_prev : ImageView? = null
    override fun onClickPlay(idx: String?, title: String, uri: String, btn: ImageView) {
        val play: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        if (btn.drawable.constantState == play?.constantState) {
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
        } else {
            btn.imageResource = R.drawable.ic_play

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            stopService(i)
            isPlaying = false
        }
    }
    val fuelUtils = FuelUtils(this)
    var progressDialog: ProgressDialog? = null
    private val mItems = ArrayList<FavoriteItem>()
    private var adapter : FavoriteAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL)

        adapter = FavoriteAdapter(mItems, this, this)

        list.layoutManager = layoutManager
        list.setHasFixedSize(false)
        list.adapter = adapter
        list.addOnScrollListener(CenterScrollListener())
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())

        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        Toasty.Config.reset()
        loadMusic()
    }
    private fun loadMusic() {
        progressDialog?.show()
        doAsync {
            fuelUtils.getMusicData(true)
        }
    }

    fun notifyFinish(musicInfo: String) {
        doAsync {
            val arr = JSONObject(musicInfo).getJSONArray("music")
            Log.d("ARRAY", arr.toString())
            for (idx: Int in 0 until arr.length()) { // 한개의 음악에 한해
                val item: JSONObject = arr.getJSONObject(idx)
                if (item.getBoolean("isMusic")) { // 소스가 아니고 음악이면
                    val rateArr = item.getJSONArray("rate")
                    for (idx2: Int in 0 until rateArr.length()) { // 좋아요 한 사람중 자신의 이름을 찾으면 is Like = true
                        var authorID: String? = null
                        val authorObject = rateArr.getJSONObject(idx2)
                        if (authorObject.has("username"))
                            authorID = authorObject.getString("username")
                        if (authorID != null && authorID == PreferencesUtils(this@FavoriteActivity).getData("id"))
                            if (item.has("artist"))
                                uiThread {
                                    mItems.add(FavoriteItem(item.getString("title"),
                                            DateUtils.fromISO(item.getString("date"))!!,
                                            "http://10.80.162.221:3000/" + item.getString("music"),
                                            "http://10.80.162.221:3000/" + item.getString("cover"),
                                            item.getString("artist")
                                    ))
                                }
                            else
                                uiThread {
                                    mItems.add(FavoriteItem(item.getString("title"),
                                            DateUtils.fromISO(item.getString("date"))!!,
                                            "http://10.80.162.221:3000/" + item.getString("music"),
                                            "http://10.80.162.221:3000/" + item.getString("cover"),
                                            "No Artist"
                                    ))
                                }

                    }
                }
            }

            uiThread {
                if(mItems.size==0){
                    list.visibility = View.GONE
                    nothing.visibility = View.VISIBLE
                }
                else {
                    list.visibility = View.VISIBLE
                    nothing.visibility = View.GONE
                }
                progressDialog?.dismiss()
                adapter!!.notifyDataSetChanged()
            }
        }
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