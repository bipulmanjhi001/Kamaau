package com.larvaesoft.kamaau.model

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by Bipul on 15-03-2018.
 */

@IgnoreExtraProperties
class RegisterPOJO {
    lateinit var name: String
    lateinit var email: String
    lateinit var phone: String
    lateinit var state: String
    lateinit var dob: String
    lateinit var gender: String
    lateinit var city: String
    lateinit var pincode: String
    lateinit var refrral_key:String
    lateinit var country:String
    lateinit var your_refrral_key:String

    // Default constructor required for calls to
    // DataSnapshot.getValue(RegisterPoJO.class)
    constructor() {

    }
    constructor(name: String, email: String, phone: String, state: String, dob: String, gender: String, city: String, pincode: String, country: String, refrral_key: String, your_refrral_key: String) {
        this.name = name
        this.email = email
        this.phone = phone
        this.state = state
        this.dob = dob
        this.gender = gender
        this.city = city
        this.pincode = pincode
        this.country = country
        this.refrral_key = refrral_key
        this.your_refrral_key = your_refrral_key
    }

}
