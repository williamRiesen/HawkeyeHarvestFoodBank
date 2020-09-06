package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.lifecycle.MutableLiveData

class Order(var accountID:String, var itemMap: MutableMap<String, Int>) {

    var packed = false
    var delivered = false

}