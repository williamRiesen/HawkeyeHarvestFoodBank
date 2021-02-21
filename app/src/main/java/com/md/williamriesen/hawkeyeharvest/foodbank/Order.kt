package com.md.williamriesen.hawkeyeharvest.foodbank

import androidx.lifecycle.MutableLiveData
import java.util.*

class Order() {
    var accountID: String? = null
    var date: Date? = null
    var itemList = mutableListOf<FoodItem>()
    var orderState: String? = null
    var deviceToken: String? = null
    var orderID: String? = null
    var pickUpHour24: Int? = null
    var pickUpMonth: Int? = null

    constructor(
        accountIDArg: String,
        dateArg: Date,
        foodItemListArg: MutableList<FoodItem>,
        orderStateArg: String,
        pickUpHour24Arg: Int? = null,
        pickUpMonthArg: Int? = null
    ) : this() {
        accountID = accountIDArg
        date = dateArg
        itemList = foodItemListArg
        orderState = orderStateArg
        pickUpHour24 = pickUpHour24Arg
        pickUpMonth = pickUpMonthArg
    }

    fun filterOutZeros(): Order {
        val itemList = itemList
        val filteredList = itemList.filter { item ->
            item.qtyOrdered != 0
        }
        val filteredOrder = Order()
        filteredOrder.itemList = filteredList as MutableList<FoodItem>
        filteredOrder.accountID = accountID
        filteredOrder.date = date
        filteredOrder.orderState = orderState
        filteredOrder.pickUpHour24 = pickUpHour24
        filteredOrder.pickUpMonth = pickUpMonth
        return filteredOrder
    }
}