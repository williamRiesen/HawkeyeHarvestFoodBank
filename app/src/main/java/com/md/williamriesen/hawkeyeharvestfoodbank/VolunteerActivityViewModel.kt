package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.*

class VolunteerActivityViewModel : ViewModel() {

    var itemsToPackList = MutableLiveData<MutableList<Item>>()
    var nextOrder: Order? = null
    var orderID: String? = null
    var ordersToPackCount = MutableLiveData<Int>()


    fun getNextOrderFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val query = ordersRef.whereEqualTo("orderState", "SUBMITTED")
            .orderBy("date").limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                ordersToPackCount.value = querySnapshot.size()
                Log.d("TAG", "$ordersToPackCount documents retrieved.")
                if (ordersToPackCount.value!! > 0) {
                    orderID = querySnapshot.documents[0].id
                    nextOrder = querySnapshot.documents[0].toObject<Order>()
                    val myList = nextOrder?.itemList
                    itemsToPackList.value = myList
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve orders from database failed with error: $it.")
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

    fun upDateOrderAsPacked() {
        val updatedOrder = nextOrder
        updatedOrder?.orderState = "PACKED"
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document(orderID!!).set(updatedOrder!!)
    }

}
