package com.md.williamriesen.hawkeyeharvestfoodbank

import java.util.*


class FoodBank {

    fun makeDate(month: Int, day: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private val holidaysList = listOf<Date>(
        makeDate(Calendar.OCTOBER, 23, 2020),
        makeDate(Calendar.NOVEMBER, 26, 2020),
        makeDate(Calendar.DECEMBER, 25, 2020),
        makeDate(Calendar.JANUARY, 1, 2021)
    )

    private fun getCurrentDateWithoutTime(): Date {
        val calendar = Calendar.getInstance()
        val thisMonth = calendar.get(Calendar.MONTH)
        val thisDay = calendar.get(Calendar.DAY_OF_MONTH)
        val thisYear = calendar.get(Calendar.YEAR)
        return makeDate(thisMonth, thisDay, thisYear)
    }

    val isOpen: Boolean
        get() {
            val today = getCurrentDateWithoutTime()
            return !holidaysList.contains(today)
        }


}