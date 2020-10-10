package com.md.williamriesen.hawkeyeharvestfoodbank

import java.util.*

class Order() {
    var accountID: String? = null
    var date: Date? = null
    var itemList =  mutableListOf<Item>()
    var orderState: String?  = null

    constructor(accountIDArg: String, dateArg: Date, itemListArg: MutableList<Item>) : this() {
        accountID = accountIDArg
        date = dateArg
        itemList = itemListArg
        orderState = "SUBMITTED"
    }
}