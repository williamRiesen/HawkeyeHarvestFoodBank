package com.md.williamriesen.hawkeyeharvestfoodbank

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.core.View
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firestore.v1.StructuredQuery
import java.util.*

class VolunteerActivityViewModel : ViewModel() {

    var itemsToPackList = MutableLiveData<MutableList<Item>>()
    var todaysOrdersList = MutableLiveData<MutableList<Order>>()
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
                        Log.d("TAG", "$ordersToPackCount documents retrieved.")
                        if (querySnapshot.size()>0) {
                            orderID = querySnapshot.documents[0].id
                            nextOrder = querySnapshot.documents[0].toObject<Order>()
                            val myList = nextOrder?.itemList
                            accountID = nextOrder?.accountID
                            itemsToPackList.value = myList
                        }
                    }
                    .addOnFailureListener {
                        Log.d("TAG", "Retrieve orders from database failed with error: $it.")
                    }
            }
    }

    fun getTodaysOrdersList() {
        Log.d("TAG", "getTodaysOrdersList called.")
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val query = ordersRef
//            .whereEqualTo("orderDate", today)
            .whereEqualTo("orderState", "PACKED")
            .get()
            .addOnSuccessListener {querySnapshot ->
                if(!querySnapshot.isEmpty) {
                    todaysOrdersList.value = querySnapshot.toObjects<Order>(Order().javaClass)
                    querySnapshot.forEachIndexed{index, queryDocumentSnapshot ->
                        (todaysOrdersList.value as MutableList<Order>)[index].orderID = queryDocumentSnapshot.id
                    }
                    Log.d("TAG", "retrieved orders for today: count= ${querySnapshot.size()}")
                }else {
                    Log.d("TAG", "querySnapshot is empty.")
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Get today's orders failed with exception $it")
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
        val allItemsChecked = itemsOrderedList.all { item ->
            (item.packed)
        }
        return allItemsChecked
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
