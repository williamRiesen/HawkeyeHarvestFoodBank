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
    var objectCatalog: MutableMap<String, Any>? = null
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

    fun sendObjectCatalogToFireStore() {
        val myObjectCatalog = mutableMapOf<String, Item>()
        myObjectCatalog["1"] = Item("Pork Chops 1lb", "Meat", 1, 100, 100)
        myObjectCatalog["2"] = Item("Pork & Bacon Sausage 1.5lb", "Meat", 1, 100, 100)
        myObjectCatalog["3"] = Item("Ground Beef 1lb", "Meat", 1, 2, 100)
        myObjectCatalog["4"] = Item("Sliced Cooked Ham 2lb", "Meat", 1, 1, 100)
        myObjectCatalog["5"] = Item("Sliced Cotto Salami 2lb", "Meat", 1, 1, 100)
        myObjectCatalog["6"] = Item("Whole Chicken 3lb", "Meat", 1, 1, 100)
        myObjectCatalog["7"] = Item("Chicken Legs 5lb", "Meat", 1, 100, 100)
        myObjectCatalog["8"] = Item("Whole Ham", "Meat", 2, 1, 100)
        myObjectCatalog["9"] = Item("Catfish Fillets 2lb", "Meat", 2, 100, 100)
        myObjectCatalog["10"] = Item("Pork Loin 4lb", "Meat", 2, 100, 100)
        myObjectCatalog["11"] = Item("Chicken Thighs 5lb", "Meat", 2, 100, 100)
        myObjectCatalog["12"] = Item("Pork Shoulder Roast 6lb", "Meat", 2, 100, 100)
        myObjectCatalog["13"] = Item("Cooked Chicken Fajita 5lb", "Meat", 4, 100, 100)
        myObjectCatalog["14"] = Item("Cooked Chicken Fillets 5lb", "Meat", 4, 100, 100)

        myObjectCatalog["15"] = Item("Spaghetti / Meatballs", "Protein", 1, 100, 100)
        myObjectCatalog["16"] = Item("Tuna", "Protein", 1, 100, 100)
        myObjectCatalog["17"] = Item("Beef Ravioli", "Protein", 1, 100, 100)
        myObjectCatalog["18"] = Item("Chicken", "Protein", 1, 100, 100)
        myObjectCatalog["19"] = Item("Peanut Butter", "Protein", 1, 100, 100)
        myObjectCatalog["20"] = Item("Beef Stew", "Protein", 1, 100, 100)

        myObjectCatalog["21"] = Item("Carrots", "Vegetables", 1, 100, 100)
        myObjectCatalog["22"] = Item("Potatoes", "Vegetables", 1, 100, 100)
        myObjectCatalog["23"] = Item("Corn", "Vegetables", 1, 100, 100)
        myObjectCatalog["24"] = Item("Green Beans", "Vegetables", 1, 100, 100)


        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(myObjectCatalog)
    }

    fun retrieveObjectCatalogFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                Log.d("TAG", "documentSnapshot: $documentSnapshot")
                objectCatalog = documentSnapshot.data?.toMutableMap()
                Log.d("TAG", "objectCatalog: $objectCatalog")
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
            val thisOrder = Order(foodCountMap.value!!, accountID)
            val db = FirebaseFirestore.getInstance()
            db.collection("orders").document("nextOrder").set(thisOrder)
                .addOnSuccessListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_checkoutFragment_to_doneFragment)
                }
        }
    }