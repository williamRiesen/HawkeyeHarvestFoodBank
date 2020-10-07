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


    fun getNextOrderFromFireStore() {
        val db = FirebaseFirestore.getInstance()

        val ordersRef = db.collection("orders")
        val query = ordersRef
//            .whereEqualTo("packed", false)
            .orderBy("date").limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val nextOrder = querySnapshot.documents[0].toObject<Order>()
                val myList = nextOrder?.itemList
                itemsToPackList.value = myList
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve orders from database failed.")
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

}
