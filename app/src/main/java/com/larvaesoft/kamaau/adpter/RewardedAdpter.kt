package com.larvaesoft.kamaau.adpter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList

import larvaesoft.com.kamaau.R


/**
 * Created by Bipul on 25-03-2018.
 */

class RewardedAdpter (val userList: ArrayList<Reward_list>) : RecyclerView.Adapter<RewardedAdpter.ViewHolder>() {
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardedAdpter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reward_list, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RewardedAdpter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])

    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: Reward_list) {
            lateinit var hours_ago_reward: TextView
            lateinit var watch_reward_amt: TextView
            lateinit var date_reward: TextView

            hours_ago_reward = itemView.findViewById(R.id.hours_ago_reward) as TextView
            watch_reward_amt = itemView.findViewById(R.id.watch_reward_amt) as TextView
            date_reward = itemView.findViewById(R.id.date_reward) as TextView
            hours_ago_reward.setText(user.date)
            watch_reward_amt.setText(user.amount)
            date_reward.setText(user.time)
        }
    }
}
