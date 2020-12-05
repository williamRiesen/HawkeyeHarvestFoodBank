package com.md.williamriesen.hawkeyeharvest.foodbank

import android.util.Log
import java.util.*

enum class OrderState {
    NOT_STARTED_YET,
    SAVED,
    SUBMITTED,
    BEING_PACKED,
    PACKED,
    NO_SHOW,
}

enum class OrderType {
    NEXT_DAY,
    ON_SITE,
}

enum class WhenOrdered {
    TODAY,
    YESTERDAY,
    EARLIER_THIS_MONTH,
    PRIOR_TO_THIS_MONTH;

    companion object {
        fun fromDate(lastOrderDate: Date, foodBank: FoodBank): WhenOrdered {

            val startOfToday: Date = foodBank.getCurrentDateWithoutTime()
            val calendar = Calendar.getInstance()
            calendar.time = startOfToday
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val startOfYesterday = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startOfThisMonth = calendar.time
            Log.d(
                "TAG",
                "lastOrderDate: $lastOrderDate, startOfToday: $startOfToday, startOfThisMonth: $startOfThisMonth"
            )
            return when {
                lastOrderDate > startOfToday -> TODAY
                lastOrderDate > startOfYesterday -> YESTERDAY
                lastOrderDate > startOfThisMonth -> EARLIER_THIS_MONTH
                else -> PRIOR_TO_THIS_MONTH
            }
        }
    }
}