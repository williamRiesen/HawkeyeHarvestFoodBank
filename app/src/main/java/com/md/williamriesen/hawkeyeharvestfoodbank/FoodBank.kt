package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
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

    fun createTimePoint(date: Date, hour24: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        return calendar.time
    }

    private val holidaysList = listOf<Date>(
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

    private fun isWeekend(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val isSaturday = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
        val isSunday = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        return isSaturday || isSunday
    }

    val isOpen: Boolean
        get() {
            val today = getCurrentDateWithoutTime()
            return !isWeekend(today) && !holidaysList.contains(today)
        }

    val isOpeningLaterToday: Boolean
        get() {
            return if (!isOpen) false
            else {
                val today = getCurrentDateWithoutTime()
                val openingTime = createTimePoint(today, 12, 0)
                val calendar = Calendar.getInstance()
                val now = Date(calendar.timeInMillis)
                Log.d("TAG", "today: $today, openingTime: $openingTime, now: $now")
                val result = (openingTime > now)
                Log.d("TAG", "result: $result")
                result
            }
        }
}