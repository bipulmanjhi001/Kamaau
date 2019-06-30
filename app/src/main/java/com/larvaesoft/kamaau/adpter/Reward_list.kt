package com.larvaesoft.kamaau.adpter

import java.io.Serializable

/**
 * Created by tkru on 11/27/2017.
 */

class Reward_list : Serializable {
    var date: String? = null
    var time: String? = null
    var amount: String? = null
    var total: String? = null
    var parantid: String? = null

    constructor() {

    }

    constructor(date: String, time: String, amount: String) {
        this.date = date
        this.time = time
        this.amount = amount
    }
}
