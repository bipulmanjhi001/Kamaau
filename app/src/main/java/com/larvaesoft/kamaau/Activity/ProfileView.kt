package com.larvaesoft.kamaau.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.larvaesoft.kamaau.ui.dashboard.Home

import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_register.*
import larvaesoft.com.kamaau.R

class ProfileView : AppCompatActivity() {

    internal var select_image: CircleImageView? = null
    lateinit var refdb: DatabaseReference
    lateinit var name_field: TextView
    lateinit var email_field: TextView
    lateinit var phone_field: EditText

    lateinit var preferences: SharedPreferences
    lateinit var phone:String
    lateinit var code:String
    lateinit var name:String
    lateinit var uuid:String
    lateinit var set_referral_code:TextView
    lateinit var share_us:ImageView
    lateinit var profile_back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        preferences = applicationContext.getSharedPreferences("register_save", Context.MODE_PRIVATE)
        val images = preferences.getString("getphoto", "")
        uuid = preferences.getString("uuid", "")
        name = preferences.getString("name", "")
        phone = preferences.getString("phone", "")
        code = preferences.getString("your_ref_code", "")

        refdb = FirebaseDatabase.getInstance().reference

        select_image = findViewById(R.id.profile_view_image)

        try {
            if (!images.isEmpty()) {
                Glide.with(applicationContext)
                        .load(images)
                        .thumbnail(0.5f)
                        .into(select_image)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        name_field = findViewById(R.id.profile_name)
        email_field = findViewById(R.id.profile_email)
        phone_field = findViewById(R.id.profile_phone)
        profile_back=findViewById(R.id.profile_back)
        profile_back.setOnClickListener {
            val intent = Intent(this@ProfileView, Home::class.java)
            startActivity(intent)
            finish()
        }

        share_us = findViewById(R.id.share_us)
        share_us.setOnClickListener { Share_us() }
        set_referral_code = findViewById(R.id.set_referral_code)

            name_field.setText(name)
            phone_field.setText(phone)
        set_referral_code.setText(code)

        if (set_referral_code.text.isEmpty()) {
            try {
                refdb.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (uniqueKeySnapshot in dataSnapshot.children) {

                            if (uniqueKeySnapshot.key == uuid) {
                                //Loop 1 to go through all the child nodes of rewards
                                val email = uniqueKeySnapshot.child("email").value.toString()
                                val name = uniqueKeySnapshot.child("name").value.toString()
                                val phone = uniqueKeySnapshot.child("phone").value.toString()
                                val your_refer = uniqueKeySnapshot.child("your_refrral_key").value.toString()

                                name_field.setText(name)
                                email_field.setText(email)
                                phone_field.setText(phone)
                                set_referral_code.setText(your_refer)

                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@ProfileView, "Check Connection", Toast.LENGTH_LONG).show()
                    }
                })

            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }
    }

    fun Share_us() {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Share & Earn")
            var sAux = "I am using kamaau app to make some extra money." +
                    "Use kamaau app to make some extra money by watching" +
                    " ad videos.Use "+code+ "to join now. "
            sAux = sAux + "http://www.larvaesoft.com \n\n"
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            startActivity(Intent.createChooser(i, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }
}
