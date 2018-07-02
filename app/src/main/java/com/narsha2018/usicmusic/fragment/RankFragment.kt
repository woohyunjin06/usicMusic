package com.narsha2018.usicmusic.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.narsha2018.usicmusic.R
import org.jetbrains.anko.toast

class RankFragment : Fragment() {

    private var v : View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_rank, container, false)

        return v
    }
    fun getLayoutView() : View?{
        return v
    }
}
