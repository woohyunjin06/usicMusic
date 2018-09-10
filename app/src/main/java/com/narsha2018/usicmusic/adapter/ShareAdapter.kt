package com.narsha2018.usicmusic.adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.activity.DetailActivity
import kotlinx.android.synthetic.main.item_share.view.*
import org.jetbrains.anko.startActivity
import java.util.ArrayList

/**
* Created by HyunJin on 2018. 5. 11..
*/
class ShareAdapter(private var mItems: ArrayList<ShareItem>, val context : Context) : RecyclerView.Adapter<ShareAdapter.ItemViewHolder>() {

    var id: String? = null
    // 새로운 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_share, parent, false)
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("user", Activity.MODE_PRIVATE)
        id = sharedPreferences.getString("id", null)

        return ItemViewHolder(view)
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.no.text = mItems[position].no.toString()
        holder.title.text = mItems[position].title
        holder.writer.text = mItems[position].writer
        holder.date.text = mItems[position].date
        holder.itemView.setOnClickListener {
            context.startActivity<DetailActivity>("id" to mItems[position].id)
        }
    }

    // 데이터 셋의 크기를 리턴
    override fun getItemCount(): Int {
        return mItems.size
    }

    // 커스텀 뷰홀더
    // binding widgets on item layout
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val no: TextView = itemView.no
        val title: TextView = itemView.title
        val writer: TextView = itemView.writer
        val date: TextView = itemView.date
    }
}

class ShareItem(val id: String, val no: Int, val title: String,val writer: String, val date: String)