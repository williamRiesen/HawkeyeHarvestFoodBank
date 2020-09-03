package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class MainActivityViewModel() : ViewModel() {
    private lateinit var retrievedCatalog: Catalog
    val orderBlank: MutableLiveData<OrderBlank?>? = null
    val foodCountMap = MutableLiveData<MutableMap<String, Int>>()
    val order = Transformations.map(foodCountMap) { something -> something.filterValues { it !=0 } }

    fun populateFoodCountMapFromCode() {
        if (foodCountMap.value == null) {
            val myMap = mutableMapOf<String, Int>(
                "Ground Beef 1lb" to 0,
                "Sliced Cooked Ham 2lb" to 0,
                "Sliced Cotto Salami 2lb" to 0,
                "Whole Chicken 3lb" to 0,
                "Chicken Legs 5lb" to 0,
                "Whole Ham" to 0,
                "Catfish Fillets 2lb" to 0,
                "Pork Loin 4lb" to 0,
                "Chicken Thighs 5lb" to 0,
                "Pork Shoulder Roast 6lb" to 0,
                "Cooked Chicken Fajita 5lb" to 0,
                "Cooked Chicken Fillets 5lb" to 0
            )
            foodCountMap.value = myMap
        }
    }

    fun populateFoodCountMapFromFireStore() {
        if (foodCountMap.value == null) {
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("catalogs").document("catalog")
            docRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val catalog = documentSnapshot.toObject<Catalog>()
                    Log.d("TAG", "catalog successfully retrieved.")
                    val myMap = catalog?.itemList
                    foodCountMap.value = myMap
                }
                .addOnFailureListener {
                    Log.d("TAG", "Retrieve from database failed.")
                }
        }


    }

    fun sendCatalogToFireStore() {
        val myMap = mutableMapOf<String, Int>(
            "Ground Beef 1lb" to 0,
            "Sliced Cooked Ham 2lb" to 0,
            "Sliced Cotto Salami 2lb" to 0,
            "Whole Chicken 3lb" to 0,
            "Chicken Legs 5lb" to 0,
            "Whole Ham" to 0,
            "Catfish Fillets 2lb" to 0,
            "Pork Loin 4lb" to 0,
            "Chicken Thighs 5lb" to 0,
            "Pork Shoulder Roast 6lb" to 0,
            "Cooked Chicken Fajita 5lb" to 0,
            "Cooked Chicken Fillets 5lb" to 0
        )
        val catalog = Catalog(myMap)
        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("catalog").set(catalog)

    }


    fun addItem(itemName: String) {
        val myMap = foodCountMap.value
        myMap!![itemName] = myMap[itemName]!! + 1
    }

    fun removeItem(itemName: String) {
//        orderBlank?.remove(itemName)
    }

}