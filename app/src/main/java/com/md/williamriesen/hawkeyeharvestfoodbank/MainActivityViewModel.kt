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
    val itemList = MutableLiveData<MutableList<Item>>()
    val categoriesList = MutableLiveData<MutableList<Category>>()
    var objectCatalog: MutableMap<String, Any>? = null
    private var familySizeFromFireStore: Long? = null
    var familySize = 0
    lateinit var accountID: String

    var points: Int? = null
    var categories: MutableLiveData<MutableList<Category>> =
        MutableLiveData(mutableListOf<Category>())


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

    fun sendCategoriesListToFireStore() {
        val categoriesListing = CategoriesListing()
        categoriesListing.categoriesListingName = "myCategories"
        categoriesListing.categories = listOf(
            Category("Meat", 2, 0),
            Category("Protein", 2, 1),
            Category("Vegetables", 3, 0)
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("categories").document("categories").set(categoriesListing)
    }

    fun retrieveCategoriesFromFireStore(view: View) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("categories").document("categories")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesList.value = categoriesListing?.categories as MutableList<Category>
                Log.d("TAG", "Retrieve categories from database succeeded.")
                generateHeadings()
                itemList.value?.sortWith(
                    compareBy<Item> { it.category }.thenBy { it.itemID })
                Navigation.findNavController(view)
                    .navigate(R.id.action_signInFragment_to_selectionFragment)
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve categories from database failed.")
            }
    }

    fun retrieveObjectCatalogFromFireStore(view: View) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                itemList.value = myObjectCatalog?.itemList as MutableList<Item>?
                retrieveCategoriesFromFireStore(view)
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
            }
    }

    //    fun determineCategories() {
//        val categoryNames = itemList.value?.distinctBy { it.category }
//        categoryNames?.forEach { item ->
//            val newCategoryItem = Item(0, item.category!!, item.category!!, 0, 0, 0, true )
//            itemList.value?.add(newCategoryItem)
//            val newCategory = Category(
//                name = newCategoryItem.name!!,
//                familySize = familySize
//            )
//            categories.value!!.add(newCategory)
//            Log.d("TAG", "newCategory ${newCategory.name}")
//        }
//        categories.value?.forEach { category ->
//            Log.d("TAG", "categories:  ${category.name}, ${category.pointsAllocated}, ${category.pointsUsed}")
//        }
//        updatePointValues()
//        itemList.value?.sortWith(
//            compareBy<Item> { it.category }.thenBy { it.itemID })
//    }
    fun generateHeadings() {
        categoriesList.value?.forEach() { category ->
            Log.d("TAG", "familySize $familySize")
            val heading = Item(
                0,
                category.name,
                category.name,
                0,
                0,
                0,
                true,
                category.calculatePoints(familySize)
            )
            Log.d("TAG", "heading.name: ${heading.name}, heading.PointsA  ${heading.categoryPointsAllocated}, " +
                        "heading.pointsUsed ${heading.categoryPointsUsed}")
            itemList.value!!.add(heading)
        }
    }

    fun updatePointValues() {
        itemList.value?.forEach() { item ->
            item.categoryPointsAllocated = lookUpPointsAllocated(item.category!!)
        }
    }

    fun lookUpPointsAllocated(category: String): Int {
        val thisCategory = categories.value?.find { it.name == category }
        return thisCategory!!.calculatePoints(familySize)
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
        val myList = itemList.value
        val selectedItem = myList?.find {
            it.name == itemName
        }
        selectedItem?.qtyOrdered = selectedItem?.qtyOrdered?.plus(1)!!
        val selectedCategoryHeading = myList.find {
            it.name == selectedItem?.category
        }
        Log.d("TAG", "selectedCategoryHeading.name: ${selectedCategoryHeading?.name}")
        selectedCategoryHeading?.categoryPointsUsed!!.plus(1)

//
//        val myMap = foodCountMap.value
//        myMap!![itemName] = myMap[itemName]!! + 1
    }

    fun removeItem(itemName: String) {
    }

    fun isOption(itemName: String): Boolean {
        val myList = itemList.value
        val selectedItem = myList?.find {
            it.name == itemName
        }
        return points!! > selectedItem?.qtyOrdered!!

//        return (points!! > myMap!![itemName]!!)
    }

    fun signIn(enteredAccountID: String, context: Context, view: View) {
        accountID = enteredAccountID
        familySizeFromFireStore = null
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("accounts").document(enteredAccountID)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                familySizeFromFireStore = documentSnapshot["familySize"] as Long
                if (familySizeFromFireStore != null) {
                    points = (familySizeFromFireStore!! * 2).toInt()
                    familySize = familySizeFromFireStore!!.toInt()
//                    determineCategories()

                    retrieveObjectCatalogFromFireStore(view)


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
        val thisOrder = Order(itemList.value!!)
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document("nextOrder").set(thisOrder)
            .addOnSuccessListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_checkoutFragment_to_doneFragment)
            }
    }
}
