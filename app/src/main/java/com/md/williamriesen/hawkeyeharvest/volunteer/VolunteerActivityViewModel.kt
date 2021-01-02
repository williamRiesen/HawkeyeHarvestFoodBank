package com.md.williamriesen.hawkeyeharvest.volunteer

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import com.md.williamriesen.hawkeyeharvest.foodbank.Order
import com.md.williamriesen.hawkeyeharvest.signin.OrderService

class VolunteerActivityViewModel : ViewModel() {
    var orderService: OrderService? = null

    var itemsToPackList = MutableLiveData<MutableList<FoodItem>>()
    var todaysSubmittedOrdersList = MutableLiveData<MutableList<Order>>()
    var todaysPackedOrdersList = MutableLiveData<MutableList<Order>>()
    var nextOrder: Order? = null
    var orderID: String? = null
    var ordersToPackCount = MutableLiveData<Int>()
    var accountID: String? = null
    var accountNumberForDisplay: String? = ""
        get() = accountID?.takeLast(4)


    fun getNextOrderFromFireStore() {
        orderService!!.getNextOrder()
            .addOnSuccessListener {
                if (it != null) {
                    nextOrder = it

                    orderID = it.orderID
                    accountID = it.accountID

                    itemsToPackList.value = it.itemList
                }
            }
    }

    fun updateOrderAsBeingPacked(activity: Activity) {
        val updatedOrder = nextOrder
        updatedOrder?.orderState = "BEING_PACKED"

        orderService!!.saveOrder(updatedOrder!!)
            .addOnSuccessListener {
                Log.d("TAG", "Order has been updated as BEING_PACKED.")
            }
    }

    fun getTodaysPackedOrdersList() {
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val startOfTodayTimestamp = Timestamp(today)
        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)

        val ordersRef = db.collection("orders")
        val query = ordersRef
            .whereGreaterThan("date", startOfTodayTimestamp)
            .whereEqualTo("orderState", "PACKED")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    todaysPackedOrdersList.value = querySnapshot.toObjects<Order>(Order().javaClass)
                    querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                        (todaysPackedOrdersList.value as MutableList<Order>)[index].orderID =
                            queryDocumentSnapshot.id
                    }
                }
            }
    }


    fun getTodaysSubmittedOrdersList() {
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val startOfTodayTimestamp = Timestamp(today)

        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)

        val ordersRef = db.collection("orders")
        val query = ordersRef
//            .whereGreaterThan("date", startOfTodayTimestamp)  //For now, look at all submitted orders.
            .whereEqualTo("orderState", "SUBMITTED")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    todaysSubmittedOrdersList.value =
                        querySnapshot.toObjects<Order>(Order().javaClass)
                    querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                        (todaysSubmittedOrdersList.value as MutableList<Order>)[index].orderID =
                            queryDocumentSnapshot.id
                    }
                }
                todaysSubmittedOrdersList.value?.sortBy {
                    it.pickUpHour24
                }
            }
    }

    fun setUpSubmittedOrdersListener() {
        orderService!!.listenForOrders { size: Int, orderList: List<Order> ->
            ordersToPackCount.value = size
            todaysSubmittedOrdersList.value = orderList.sortedBy { it.pickUpHour24 }.toMutableList()
        }
    }

    fun togglePackedState(itemName: String) {
        val myList = itemsToPackList.value
        val thisItem = myList?.find { item ->
            item.name == itemName
        }
        if (thisItem != null) {
            thisItem.packed = !thisItem.packed
        }
    }

    fun checkIfAllItemsPacked(): Boolean {
        itemsToPackList.value
        val itemsOrderedList = itemsToPackList.value!!.filter { item ->
            item.qtyOrdered != 0
        }
        return itemsOrderedList.all { item ->
            (item.packed)
        }
    }

    fun upDateOrderAsPacked(activity: Activity) {
        val updatedOrder = nextOrder
        updatedOrder?.orderState = "PACKED"

        orderService!!.saveOrder(updatedOrder!!)
            .addOnSuccessListener {
                activity.onBackPressed()
            }
    }

    fun recordNoShow(orderID: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)

        db.collection("orders").document(orderID).update("orderState", "NO SHOW")
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "No Show has been recorded.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }


}
