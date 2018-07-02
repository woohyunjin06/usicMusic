package com.narsha2018.usicmusic.fragment

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.adapter.MusicAdapter
import com.narsha2018.usicmusic.adapter.MusicItem
import kotlinx.android.synthetic.main.fragment_chart.view.*
import java.net.URI

class ChartFragment : Fragment(){

    private val mItems = ArrayList<MusicItem>()
    private var adapter : MusicAdapter? = null
    private var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_chart, container, false)
        initRecyclerView()
        addDummyData()

        return v
    }
    private fun initRecyclerView() { // RecyclerView 기본세팅
        // 변경될 가능성 o : false 로 , 없다면 true.
        v!!.list.setHasFixedSize(false)

        adapter = MusicAdapter(mItems, activity!!, activity as OnPlayListener)
        v!!.list.adapter = adapter
        v!!.list.layoutManager = LinearLayoutManager(activity)
    }
    private fun addDummyData(){
        mItems.add(MusicItem(0,"1번노래","2018. 06. 29","","5:03", "https://images.pexels.com/photos/35807/rose-red-rose-romantic-rose-bloom.jpg",true))
        mItems.add(MusicItem(1,"2번노래","2018. 06. 25","","4:03", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",false))
        mItems.add(MusicItem(2,"3번노래","2018. 06. 21","","2:08", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",false))
        mItems.add(MusicItem(3,"4번노래","2018. 06. 29","","1:49", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg",true))
        adapter!!.notifyDataSetChanged()
    }
}
