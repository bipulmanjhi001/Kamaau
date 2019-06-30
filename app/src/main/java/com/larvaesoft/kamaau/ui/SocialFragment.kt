package com.larvaesoft.kamaau.ui


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import com.larvaesoft.kamaau.model.Feedbackdata

import larvaesoft.com.kamaau.R
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast

class SocialFragment : Fragment() {

    lateinit var send_social: LinearLayout
    internal var email: String? = null
    internal var name: String? = null
    lateinit var getfeedback: String
    lateinit var feedback: String

    lateinit var rate_us: RatingBar
    lateinit var feedback_social: EditText

    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null

    private val TAG = SocialFragment::class.java.simpleName
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.app_bar_social, container, false)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance!!.getReference("feedback")
        feedback_social = rootView.findViewById<View>(R.id.feedback_social) as EditText
        send_social = rootView.findViewById<View>(R.id.send_social) as LinearLayout

        send_social.setOnClickListener {
            var cancel = false
            var focusView: View? = null
            if (TextUtils.isEmpty(feedback_social.text.toString())) {
                feedback_social.error = "Required field!"
                focusView = feedback_social
                cancel = true
            }
            if (cancel) {
                focusView!!.requestFocus()
            } else {
                GetData()
            }
        }
        rate_us = rootView.findViewById<View>(R.id.rating_us) as RatingBar

       rate_us.setOnClickListener {
           Rate_us()
       }
        return rootView;
    }

    fun GetData() {

            feedback = feedback_social.text.toString()
            if (TextUtils.isEmpty(userId)) {

                userRating(feedback)
                Toast.makeText(getActivity(), "Thank you for your feedback.", Toast.LENGTH_LONG).show()

            } else {
                updateRating(feedback)
                Toast.makeText(getActivity(), "Feedback Updated succeed.", Toast.LENGTH_LONG).show()
            }
            feedback_social.setText("")
    }

        fun Rate_us() {
            val uri = Uri.parse("market://details?id=" + getActivity().packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().packageName)))
            }
        }
        private fun userRating(feed: String) {
            // TODO
            // In real apps this userId should be fetched
            // by implementing firebase auth
            if (TextUtils.isEmpty(userId)) {
                userId = mFirebaseDatabase!!.push().key
            }

            val feedbackdata = Feedbackdata(feed)
            mFirebaseDatabase!!.child(userId!!).setValue(feedbackdata)
            addUserChangeListener()

        }

        /**
         * User data change listener
         */
        private fun addUserChangeListener() {
            // User data change listener
            mFirebaseDatabase!!.child(userId!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val feedbackdata = dataSnapshot.getValue(Feedbackdata::class.java)

                    // Check for null
                    if (feedbackdata == null) {
                        Log.e(TAG, "Feedback data is null!")
                        return
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.e(TAG, "Failed to get data", error.toException())
                }
            })
        }

        private fun updateRating(feed: String) {
            if (!TextUtils.isEmpty(feedback))
                mFirebaseDatabase!!.child(userId!!).child("feedback").setValue(feed)
        }

    }
