package com.md.williamriesen.hawkeyeharvest.foodbank

import java.util.*

class Account() {
    var accountID: String = ""
    var familySize = 0
    var city: String = ""
    var county: String = ""
    var accountNumber: Int? = null
    var lastOrderDate: Date = julyFirst2020()
    var lastOrderType: String = "ON_SITE"
    var orderState: String = "PACKED"
    var pickUpHour24: Int? = null

    constructor(
        accountIdArg: String,
        familySizeArg: Int,
        cityArg: String,
        countyArg: String,
        accountNumberArg: Int?,
        lastOrderDateArg: Date = julyFirst2020(),
        lastOrderTypeArg: String = "ON_SITE",
        orderStateArg: String = "PACKED",
        pickUpHour24Arg: Int = 0
    ) : this(){
        accountID = accountIdArg
        familySize = familySizeArg
        city = cityArg
        county = countyArg
        accountNumber = accountNumberArg
        lastOrderDate = lastOrderDateArg
        lastOrderType = lastOrderTypeArg
        orderState = orderStateArg
        pickUpHour24 = pickUpHour24Arg
    }
}
    fun julyFirst2020(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 6, 1)
        return calendar.time
    }
