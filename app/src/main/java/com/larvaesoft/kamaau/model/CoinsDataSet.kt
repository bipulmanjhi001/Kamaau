package com.larvaesoft.kamaau.model

class CoinsDataSet {
    var date: String? = null
    var rewards: String? = null
    var time: String? = null
    var uuid: String? = null

    constructor() {

    }

    constructor(date: String, rewards: String, time: String, uuid: String) {
        this.date = date
        this.rewards = rewards
        this.time = time
        this.uuid = uuid
    }
}