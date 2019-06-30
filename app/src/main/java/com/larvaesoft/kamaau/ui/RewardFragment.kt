package com.larvaesoft.kamaau.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.larvaesoft.kamaau.adpter.DividerItemDecoration
import com.larvaesoft.kamaau.adpter.Reward_list
import com.larvaesoft.kamaau.adpter.RewardedAdpter

import larvaesoft.com.kamaau.R
import java.text.SimpleDateFormat
import java.util.*

class RewardFragment : Fragment() {

    lateinit var display_date: TextView
    lateinit var pref: SharedPreferences

    internal var email: String? = null
    internal var name: String? = null
    internal var getTotal = 0

    lateinit var adapter: RewardedAdpter
    lateinit var reward_list_recycle: RecyclerView
    lateinit var reward_lists: ArrayList<Reward_list>
    lateinit var refdb: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.app_bar_rewards, container, false)
        display_date = rootView.findViewById<View>(R.id.display_date) as TextView
        reward_list_recycle = rootView.findViewById<View>(R.id.reward_list) as RecyclerView

        refdb = FirebaseDatabase.getInstance().getReference("rewards")
        CallValues()

        return rootView;
    }
    fun CallValues() {
        val cal = Calendar.getInstance()
        val month_date = SimpleDateFormat("MMMM")
        val month_name = month_date.format(cal.time)

        display_date.text = month_name
        reward_lists = ArrayList()
        adapter = RewardedAdpter(reward_lists);
        val mLayoutManager = LinearLayoutManager(getActivity())
        reward_list_recycle.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
        reward_list_recycle.addItemDecoration(DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL))
        reward_list_recycle.adapter = adapter
        try {
            refdb.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    reward_lists.clear()
                    getTotal=0;
                    for (uniqueKeySnapshot in dataSnapshot.children) {
                        //Loop 1 to go through all the child nodes of rewards
                        val date = uniqueKeySnapshot.child("date").value!!.toString()
                        val coins = uniqueKeySnapshot.child("rewards").value!!.toString()
                        val time = uniqueKeySnapshot.child("time").value!!.toString()
                      //  val total = uniqueKeySnapshot.child("total").value!!.toString()

                        getTotal = getTotal + Integer.parseInt(coins)
                        val reward_list = Reward_list()
                        reward_list.date = date
                        reward_list.amount = coins
                        reward_list.time = time
                        reward_lists.add(reward_list)

                        adapter.notifyDataSetChanged()
                    }

                    pref = getActivity().getSharedPreferences("total_save", Context.MODE_PRIVATE)
                    val logineditor: SharedPreferences.Editor
                    logineditor = pref.edit()
                    logineditor.clear()
                    logineditor.putInt("total", getTotal)
                    logineditor.apply()

                }

                override fun onCancelled(databaseError: DatabaseError) {

                    Toast.makeText(getActivity(), "data not found", Toast.LENGTH_LONG).show()

                }
            })

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}