package com.md.williamriesen.hawkeyeharvest.foodbank

import java.util.*

class Account(
    val accountID: String,
    val familySize: Int,
    val city: String,
    val county: String,
    val lastOrderDate: Date = julyFirst2020(),
    val lastOrderType: String = "ON_SITE",
    val orderType: String = "PACKED",
    val pickUpHour24: Int = 0
)
    fun julyFirst2020(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 6, 1)
        return calendar.time
    }
