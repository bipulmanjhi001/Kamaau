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
import android.widget.Toast

import larvaesoft.com.kamaau.R

class Payout : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var paypal_active: LinearLayout
    lateinit var paytm_active: LinearLayout
    internal var email: String? = null
    internal var name: String? = null
    lateinit var email_field: TextView
    lateinit var name_field: TextView

    lateinit var uuid: String
    lateinit var preferences: SharedPreferences
    lateinit var add_bank_account: LinearLayout
    lateinit var show_paytm: LinearLayout
    lateinit var show_paypal: LinearLayout
    lateinit var bank_account_form: LinearLayout

    lateinit var code:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payout)
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

        try {
            email_field.text = email
            name_field.text = name
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        GetValues()
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
            val intent = Intent(this@Payout, ProfileView::class.java)
            startActivity(intent)

        } else if (id == R.id.nav_payout) {

            Toast.makeText(this@Payout, "Choose another option", Toast.LENGTH_SHORT).show()

        } else if (id == R.id.nav_share) {

            Share_us()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun GetValues() {
        bank_account_form = findViewById<View>(R.id.bank_account_form) as LinearLayout
        paypal_active = findViewById<View>(R.id.paypal_active) as LinearLayout
        paytm_active = findViewById<View>(R.id.paytm_active) as LinearLayout

        show_paypal = findViewById<View>(R.id.show_paypal) as LinearLayout
        show_paypal.setOnClickListener {
            bank_account_form.visibility = View.GONE
            paypal_active.visibility = View.VISIBLE
            paytm_active.visibility = View.GONE
        }
        show_paytm = findViewById<View>(R.id.show_paytm) as LinearLayout
        show_paytm.setOnClickListener {
            bank_account_form.visibility = View.GONE
            paypal_active.visibility = View.GONE
            paytm_active.visibility = View.VISIBLE
        }
        add_bank_account = findViewById<View>(R.id.add_bank_account) as LinearLayout
        add_bank_account.setOnClickListener {
            bank_account_form.visibility = View.VISIBLE
            paypal_active.visibility = View.GONE
            paytm_active.visibility = View.GONE
        }
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
