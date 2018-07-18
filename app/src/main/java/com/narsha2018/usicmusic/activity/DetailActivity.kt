package com.narsha2018.usicmusic.activity

import android.os.Bundle
import android.preference.Preference
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.adapter.CommentAdapter
import com.narsha2018.usicmusic.adapter.CommentItem
import com.narsha2018.usicmusic.model.CommentRequest
import com.narsha2018.usicmusic.util.DateUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity(){
    val fuelUtils = FuelUtils(this)
    var id : String ? = null
    var idx : Int = 0
    private val mItems = ArrayList<CommentItem>()
    private var adapter: CommentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        id = intent.getStringExtra("id")
        val uid = PreferencesUtils(this).getData("id")
        fuelUtils.getBoardContent(id!!)
        list.setHasFixedSize(false)
        adapter = CommentAdapter(mItems, this)
        list.adapter = adapter
        post.setOnClickListener { fuelUtils.postData("/board/$id/comment", CommentRequest(uid, commentInput.text.toString()), false);
        commentInput.setText("")}
        list.layoutManager = LinearLayoutManager(this)
    }
    fun notifyFinish(result : String){
        mItems.clear()
        val obj = JSONObject(result)
        val message = obj.getJSONObject("message")
        titles.text = message.getString("title")
        writer.text = message.getString("writer")
        date.text = DateUtils.fromISO(message.getString("date"))
        content.text = message.getString("content")
        val comments = obj.getJSONObject("message").getJSONArray("comments")

        for(idx in 0 until comments.length()){
            var comment = comments.getJSONObject(idx)
            mItems.add(CommentItem(id!! ,comment.getString("_id"),comment.getString("comment"),comment.getString("name")))
        }
        adapter!!.notifyDataSetChanged()
    }
    fun notifyCommentFinish(result: String){
        fuelUtils.getBoardContent(id!!)
        val objects = JSONObject(result)
        if(objects.getString("status")!="200")
            Toasty.success(this, "댓글 작성을 실패했습니다.")
        else
            fuelUtils.getBoardContent(id!!)
    }
}