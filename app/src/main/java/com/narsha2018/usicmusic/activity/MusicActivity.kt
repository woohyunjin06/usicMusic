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
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_music.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivityForResult


class MusicActivity : AppCompatActivity(), OnPlayListener {
    var isPlaying = false
    override fun onClickPlay(idx: Int, title: String, uri: String, btn: ImageView) {
        val play : Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_play)
        if(btn.drawable.constantState == play?.constantState){ // 켜기
            btn.imageResource = R.drawable.ic_pause
            isPlaying = true
        }
        else{ //끄기
            btn.imageResource = R.drawable.ic_play
            isPlaying = false
        }
        music.onClick { startActivityForResult<MusicActivity>(1) }
    }

    private val mItems = ArrayList<MusicItem>()
    private var adapter : MusicAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        initRecyclerView()
        addDummyData()
    }
    private fun initRecyclerView() { // RecyclerView 기본세팅
        // 변경될 가능성 o : false 로 , 없다면 true.
        list.setHasFixedSize(false)

        adapter = MusicAdapter(mItems, this, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }
    private fun addDummyData(){
        mItems.add(MusicItem(0,"1번노래","2018. 06. 29","","5:03", "https://images.pexels.com/photos/35807/rose-red-rose-romantic-rose-bloom.jpg",true))
        mItems.add(MusicItem(1,"2번노래","2018. 06. 25","","4:03", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",false))
        mItems.add(MusicItem(2,"3번노래","2018. 06. 21","","2:08", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",false))
        mItems.add(MusicItem(3,"4번노래","2018. 06. 29","","1:49", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",true))
        adapter!!.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra("isPlaying",isPlaying)
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }
}