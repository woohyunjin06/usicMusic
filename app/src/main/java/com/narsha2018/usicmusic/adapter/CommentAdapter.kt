package com.narsha2018.usicmusic.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import kotlinx.android.synthetic.main.item_comment.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.*

/**
* Created by hyunjin on 2018. 5. 11..
*/
class CommentAdapter(private var mItems: ArrayList<CommentItem>, context : Context) : RecyclerView.Adapter<CommentAdapter.ItemViewHolder>() {

    private val contexts : Context = context
    private val fuelUtils = FuelUtils(context)
    var id: String? = null
    var nick: String? = null
    // 새로운 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        id = PreferencesUtils(contexts).getData("id")
        nick = PreferencesUtils(contexts).getData("nick")

        return ItemViewHolder(view)
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.name.text = mItems[position].name
        holder.content.text = mItems[position].content
        holder.itemView.setOnClickListener {
            if(holder.name.text == id){
                contexts.alert("정말로 삭제하시겠습니까?"){
                    also {
                        ctx.setTheme(R.style.CustomAlertDialog)
                    }
                    yesButton {
                        fuelUtils.delete("/board/${mItems[position].bid}/comment/${mItems[position].cid}", FuelUtils.DeleteEnum.Comment)
                    mItems.removeAt(position)
                    notifyDataSetChanged()} // 삭제
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

class CommentItem(val bid: String, val cid: String, val content: String, val name: String)