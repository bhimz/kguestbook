package id.co.ruma.guestbook.data

import io.realm.RealmObject

/**
 * Created by ariabima on 5/4/17.
 */

open class Guest() : RealmObject() {
    var name: String? = null
    var division: String? = null

    constructor(name: String, division: String) : this() {
        this.name = name
        this.division = division
    }
}