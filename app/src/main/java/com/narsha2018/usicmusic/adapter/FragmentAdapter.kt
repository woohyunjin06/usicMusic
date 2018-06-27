package com.narsha2018.usicmusic.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class FragmentAdapter(private val fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    class Holder(private val manager: FragmentManager) {
        private val fragments = ArrayList<Fragment>()

        fun add(f: Fragment): Holder {
            fragments.add(f)
            return this
        }

        fun set(): FragmentAdapter {
            return FragmentAdapter(manager, fragments)
        }
    }
}