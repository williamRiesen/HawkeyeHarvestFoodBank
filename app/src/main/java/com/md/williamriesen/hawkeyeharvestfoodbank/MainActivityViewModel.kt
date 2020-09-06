package com.md.williamriesen.hawkeyeharvestfoodbank

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MainActivityViewModel() : ViewModel() {
    private lateinit var retrievedCatalog: Catalog
    val orderBlank: MutableLiveData<OrderBlank?>? = null
    val foodCountMap = MutableLiveData<MutableMap<String, Int>>()
    var familySize: Any? = 0
    lateinit var accountID: String

    val order = foodCountMap

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
    }

    fun signIn(enteredAccountID: String, context: Context, view: View): String {
        accountID = enteredAccountID
        familySize = null
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("accounts").document(enteredAccountID)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                familySize = documentSnapshot["familySize"]
                if (familySize != null) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_signInFragment_to_selectionFragment)
                } else {
                    Toast.makeText(
                        context,
                        "Sorry, Not a valid account.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve family size from database failed.")
            }
        return familySize.toString()
    }

    fun submitOrder(view: View) {
        val thisOrder = Order(accountID, foodCountMap.value!!)
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document().set(thisOrder)
            .addOnSuccessListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_checkoutFragment_to_doneFragment)
            }
    }
}