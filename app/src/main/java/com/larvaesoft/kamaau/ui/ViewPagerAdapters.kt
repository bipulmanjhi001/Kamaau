package com.larvaesoft.kamaau.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import java.util.ArrayList

/**
 * Created by LSSIN005 on 04-04-2018.
 */
class ViewPagerAdapters(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private val mFragmentList = ArrayList<Fragment>()
    
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

}