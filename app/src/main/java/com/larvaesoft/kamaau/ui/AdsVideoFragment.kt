package com.larvaesoft.kamaau.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.larvaesoft.kamaau.model.CoinsDataSet

import larvaesoft.com.kamaau.R
import java.text.SimpleDateFormat
import java.util.*

class AdsVideoFragment : Fragment() {

    internal var email: String? = null
    internal var name: String? = null
    lateinit var getdate: String
    lateinit var gettime: String

    lateinit var activity: LinearLayout
    lateinit var video_container:LinearLayout
    private var mRewardedVideoAd: RewardedVideoAd? = null
    lateinit var play_video: Button

    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null
    lateinit var rewardamt: String

    lateinit var preferences: SharedPreferences
    lateinit var uuid: String
    lateinit var watched_videos:TextView
    lateinit var watching_videos:TextView
    var watchingless:Int = 5
    var watchingmore:Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.app_bar_video, container, false)

        MobileAds.initialize(getActivity(),"ca-app-pub-3940256099942544~3347511713");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());

        preferences = getActivity().getSharedPreferences("register_save", Context.MODE_PRIVATE)
        uuid=preferences.getString("uuid","")

        play_video = rootView.findViewById<View>(R.id.btn_show) as Button
        video_container=rootView.findViewById<View>(R.id.video_container) as LinearLayout
        watched_videos=rootView.findViewById<View>(R.id.watched_videos) as TextView
        watching_videos=rootView.findViewById<View>(R.id.watching_videos) as TextView

        startLoading()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance!!.getReference("rewards")

        mRewardedVideoAd!!.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewarded(rewardItem: RewardItem) {
                GetTime()
                rewardamt = rewardItem.amount.toString()
                val dub = rewardItem.amount
                var secdub = 0
                secdub = dub + secdub
                val total = secdub.toString()

                try {
                    if (TextUtils.isEmpty(userId)) {
                        coinsadd(getdate, rewardamt, gettime, uuid)

                        Toast.makeText(getActivity(), "onRewarded : $rewardamt", Toast.LENGTH_SHORT).show()

                        watchingless--;
                        watching_videos.setText(watchingless.toString())

                        watchingmore++;
                        watched_videos.setText(watchingmore.toString())
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }

            }

            override fun onRewardedVideoAdLeftApplication() {

            }

            override fun onRewardedVideoAdClosed() {

            }

            override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
                Toast.makeText(getActivity(), "AdMob video is not ready.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdLoaded() {
                play_video.visibility=View.VISIBLE
                video_container.visibility=View.VISIBLE
            }

            override fun onRewardedVideoAdOpened() {
            }

            override fun onRewardedVideoStarted() {
            }
        }
        loadRewardedVideoAd()

        return rootView
    }

    fun startLoading(){
        play_video.setOnClickListener {
            play_video.isEnabled = false
            if (mRewardedVideoAd!!.isLoaded) {
                mRewardedVideoAd!!.show()
            }
        }
    }

    fun GetTime() {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val sdf2 = SimpleDateFormat("hh:mm:ss")
        getdate = sdf.format(c.time)
        gettime = sdf2.format(c.time)

    }
    private fun loadRewardedVideoAd() {
        mRewardedVideoAd!!.loadAd(getString(R.string.rewarded_video),
                AdRequest.Builder().build())

        showRewardedVideo()
    }

    private fun showRewardedVideo() {
        if (mRewardedVideoAd!!.isLoaded) {
            mRewardedVideoAd!!.show()
        }
    }
        override fun onResume() {
            mRewardedVideoAd!!.resume(getActivity())
            super.onResume()
        }

    override fun onPause() {
        mRewardedVideoAd!!.pause(getActivity())
        super.onPause()
    }

     override fun onDestroy() {
        mRewardedVideoAd!!.destroy(getActivity())
        super.onDestroy()
    }

    private fun coinsadd(date: String, rewards: String, time: String, uuid: String) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase!!.push().key
        }
        val coinsPOJO = CoinsDataSet(date, rewards, time, uuid)
        mFirebaseDatabase!!.child(userId!!).setValue(coinsPOJO)
    }
}

