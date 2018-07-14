package com.narsha2018.usicmusic.activity

import android.os.Bundle
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
import org.jetbrains.anko.toast
import java.util.*

class FavoriteActivity : AppCompatActivity(), OnPlayListener {
    override fun onClickPlay(idx: String, title: String, uri: String, btn: ImageView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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
        addDummyData()
    }
    private fun addDummyData(){
        mItems.add(FavoriteItem("1번노래","2018. 06. 29","", "https://images.pexels.com/photos/35807/rose-red-rose-romantic-rose-bloom.jpg"))
        mItems.add(FavoriteItem("2번노래","2018. 06. 25","", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"))
        mItems.add(FavoriteItem("3번노래","2018. 06. 21","", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"))
        mItems.add(FavoriteItem("4번노래","2018. 06. 29","", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"))
        adapter!!.notifyDataSetChanged()
    }
}