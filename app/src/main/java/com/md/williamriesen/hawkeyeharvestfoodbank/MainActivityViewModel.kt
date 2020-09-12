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
    val foodCountMap2 = MutableLiveData<MutableMap<String, Int>>()
    var objectCatalog: MutableMap<String, Any>? = null
    var familySize: Long? = null
    lateinit var accountID: String

    var points: Int? = null


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

    fun sendObjectCatalogToFireStore() {
        val myObjectCatalog = ObjectCatalog()
        myObjectCatalog.catalogName = "myCatalog"
        myObjectCatalog.itemList = listOf(
            Item(1, "Pork Chops 1lb", "Meat", 1, 100, 100, true),
            Item(2, "Pork & Bacon Sausage 1.5lb", "Meat", 1, 100, 100, true),
            Item(3, "Ground Beef 1lb", "Meat", 1, 2, 100, true),
            Item(4, "Sliced Cooked Ham 2lb", "Meat", 1, 1, 100, true),
            Item(5, "Sliced Cotto Salami 2lb", "Meat", 1, 1, 100, true),
            Item(6, "Whole Chicken 3lb", "Meat", 1, 1, 100, true),
            Item(7, "Chicken Legs 5lb", "Meat", 1, 100, 100, true),
            Item(8, "Whole Ham", "Meat", 2, 1, 100, true),
            Item(9, "Catfish Fillets 2lb", "Meat", 2, 100, 100, true),
            Item(10, "Pork Loin 4lb", "Meat", 2, 100, 100, true),
            Item(11, "Chicken Thighs 5lb", "Meat", 2, 100, 100, true),
            Item(12, "Pork Shoulder Roast 6lb", "Meat", 2, 100, 100, true),
            Item(13, "Cooked Chicken Fajita 5lb", "Meat", 4, 100, 100, true),
            Item(14, "Cooked Chicken Fillets 5lb", "Meat", 4, 100, 100, true),

            Item(15, "Spaghetti / Meatballs", "Protein", 1, 100, 100, true),
            Item(16, "Tuna", "Protein", 1, 100, 100, true),
            Item(17, "Beef Ravioli", "Protein", 1, 100, 100, true),
            Item(18, "Chicken", "Protein", 1, 100, 100, true),
            Item(19, "Peanut Butter", "Protein", 1, 100, 100, true),
            Item(20, "Beef Stew", "Protein", 1, 100, 100, true),

            Item(21, "Carrots", "Vegetables", 1, 100, 100, true),
            Item(22, "Potatoes", "Vegetables", 1, 100, 100, true),
            Item(23, "Corn", "Vegetables", 1, 100, 100, true),
            Item(24, "Green Beans", "Vegetables", 1, 100, 100, true)
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(myObjectCatalog)
    }

    fun retrieveObjectCatalogFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                myObjectCatalog?.itemList?.forEach {
                    foodCountMap.value?.set(it.name!!, 0)
                }
                Log.d("TAG", "myFoodCountMap ${foodCountMap.value}")
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
            }
    }

    fun generateChoices() {
        if (objectCatalog != null) {
            val nonNullObjectCatalog: MutableMap<String, Any> = objectCatalog!!
            for (element in nonNullObjectCatalog) {
                Log.d("TAG", "element: $element")
            }
        }
    }

    fun addItem(itemName: String) {
        val myMap = foodCountMap.value
        myMap!![itemName] = myMap[itemName]!! + 1
    }

    fun removeItem(itemName: String) {
    }

    fun isOption(itemName: String): Boolean {
        val myMap = foodCountMap.value
        return (points!! > myMap!![itemName]!!)
    }

    fun signIn(enteredAccountID: String, context: Context, view: View) {
        accountID = enteredAccountID
        familySize = null
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("accounts").document(enteredAccountID)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                familySize = documentSnapshot["familySize"] as Long
                if (familySize != null) {
                    points = (familySize!! * 2).toInt()
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
    }

    fun submitOrder(view: View) {
        val thisOrder = Order(foodCountMap.value!!)
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document("nextOrder").set(thisOrder)
            .addOnSuccessListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_checkoutFragment_to_doneFragment)
            }
    }
}