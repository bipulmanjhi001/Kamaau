package com.larvaesoft.kamaau.Activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import larvaesoft.com.kamaau.R
import java.util.*

class Transaction : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var email_field: TextView
    lateinit var name_field: TextView
    internal var email: String? = null
    internal var name: String? = null
    lateinit var time: String

    lateinit var code:String
    lateinit var uuid: String
    lateinit var preferences: SharedPreferences
    lateinit var add_bank_account: LinearLayout
    lateinit var show_ref: LinearLayout
    lateinit var show_adds: LinearLayout
    lateinit var sorry_img: LinearLayout
    lateinit var referral_amt: TextView
    lateinit var calc_date: TextView
    lateinit var calc_exp_date: TextView
    lateinit var adds_calc_amt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val header = navigationView.getHeaderView(0)

        email_field = header.findViewById<View>(R.id.email_id) as TextView
        name_field = header.findViewById<View>(R.id.name) as TextView

        preferences = applicationContext.getSharedPreferences("register_save", Context.MODE_PRIVATE)
        email = preferences.getString("email", "")
        name = preferences.getString("name", "")
        uuid=preferences.getString("uuid","")
        code=preferences.getString("code","")

        add_bank_account = findViewById<View>(R.id.add_bank_accounts) as LinearLayout
        add_bank_account.setOnClickListener {
            val intent = Intent(this@Transaction, Payout::class.java)
            startActivity(intent)
        }

        try {
            email_field.text = email
            name_field.text = name
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        show_adds = findViewById<View>(R.id.show_adds) as LinearLayout
        show_ref = findViewById<View>(R.id.show_ref) as LinearLayout
        referral_amt = findViewById<View>(R.id.referral_amt) as TextView
        calc_date = findViewById<View>(R.id.calc_date) as TextView
        calc_exp_date = findViewById<View>(R.id.calc_exp_date) as TextView
        adds_calc_amt = findViewById<View>(R.id.adds_calc_amt) as TextView
        sorry_img = findViewById<View>(R.id.sorry_img) as LinearLayout

    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_profile) {

            val intent = Intent(this@Transaction, ProfileView::class.java)
            startActivity(intent)

        } else if (id == R.id.nav_payout) {

            val intent = Intent(this@Transaction, Payout::class.java)
            startActivity(intent)

        } else if (id == R.id.nav_share) {
            Share_us()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun Share_us() {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Share & Earn")
            var sAux = "I am using kamaau app to make some extra money." +
                    "Use kamaau app to make some extra money by watching" +
                    " ad videos.Use" + code + "to join now. "
            sAux = sAux + "http://www.larvaesoft.com \n\n"
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            startActivity(Intent.createChooser(i, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }
}
