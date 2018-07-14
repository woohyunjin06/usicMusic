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
import android.util.Log.d
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.model.MusicResponse
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.FuelUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_music.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivityForResult
import org.json.JSONArray
import org.json.JSONObject


class MusicActivity : AppCompatActivity(), OnPlayListener {
    var isPlaying = false
    private val gson = Gson()
    override fun onClickPlay(idx: String, title: String, uri: String, btn: ImageView) {
        val play : Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        if(btn.drawable.constantState == play?.constantState){ // 켜기
            btn.imageResource = R.drawable.ic_pause
            isPlaying = true
            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.MESSAGE_KEY, true)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            startService(i)
        }
        else{ //끄기
            btn.imageResource = R.drawable.ic_play
            isPlaying = false
            val i = Intent(this, MusicService::class.java)
            i.putExtra(MusicService.MESSAGE_KEY, false)
            i.putExtra(MusicService.SONG_NAME, title)
            i.putExtra(MusicService.SONG_URL, uri)
            startService(i)
        }
    }
    private val fuelUtils = FuelUtils(this)
    private val mItems = ArrayList<MusicItem>()
    private var adapter : MusicAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        initRecyclerView()
        //addDummyData()
        loadMusic()
    }
    private fun initRecyclerView() { // RecyclerView 기본세팅
        // 변경될 가능성 o : false 로 , 없다면 true.
        list.setHasFixedSize(false)

        adapter = MusicAdapter(mItems, this, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }
    private fun addDummyData(){
        mItems.add(MusicItem("","1번노래","2018. 06. 29","http://10.80.162.221:3000/music/twice.mp3","5:03", "https://images.pexels.com/photos/35807/rose-red-rose-romantic-rose-bloom.jpg",true))
        mItems.add(MusicItem("","2번노래","2018. 06. 25","","4:03", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",false))
        mItems.add(MusicItem("","3번노래","2018. 06. 21","","2:08", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",false))
        mItems.add(MusicItem("","4번노래","2018. 06. 29","","1:49", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",true))
        adapter!!.notifyDataSetChanged()
    }

    private fun loadMusic() {
        fuelUtils.getMusicData()

    }

    fun notifyFinish(musicInfo : String){
        val arr = JSONObject(musicInfo).getJSONArray("music")
        val arrList = ArrayList<MusicResponse>()
        for (idx : Int in 0 until arr.length()){
            val item : JSONObject = arr.getJSONObject(idx)
            arrList.add(MusicResponse(item.getBoolean("isMusic"), item.getString("_id"), item.getString("title"),
                    item.getString("music"), item.getJSONArray("rate"), item.getString("date")))
            if(item.getBoolean("isMusic"))
            mItems.add(MusicItem(item.getString("_id"),
                    item.getString("title"),
                    "2018. 06. 29","http://10.80.162.221:3000/music/twice.mp3","5:03", "https://images.pexels.com/photos/35807/rose-red-rose-romantic-rose-bloom.jpg",true))
        }
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra("isPlaying", isPlaying)
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }
}