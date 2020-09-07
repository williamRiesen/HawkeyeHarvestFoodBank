package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class VolunteerActivityViewModel : ViewModel() {

    var nextOrder: Order? = null

    fun getNextOrderFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("orders").document("nextOrder")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                nextOrder = documentSnapshot.toObject<Order>()
                Log.d("TAG", "nextorder: ${nextOrder.toString()}.")
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve orders from database failed.")
            }
    }

}
