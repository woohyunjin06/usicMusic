package com.narsha2018.usicmusic.adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.widget.ImageViewCompat
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
import com.narsha2018.usicmusic.util.FuelUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_chart.view.*
import java.util.*

/**
* Created by hyunjin on 2018. 5. 11..
*/
class MusicAdapter(private var mItems: ArrayList<MusicItem>, context : Context, var listener: OnPlayListener, var isSearch: Boolean) : RecyclerView.Adapter<MusicAdapter.ItemViewHolder>() {

    val contexts : Context = context
    var id: String? = null
    // 새로운 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chart, parent, false)
        val sharedPreferences : SharedPreferences = contexts.getSharedPreferences("user", Activity.MODE_PRIVATE)
        id = sharedPreferences.getString("id", null)

        return ItemViewHolder(view)
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var isLike = false
        holder.musicTitle.text = mItems[position].musicTitle
        holder.date.text = mItems[position].date
        holder.artist.text = mItems[position].artist
        Glide.with(contexts)
                .load(mItems[position].thumbnailUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_launcher) //로딩
                        .error(R.drawable.ic_launcher) //실패
                        .fallback(R.drawable.ic_launcher)) //없음
                .into(holder.thumbnail)

        if(mItems[position].isLike) {
            isLike = true
            ImageViewCompat.setImageTintList(holder.like, ColorStateList.valueOf(Color.parseColor("#ff0000"))) // set tint
        } else{
            isLike = false
            ImageViewCompat.setImageTintList(holder.like, ColorStateList.valueOf(Color.parseColor("#e9e9e9"))) // set tint
        }


        holder.like.setOnClickListener {

            if(isLike){ // 좋아요 취소
                ImageViewCompat.setImageTintList(holder.like, ColorStateList.valueOf(Color.parseColor("#e9e9e9")))
                FuelUtils(contexts).delete(mItems[position].idx, FuelUtils.DeleteEnum.SearchFavorite.takeIf { isSearch }
                        ?: FuelUtils.DeleteEnum.MusicFavorite)
            }
            else{ // 좋아요
                ImageViewCompat.setImageTintList(holder.like, ColorStateList.valueOf(Color.parseColor("#ff0000")))
                FuelUtils(contexts).postData(mItems[position].idx, isSearch, FuelUtils.PostEnum.SearchFavorite.takeIf { isSearch }
                        ?: FuelUtils.PostEnum.MusicFavorite)
            }
            isLike = !isLike
        }
        holder.play.setOnClickListener {
            listener.onClickPlay(mItems[position].idx,mItems[position].musicTitle, mItems[position].musicUri ,holder.play)
        }
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
        val artist: TextView = itemView.artist
        val thumbnail: CircleImageView = itemView.thumbnail
        val like: ImageView = itemView.btn_like
        val play: ImageView = itemView.btn_play
    }
}

class MusicItem(val idx: String, val musicTitle: String, val date: String,val musicUri: String, val thumbnailUri: String, val isLike: Boolean, val artist: String)