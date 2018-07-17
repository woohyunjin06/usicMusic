package com.narsha2018.usicmusic.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.narsha2018.usicmusic.R
import com.narsha2018.usicmusic.adapter.MusicAdapter
import com.narsha2018.usicmusic.adapter.MusicItem
import android.app.Activity
import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ImageView
import com.narsha2018.usicmusic.`interface`.OnPlayListener
import com.narsha2018.usicmusic.adapter.ShareAdapter
import com.narsha2018.usicmusic.adapter.ShareItem
import com.narsha2018.usicmusic.service.MusicService
import com.narsha2018.usicmusic.util.DateUtils
import com.narsha2018.usicmusic.util.FuelUtils
import com.narsha2018.usicmusic.util.PreferencesUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_music.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class CommunityActivity : AppCompatActivity() {
    var progressDialog : ProgressDialog? = null


    var btn_prev : ImageView? = null
    private var isPlaying = false
    var titles : String? = null
    var uris : String? = null

    private val fuelUtils = FuelUtils(this)
    private val mItems = ArrayList<ShareItem>()
    private var adapter: ShareAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        initRecyclerView()
        Toasty.Config.reset()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        loadMusic()
    }

    private fun initRecyclerView() { // RecyclerView 기본세팅
        // 변경될 가능성 o : false 로 , 없다면 true.
        list.setHasFixedSize(false)

        adapter = ShareAdapter(mItems, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    private fun loadMusic() {
        progressDialog?.show()
        doAsync {
            fuelUtils.getShare()
        }
    }

    fun notifyFinish(musicInfo: String) {
        doAsync {
            val arr = JSONObject(musicInfo).getJSONArray("boards")
            for (idx: Int in 0 until arr.length()) { // 한개의 게시물에 한해
                val item: JSONObject = arr.getJSONObject(idx)
                mItems.add(ShareItem(item.getString("_id"),arr.length()-idx, item.getString("title"),
                        item.getString("writer"), DateUtils.fromISO(item.getString("date"))!!))
            }
            uiThread {
                adapter!!.notifyDataSetChanged()
                progressDialog?.dismiss()
            }
        }
    }


    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra("isPlaying", isPlaying)
        returnIntent.putExtra("songUrl", uris)
        returnIntent.putExtra("songTitle", titles)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
