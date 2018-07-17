package com.narsha2018.usicmusic.adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
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
import com.narsha2018.usicmusic.util.PreferencesUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_chart.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.ArrayList

/**
* Created by hyunjin on 2018. 5. 11..
*/
class CommentAdapter(private var mItems: ArrayList<CommentItem>, context : Context) : RecyclerView.Adapter<CommentAdapter.ItemViewHolder>() {

    val contexts : Context = context
    var id: String? = null
    var nick: String? = null
    // 새로운 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chart, parent, false)
        val sharedPreferences : SharedPreferences = contexts.getSharedPreferences("user", Activity.MODE_PRIVATE)
        id = sharedPreferences.getString("id", null)
        nick = sharedPreferences.getString("nick", null)

        return ItemViewHolder(view)
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.name.text = mItems[position].name
        holder.content.text = mItems[position].content
        holder.itemView.setOnClickListener {
            if(holder.name.text == nick){
                contexts.alert("정말로 삭제하시겠습니까?"){
                    yesButton {  } // 삭제
                    noButton {  }
                }.show()
            }
        }
    }

    // 데이터 셋의 크기를 리턴
    override fun getItemCount(): Int {
        return mItems.size
    }

    // 커스텀 뷰홀더
    // binding widgets on item layout
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val content: TextView = itemView.content
        val name: TextView = itemView.name
    }
}

class CommentItem(val cid: String, val content: String, val name: String)