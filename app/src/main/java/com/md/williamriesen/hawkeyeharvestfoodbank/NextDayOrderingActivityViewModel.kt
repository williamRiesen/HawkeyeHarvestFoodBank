package com.md.williamriesen.hawkeyeharvestfoodbank

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import java.text.DateFormat
import java.text.SimpleDateFormat

class NextDayOrderingActivityViewModel : ViewModel() {

    var pickUpHour24 = 0
    val foodBank = FoodBank()
    val simpleDateFormat = SimpleDateFormat("E, MMM d")

    val nextDayOpen : String?
        get() = simpleDateFormat.format(foodBank.nextDayOpen())

    val nextDayTakingOrders: String
    get() = simpleDateFormat.format(foodBank.nextDayTakingOrders())

    val nextPickUpDay: String
    get() = simpleDateFormat.format(foodBank.nextDayOpen(afterTomorrow = true))

    val nextPreOrderDay : String
    get() = simpleDateFormat.format(foodBank.nextDayTakingOrders(afterToday = true))

    val returnOnMessage: String
    get() = "Please return to this app on $nextDayTakingOrders before 5 PM, to place your order for pick up on $nextDayOpen"

    val takingOrders : Boolean
    get() = FoodBank().isTakingNextDayOrders

    fun goToNextFragment(pickUpHour24Arg: Int, view: View){
        pickUpHour24 = pickUpHour24Arg
        if (pickUpHour24Arg == 0){
            Navigation.findNavController(view).navigate(R.id.action_selectPickUpTimeFragment_to_returnAnotherDayFragment)

        } else {
            Navigation.findNavController(view).navigate(R.id.action_selectPickUpTimeFragment_to_selectionFragment2)
        }
    }

}