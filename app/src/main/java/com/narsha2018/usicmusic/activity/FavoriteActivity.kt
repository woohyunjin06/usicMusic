package com.narsha2018.usicmusic.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.narsha2018.usicmusic.R
import kotlinx.android.synthetic.main.activity_favorite.*
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.adapter.FavoriteAdapter
import com.narsha2018.usicmusic.adapter.FavoriteItem
import com.narsha2018.usicmusic.adapter.MusicItem
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.DateUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.*

class FavoriteActivity : AppCompatActivity(), OnPlayListener {
    override fun onClickPlay(idx: String?, title: String, uri: String, btn: ImageView) {
        val play: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        if (btn.drawable.constantState == play?.constantState) { // 켜기
            btn.imageResource = R.drawable.ic_pause
            //isPlaying = true

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            startService(i)
        } else { //끄기
            btn.imageResource = R.drawable.ic_play
            //isPlaying = false

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            stopService(i)
        }
    }
    val fuelUtils = FuelUtils(this)
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

        Toasty.Config.reset()
        loadMusic()
    }
    private fun loadMusic() {
        fuelUtils.getMusicData(true)
    }

    fun notifyFinish(musicInfo: String) {
        val arr = JSONObject(musicInfo).getJSONArray("music")
        var isLike = false
        for (idx: Int in 0 until arr.length()) { // 한개의 음악에 한해
            isLike = false
            val item: JSONObject = arr.getJSONObject(idx)
            if (item.getBoolean("isMusic")) { // 소스가 아니고 음악이면
                val rateArr = item.getJSONArray("rate")
                for (idx2: Int in 0 until rateArr.length()) { // 좋아요 한 사람중 자신의 이름을 찾으면 is Like = true
                    var authorID: String? = null
                    val authorObject = rateArr.getJSONObject(idx2)
                    if (authorObject.has("username"))
                        authorID = authorObject.getString("username")
                    if (authorID != null && authorID == PreferencesUtils(this).getData("id"))
                        mItems.add(FavoriteItem(item.getString("title"),
                                DateUtils.fromISO(item.getString("date"))!!,
                                "http://10.80.162.221:3000/" + item.getString("music"),
                                "http://10.80.162.221:3000/" + item.getString("cover"),
                                item.getString("artist")
                        ))
                }
                //if(isLike)

            }
        }
        adapter!!.notifyDataSetChanged()
    }
}