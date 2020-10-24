package com.md.williamriesen.hawkeyeharvestfoodbank

import java.util.*


class FoodBank {

    fun makeDate(month: Int, day: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.YEAR, year)
        return calendar.time
    }

    val holidays = listOf(
        makeDate(11,26,2020),
        makeDate(12,25,2020),
        makeDate(1,1,2021)
    )

    fun getCurrentDateWithoutTime(): Date {
        val calendar = Calendar.getInstance()
        val thisMonth = calendar.get(Calendar.MONTH)
        val thisDay = calendar.get(Calendar.DAY_OF_MONTH)
        val thisYear = calendar.get(Calendar.YEAR)
        return makeDate(thisMonth, thisDay, thisYear)
    }

    var isOpen = false


}