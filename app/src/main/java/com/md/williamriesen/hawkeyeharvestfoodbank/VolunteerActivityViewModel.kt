package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class VolunteerActivityViewModel : ViewModel() {

    var nextOrder: MutableLiveData<Order>? = null
    var itemsToPackList = MutableLiveData<MutableList<Item>>()

    fun getNextOrderFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("orders").document("nextOrder")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val nextOrder = documentSnapshot.toObject<Order>()
                val myList = nextOrder?.itemList
                itemsToPackList.value = myList
                Log.d("TAG", "nextOrder: ${nextOrder.toString()}.")
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve orders from database failed.")
            }
    }
}
