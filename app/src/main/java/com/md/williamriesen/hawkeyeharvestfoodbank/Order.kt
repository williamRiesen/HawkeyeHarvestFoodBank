package com.md.williamriesen.hawkeyeharvestfoodbank

import java.util.*

class Order() {
    var accountID: String? = null
    var date: Date? = null
    var itemList =  mutableListOf<Item>()
    var orderState: String?  = null
    var deviceToken: String? = null
    var orderID: String? = null
    var pickUpHour24: Int? = null
    var pickUpMonth: Int? = null

    constructor(accountIDArg: String, dateArg: Date, itemListArg: MutableList<Item>, orderStateArg: String, pickUpHour24Arg: Int? = null, pickUpMonthArg: Int? = null) : this() {
        accountID = accountIDArg
        date = dateArg
        itemList = itemListArg
        orderState = orderStateArg
        pickUpHour24 = pickUpHour24Arg
        pickUpMonth = pickUpMonthArg
    }
}