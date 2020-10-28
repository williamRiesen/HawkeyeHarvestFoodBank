package com.md.williamriesen.hawkeyeharvestfoodbank

import java.util.*

class Order() {
    var accountID: String? = null
    var date: Date? = null
    var itemList =  mutableListOf<Item>()
    var orderState: String?  = null
    var deviceToken: String? = null
    var orderID: String? = null

    constructor(accountIDArg: String, dateArg: Date, itemListArg: MutableList<Item>, orderStateArg: String) : this() {
        accountID = accountIDArg
        date = dateArg
        itemList = itemListArg
        orderState = orderStateArg
    }
}