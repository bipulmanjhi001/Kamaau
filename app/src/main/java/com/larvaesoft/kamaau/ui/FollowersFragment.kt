package com.larvaesoft.kamaau.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import larvaesoft.com.kamaau.R

/**
 * Created by LSSIN005 on 07-04-2018.
 */
class FollowersFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.app_bar_followers, container, false)

     return rootView;
    }

}