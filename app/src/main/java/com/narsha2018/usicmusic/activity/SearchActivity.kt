package com.narsha2018.usicmusic.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.adapter.MusicAdapter
import com.narsha2018.usicmusic.adapter.MusicItem
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.google.gson.Gson
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.DateUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONObject


class SearchActivity : AppCompatActivity(), OnPlayListener {
    private var isPlaying = false
    private val gson = Gson()
    override fun onClickPlay(idx: String?, title: String, uri: String, btn: ImageView) {
        val play: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        if (btn.drawable.constantState == play?.constantState) { // 켜기
            btn.imageResource = R.drawable.ic_pause
            isPlaying = true

            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            startService(i)
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
        setContentView(R.layout.activity_search)
        initRecyclerView()
        Toasty.Config.reset()
        btn_search.onClick { loadMusic() }
    }

    private fun initRecyclerView() { // RecyclerView 기본세팅
        // 변경될 가능성 o : false 로 , 없다면 true.
        list.setHasFixedSize(false)

        adapter = MusicAdapter(mItems, this, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    private fun loadMusic() {
        val keyword : String = edt_search.text.toString()
        if(keyword.trim()!="")
            fuelUtils.getSearchData(keyword)
        else
            Toasty.error(this, "검색어를 입력해주세요").show()
    }

    fun notifyFinish(musicInfo: String, keyword: String) {
        val arr = JSONObject(musicInfo).getJSONArray("music")
        for (idx: Int in 0 until arr.length()) { // 한개의 음악에 한해
            val item: JSONObject = arr.getJSONObject(idx)
            if (item.getBoolean("isMusic")&&item.getString("title").contains(keyword)) { // 소스가 아니고 음악이면
                val rateArr = item.getJSONArray("rate")
                var isLike = false
                for (idx2: Int in 0 until rateArr.length()) { // 좋아요 한 사람중 자신의 이름을 찾으면 is Like = true
                    var authorID: String? = null
                    val authorObject = rateArr.getJSONObject(idx2)
                    if (authorObject.has("username"))
                        authorID = authorObject.getString("username")
                    if (authorID != null && authorID == PreferencesUtils(this).getData("id"))
                        isLike = true
                }

                if(item.has("artist"))
                    mItems.add(MusicItem(item.getString("_id"),
                            item.getString("title"),
                            DateUtils.fromISO(item.getString("date"))!!,
                            "http://10.80.162.221:3000/" + item.getString("music"),
                            "http://10.80.162.221:3000/" + item.getString("cover"),
                            isLike,
                            item.getString("artist")
                    ))
                else
                    mItems.add(MusicItem(item.getString("_id"),
                            item.getString("title"),
                            DateUtils.fromISO(item.getString("date"))!!,
                            "http://10.80.162.221:3000/" + item.getString("music"),
                            "http://10.80.162.221:3000/" + item.getString("cover"),
                            isLike,
                            "No Artist"
                    ))
            }
        }
        adapter!!.notifyDataSetChanged()
    }

    fun notifyFavoriteFinish(status : Boolean){
        if(!status)
            Toasty.success(this, "Failed to rate")
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra("isPlaying", isPlaying)
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }
}
