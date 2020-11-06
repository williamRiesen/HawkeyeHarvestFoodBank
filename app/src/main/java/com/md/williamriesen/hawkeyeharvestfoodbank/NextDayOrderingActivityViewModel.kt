package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class NextDayOrderingActivityViewModel : ViewModel() {

    val nextDayOpen = FoodBank().nextDayOpen

    val nextDayTakingOrders = FoodBank().nextDayTakingOrders

    val returnOnMessage = "Please return to this app on $nextDayTakingOrders, to place your order for pick up on $nextDayOpen"

}