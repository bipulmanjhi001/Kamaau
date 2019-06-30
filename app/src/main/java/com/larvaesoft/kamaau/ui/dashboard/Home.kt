package com.larvaesoft.kamaau.ui.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_home.*
import larvaesoft.com.kamaau.R

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar

import android.view.MenuItem;
import android.view.View
import android.widget.TextView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.larvaesoft.kamaau.Activity.ProfileView
import com.larvaesoft.kamaau.Activity.Transaction
import com.larvaesoft.kamaau.model.ConnectionDetector
import com.larvaesoft.kamaau.ui.*
import com.larvaesoft.kamaau.ui.utils.Constants
import larvaesoft.com.kamaau.BuildConfig
import timber.log.Timber

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //Fragments
    var homeFragment: HomeFragment? = null
    var rewardFragment: RewardFragment? = null
    var followersFragment: FollowersFragment? = null
    var adsvideoFragment: AdsVideoFragment? = null
    var socialFragment: SocialFragment? = null

    internal var email: String? = null
    internal var name: String? = null

    internal var uuid: String? = null
    internal var getphoto: String? = null
    lateinit var email_field: TextView
    lateinit var name_field: TextView

    lateinit var preferences: SharedPreferences
    private var isInternetPresent: Boolean? = false
    lateinit var cd: ConnectionDetector
    lateinit var home_connect: CoordinatorLayout
    lateinit var code:String
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        home_connect = findViewById<View>(R.id.home_connect) as CoordinatorLayout

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val header = navigationView.getHeaderView(0)

        remoteConfig = FirebaseRemoteConfig.getInstance()
        email_field = header.findViewById<View>(R.id.email_id) as TextView
        name_field = header.findViewById<View>(R.id.name) as TextView

        nav_view.setNavigationItemSelectedListener(this)
        preferences = applicationContext.getSharedPreferences("register_save", Context.MODE_PRIVATE)
        email = preferences.getString("email", "")
        name = preferences.getString("name", "")
        uuid=preferences.getString("uuid","")
        code=preferences.getString("your_ref_code","")
        getphoto=preferences.getString("getphoto","")

        try {
            email_field.text = email
            name_field.text = name

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        //Initializing viewPager
        val viewPager:ViewPager = findViewById(R.id.viewpager)
        var prevMenuItem: MenuItem? = null
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation)

        bottomNavigation.setSelectedItemId(R.id.nav_home);
        cd = ConnectionDetector(applicationContext)
        isInternetPresent = cd.isConnectingToInternet

        if (isInternetPresent!!) {
            bottomNavigation.setOnNavigationItemSelectedListener(
                    BottomNavigationView.OnNavigationItemSelectedListener { item ->
                        when (item.itemId) {

                            R.id.nav_rewards -> {
                                viewPager.currentItem = 0
                                toolbar.setTitle("Rewards");
                            }
                            R.id.nav_follow->{
                                viewPager.currentItem = 1
                                toolbar.setTitle("Followers");
                            }

                            R.id.nav_home -> {
                                viewPager.currentItem = 2
                                toolbar.setTitle("Dashboard");
                            }
                            R.id.nav_activity ->{
                                viewPager.currentItem = 3
                                toolbar.setTitle("Activity");
                            }

                            R.id.nav_social -> {
                                viewPager.currentItem = 4
                                toolbar.setTitle("Feedback");
                            }
                        }
                        false
                    })

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }
                override fun onPageSelected(position: Int) {
                    if (prevMenuItem != null) {
                       prevMenuItem!!.setChecked(false)
                    } else {
                        bottomNavigation.getMenu().getItem(1).isChecked = false
                    }
                   // Log.d("page", "onPageSelected: $position")
                    if(position == 0){
                        toolbar.setTitle("Rewards");
                    }else if(position == 1) {
                        toolbar.setTitle("Followers");
                    } else if(position == 2){
                        toolbar.setTitle("Dashboard");
                    }else if(position == 3){
                        toolbar.setTitle("Activity");
                    }else if(position == 4){
                        toolbar.setTitle("Feedback");
                    }
                    bottomNavigation.getMenu().getItem(position).isChecked = true
                    prevMenuItem = bottomNavigation.getMenu().getItem(position)

                }
                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            setupViewPager(viewPager)

           // checkForUpdate()
        }else{
            // Ask user to connect to Internet
            val snackbar = Snackbar
                    .make(home_connect, "No internet connection ", Snackbar.LENGTH_LONG)
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
        }
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        rewardFragment = RewardFragment()
        followersFragment= FollowersFragment()
        homeFragment = HomeFragment();
        adsvideoFragment = AdsVideoFragment()
        socialFragment = SocialFragment()
        adapter.addFragment(rewardFragment)
        adapter.addFragment(followersFragment)
        adapter.addFragment(homeFragment)
        adapter.addFragment(adsvideoFragment)
        adapter.addFragment(socialFragment)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(2);
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {

                val intent = Intent(this@Home, ProfileView::class.java)
                startActivity(intent)

            }
            R.id.nav_payout -> {
                val intent = Intent(this@Home, Transaction::class.java)
                startActivity(intent)

            }
            R.id.nav_share -> {
                Share_us()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
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
    private fun checkForUpdate() {
        val availableVersion = remoteConfig.getString(Constants.latestVersion)
        Timber.d("available version is $availableVersion")
        if (availableVersion.toInt() > BuildConfig.VERSION_CODE) {
            showUpdateDialog()
        }
    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Update")
        builder.setMessage("Update is available. Download to continue")
        builder.setPositiveButton("Update", { _, _ ->
            //browse(Constants.playStoreURL)
        })
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        dialog.setOnDismissListener {
            finish()
        }
        Timber.d("Update dialog Shown")
    }
}
