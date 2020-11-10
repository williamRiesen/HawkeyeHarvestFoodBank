package com.md.williamriesen.hawkeyeharvestfoodbank

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class VolunteerActivityViewModel : ViewModel() {

    var itemsToPackList = MutableLiveData<MutableList<Item>>()
    var todaysSubmittedOrdersList = MutableLiveData<MutableList<Order>>()
    var todaysPackedOrdersList = MutableLiveData<MutableList<Order>>()
    var nextOrder: Order? = null
    var orderID: String? = null
    var ordersToPackCount = MutableLiveData<Int>()
    var accountID: String? = null
    var accountNumberForDisplay: String? = ""
        get() = accountID?.takeLast(4)


    fun getNextOrderFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val queryCount = ordersRef.whereEqualTo("orderState", "SUBMITTED")
            .get()
            .addOnSuccessListener {
                ordersToPackCount.value = it.size()
                if (ordersToPackCount.value!! > 0) {
                }
                val query = ordersRef.whereEqualTo("orderState", "SUBMITTED")
                    .orderBy("date").limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.size() > 0) {
                            orderID = querySnapshot.documents[0].id
                            nextOrder = querySnapshot.documents[0].toObject<Order>()
                            val myList = nextOrder?.itemList
                            accountID = nextOrder?.accountID
                            itemsToPackList.value = myList
                        }
                    }
            }
    }

    fun getTodaysPackedOrdersList() {
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val startOfTodayTimestamp = Timestamp(today)
        val db = FirebaseFirestore.getInstance()
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
        val ordersRef = db.collection("orders")
        val query = ordersRef
//            .whereGreaterThan("date", startOfTodayTimestamp)
            .whereEqualTo("orderState", "SUBMITTED")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    todaysSubmittedOrdersList.value = querySnapshot.toObjects<Order>(Order().javaClass)
                    querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                        (todaysSubmittedOrdersList.value as MutableList<Order>)[index].orderID =
                            queryDocumentSnapshot.id
                    }
                }
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
            val db = FirebaseFirestore.getInstance()
            db.collection("orders").document(orderID!!).set(updatedOrder!!)
                .addOnSuccessListener {
                    activity.onBackPressed()
                }
        }

        fun recordNoShow(orderID: String, context: Context) {
            val db = FirebaseFirestore.getInstance()
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
