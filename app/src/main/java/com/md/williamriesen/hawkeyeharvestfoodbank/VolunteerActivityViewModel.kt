package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class VolunteerActivityViewModel : ViewModel(){

    var orders : String? = null

    fun populateOrdersFromFireStore(){
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("orderSets").document("currentOrderSet")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                orders = documentSnapshot.get("orderList").toString()
                Log.d("TAG", "orders: $orders")
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve orders from database failed.")
            }

    }
}