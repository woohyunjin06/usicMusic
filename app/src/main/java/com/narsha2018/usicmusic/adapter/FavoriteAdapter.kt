package com.narsha2018.usicmusic.adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_favorite.view.*
import java.util.ArrayList

/**
* Created by hyunjin on 2018. 5. 11..
*/
class FavoriteAdapter(private var mItems: ArrayList<FavoriteItem>, context : Context, var listener: OnPlayListener) : RecyclerView.Adapter<FavoriteAdapter.ItemViewHolder>() {

    val contexts : Context = context
    var id: String? = null
    // 새로운 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        val sharedPreferences : SharedPreferences = contexts.getSharedPreferences("user", Activity.MODE_PRIVATE)
        id = sharedPreferences.getString("id", null)

        return ItemViewHolder(view)
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.musicTitle.text = mItems[position].musicTitle

        holder.date.text = mItems[position].date
        holder.playTime.text = mItems[position].playTime
        Glide.with(contexts)
                .load(mItems[position].thumbnailUri)
                .apply(RequestOptions()
                        .placeholder(R.mipmap.ic_launcher) //로딩
                        .error(R.mipmap.ic_launcher) //실패
                        .fallback(R.mipmap.ic_launcher)) //없음
                .into(holder.thumbnail)
    }

    // 데이터 셋의 크기를 리턴
    override fun getItemCount(): Int {
        return mItems.size
    }

    // 커스텀 뷰홀더
    // binding widgets on item layout
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val musicTitle: TextView = itemView.musicTitle
        val date: TextView = itemView.date
        val thumbnail: CircleImageView = itemView.thumbnail
        val playTime: TextView = itemView.time
        val play: ImageView = itemView.btn_play
    }
}

class FavoriteItem(val musicTitle: String, val date: String,val musicUri: String, val playTime: String, val thumbnailUri: String)