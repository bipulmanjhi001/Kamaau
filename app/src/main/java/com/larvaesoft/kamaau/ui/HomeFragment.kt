package com.larvaesoft.kamaau.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.*
import com.larvaesoft.kamaau.model.ConnectionDetector
import com.shinelw.library.ColorArcProgressBar

import larvaesoft.com.kamaau.R

class HomeFragment : Fragment() {

    internal var email: String? = null
    internal var name: String? = null

    private var bar2: ColorArcProgressBar? = null
    private var ok: Int = 0
    lateinit var pref: SharedPreferences


    lateinit var dashboard_connect: CoordinatorLayout
    private var isInternetPresent: Boolean? = false
    lateinit var cd: ConnectionDetector
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.app_bar_dashboard, container, false)

        bar2 = rootView.findViewById<View>(R.id.bar2) as ColorArcProgressBar
        bar2!!.setCurrentValues(0F)

        pref = getActivity().getSharedPreferences("total_save", Context.MODE_PRIVATE)
        ok = pref.getInt("total", ok)

        cd = ConnectionDetector(getActivity())
        isInternetPresent = cd.isConnectingToInternet

        if (isInternetPresent!!) {

            bar2!!.setCurrentValues(ok.toFloat())

        } else {
            // Ask user to connect to Internet
            val snackbar = Snackbar
                    .make(dashboard_connect, "No internet connection ", Snackbar.LENGTH_LONG)
                    .setAction("DISMISS") { }
            val snackbarActionTextView = snackbar.view.findViewById<View>(android.support.design.R.id.snackbar_action) as TextView
            snackbarActionTextView.textSize = 14f

            snackbarActionTextView.setTextColor(Color.RED)
            snackbarActionTextView.setTypeface(snackbarActionTextView.typeface, Typeface.BOLD)

            val sbView = snackbar.view
            val textView = sbView.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            textView.maxLines = 1
            textView.textSize = 14f
            textView.setSingleLine(true)
            textView.setTypeface(null, Typeface.BOLD)
            snackbar.show()
        }

        return rootView;
    }

}