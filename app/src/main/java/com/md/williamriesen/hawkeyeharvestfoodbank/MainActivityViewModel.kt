package com.md.williamriesen.hawkeyeharvestfoodbank

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.*

class MainActivityViewModel() : ViewModel() {

    var accountID = "Turnip"
    val itemList = MutableLiveData<MutableList<Item>>()
    private var savedItemList = mutableListOf<Item>()
    val outOfStockNameList : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf<String>())
    val categoriesList = MutableLiveData<MutableList<Category>>()
    var objectCatalog: MutableMap<String, Any>? = null
    private var familySizeFromFireStore: Long? = null
    var familySize = 0
    var points: Int? = null
    var lastOrderDate: Date? = null
    var lastOrderTimestamp: Timestamp? = null
    var categories: MutableLiveData<MutableList<Category>> =
        MutableLiveData(mutableListOf())


    fun sendObjectCatalogToFireStore() {
        val myObjectCatalog = ObjectCatalog()
        myObjectCatalog.catalogName = "myCatalog"
        myObjectCatalog.itemList = listOf(
            Item(8, "Pork Chops 1lb", "Meat", 1, 100, 100, true, 1),
            Item(9, "Pork & Bacon Sausage 1.5lb", "Meat", 1, 100, 100, true, 1),
            Item(10, "Ground Beef 1lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            Item(11, "Sliced Cooked Ham 2lb (Limit 1)", "Meat", 1, 1, 100, true, 1),
            Item(12, "Sliced Cotto Salami 2lb (Limit 1)", "Meat", 1, 1, 100, true, 1),
            Item(13, "Whole Chicken 3lb (Limit 1)", "Meat", 1, 1, 100, true, 1),
            Item(14, "Chicken Legs 5lb", "Meat", 1, 100, 100, true, 1),
            Item(3, "Whole Ham(2pts, Limit 1)", "Meat", 2, 1, 100, true, 1),
            Item(4, "Catfish Fillets 2lb (2pts)", "Meat", 2, 100, 100, true, 1),
            Item(5, "Pork Loin 4lb (2pts)", "Meat", 2, 100, 100, true, 1),
            Item(6, "Chicken Thighs 5lb (2pts)", "Meat", 2, 100, 100, true, 1),
            Item(7, "Pork Shoulder Roast 6lb (2pts)", "Meat", 2, 100, 100, true, 1),
            Item(2, "Cooked Chicken Fajita 5lb (4pts)", "Meat", 4, 100, 100, true, 1),
            Item(1, "Cooked Chicken Fillets 5lb (4pts)", "Meat", 4, 100, 100, true, 1),

            Item(15, "Spaghetti / Meatballs", "Protein", 1, 100, 100, true, 2),
            Item(16, "Tuna", "Protein", 1, 100, 100, true, 2),
            Item(17, "Beef Ravioli", "Protein", 1, 100, 100, true, 2),
            Item(18, "Chicken", "Protein", 1, 100, 100, true, 2),
            Item(19, "Peanut Butter", "Protein", 1, 100, 100, true, 2),
            Item(20, "Beef Stew", "Protein", 1, 100, 100, true, 2),

            Item(21, "Carrots", "Vegetables", 1, 100, 100, true, 3),
            Item(22, "Potatoes", "Vegetables", 1, 100, 100, true, 3),
            Item(23, "Corn", "Vegetables", 1, 100, 100, true, 3),
            Item(24, "Green Beans", "Vegetables", 1, 100, 100, true, 3),

            Item(25, "Peaches", "Fruits", 1, 100, 100, true, 4),
            Item(26, "Pears", "Fruits", 1, 100, 100, true, 4),
            Item(27, "Cranberry Juice", "Fruits", 1, 100, 100, true, 4),
            Item(28, "Dried Cranberries", "Fruits", 1, 100, 100, true, 4),
            Item(29, "Raisins", "Fruits", 1, 100, 100, true, 4),
            Item(30, "Apricots", "Fruits", 1, 100, 100, true, 4),
            Item(31, "Mandarin Oranges", "Fruits", 1, 100, 100, true, 4),
            Item(32, "Applesauce", "Fruits", 1, 100, 100, true, 4),
            Item(33, "Pineapple", "Fruits", 1, 100, 100, true, 4),
            Item(34, "Juice", "Fruits", 1, 100, 100, true, 4),

            Item(35, "Pinto", "Beans", 1, 100, 100, true, 5),
            Item(36, "Refried", "Beans", 1, 100, 100, true, 5),
            Item(37, "Chili", "Beans", 1, 100, 100, true, 5),
            Item(38, "Pork & Beans", "Beans", 1, 100, 100, true, 5),
            Item(39, "Kidney", "Beans", 1, 100, 100, true, 5),
            Item(40, "Black", "Beans", 1, 100, 100, true, 5),
            Item(41, "Dried Lentils", "Beans", 1, 100, 100, true, 5),
            Item(42, "Dried Red Beans", "Beans", 1, 100, 100, true, 5),
            Item(43, "Dried Black Beans", "Beans", 1, 100, 100, true, 5),
            Item(44, "Dried Pinto Beans", "Beans", 1, 100, 100, true, 5),

            Item(45, "Chicken Broth", "Soups", 1, 100, 100, true, 6),
            Item(46, "Vegetarian", "Soups", 1, 100, 100, true, 6),
            Item(47, "Tomato", "Soups", 1, 100, 100, true, 6),
            Item(48, "Cream of Chicken", "Soups", 1, 100, 100, true, 6),
            Item(49, "Cream of Mushroom", "Soups", 1, 100, 100, true, 6),
            Item(50, "Chicken Noodle", "Soups", 1, 100, 100, true, 6),
            Item(51, "Chicken/Dumpling", "Soups", 1, 100, 100, true, 6),

            Item(52, "Sloppy Joe Mix", "Processed Tomato", 1, 100, 100, true, 7),
            Item(53, "Tomato Sauce", "Processed Tomato", 1, 100, 100, true, 7),
            Item(54, "Diced Tomatoes", "Processed Tomato", 1, 100, 100, true, 7),
            Item(55, "Pasta Sauce", "Processed Tomato", 1, 100, 100, true, 7),

            Item(56, "Oatmeal", "Cereals", 1, 100, 100, true, 8),
            Item(57, "Oatmeal Packets", "Cereals", 1, 100, 100, true, 8),
            Item(58, "Toasted Oats", "Cereals", 1, 100, 100, true, 8),
            Item(59, "Corn Flakes", "Cereals", 1, 100, 100, true, 8),
            Item(60, "Crisp Rice", "Cereals", 1, 100, 100, true, 8),

            Item(61, "Stuffing", "Sides", 1, 100, 100, true, 9),
            Item(62, "Instant Potatoes", "Sides", 1, 100, 100, true, 9),
            Item(63, "White Rice", "Sides", 1, 100, 100, true, 9),
            Item(64, "Brown Rice", "Sides", 1, 100, 100, true, 9),

            Item(65, "Spaghetti", "Pasta", 1, 100, 100, true, 10),
            Item(66, "Rotini/Cellentani", "Pasta", 1, 100, 100, true, 10),
            Item(67, "Elbow Macaroni", "Pasta", 1, 100, 100, true, 10),
//            Item(68, "Spaghetti", "Pasta", 1, 100, 100, true, 10),
            Item(69, "Egg Noodles", "Pasta", 1, 100, 100, true, 10),

            Item(70, "Mac & Cheese", "Meal Helper", 1, 100, 100, true, 11),
            Item(71, "Spaghetti Rings", "Meal Helper", 1, 100, 100, true, 11),
            Item(72, "Lasagna Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(73, "Cheeseburger Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(74, "Beef Pasta Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(75, "Alfredo Chicken Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(76, "Strognaff Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(77, "Tuna Helper", "Meal Helper", 1, 100, 100, true, 11),

            Item(78, "Whole Wheat Flour", "Extra", 1, 100, 100, true, 12),
            Item(79, "Dry Milk", "Extra", 1, 100, 100, true, 12),
            Item(80, "Boxed Milk", "Extra", 1, 100, 100, true, 12),
            Item(81, "Vegetable Oil", "Extra", 1, 100, 100, true, 12),
            Item(82, "Mini Pies (Cherry/Apple)", "Extra", 1, 100, 100, true, 12),
            Item(83, "Cheese Balls", "Extra", 1, 100, 100, true, 12),

            Item(84, "Toothbrush", "Nonedibles", 1, 100, 100, true, 13),
            Item(85, "Toothpaste", "Nonedibles", 1, 100, 100, true, 13),
            Item(86, "Bar of Soap", "Nonedibles", 1, 100, 100, true, 13),
            Item(87, "Toilet Paper", "Nonedibles", 1, 100, 100, true, 13),
            Item(88, "Laundry Soap (Homemade- Limit 1)", "Nonedibles", 1, 1, 100, true, 13)
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(myObjectCatalog)
    }

    fun sendCategoriesListToFireStore() {
        val categoriesListing = CategoriesListing()
        categoriesListing.categoriesListingName = "myCategories"
        categoriesListing.categories = listOf(
            Category(1, "Meat", 2, 0),
            Category(2, "Protein", 1, 2),
            Category(3, "Vegetables", 2, 0),
            Category(4, "Fruits", 2, 0),
            Category(5, "Beans", 2, 0),
            Category(6, "Soups", 2, 0),
            Category(7, "Processed Tomato", 2, 1),
            Category(8, "Cereals", 1, 0),
            Category(9, "Sides", 1, 0),
            Category(10, "Pasta", 1, 0),
            Category(11, "Meal Helper", 1, 0),
            Category(12, "Extra", 1, 0),
            Category(13, "Nonedibles", 1, 2),
            Category(14, "Bottom Bar", 0, 0)
        )
        val db = FirebaseFirestore.getInstance()

        db.collection("categories").document("categories").set(categoriesListing)
            .addOnSuccessListener {
                Log.d("TAG", "Categories write successful.")
            }
            .addOnFailureListener {
                Log.d("TAG", "Categories write failed.")
            }
    }

    private fun retrieveCategoriesFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("categories").document("categories")
        Log.d("TAG", "docRef: $docRef")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesList.value = categoriesListing?.categories as MutableList<Category>
                Log.d("TAG", "Retrieve categories from database succeeded.")
                generateHeadings()
                itemList.value?.sortWith(
                    compareBy<Item> { it.categoryId }.thenBy { it.itemID })
                retrieveSavedOrder()
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve categories from database failed.")
            }
    }

    fun retrieveObjectCatalogFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                Log.d("TAG", "Retrieve objectCatalog from database successful.")
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                val availableItemsList = myObjectCatalog?.itemList?.filter { item ->
                    item.isAvailable as Boolean
                }
                itemList.value = availableItemsList as MutableList<Item>?
                retrieveCategoriesFromFireStore()
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
            }
    }

    fun retrieveSavedOrder() {
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val query = ordersRef
            .whereEqualTo("accountID", accountID)
            .whereEqualTo("orderState", "SAVED")
            .orderBy("date", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.d("TAG", "${querySnapshot.size()} documents retrieved.")
                if (querySnapshot.size() > 0) {
                    val savedOrder = querySnapshot.documents[0].toObject<Order>()
                    savedItemList = savedOrder?.itemList!!
                    checkSavedOrderAgainstCurrentOfferings()
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve orders from database failed with error: $it.")
            }
    }

    fun checkSavedOrderAgainstCurrentOfferings() {
        Log.d("TAG", "checSavedOrderAgainst... called.")
        savedItemList.forEach { savedItem ->
            Log.d("TAG", "savedItem: ${savedItem.name}")
            val itemToCheck = itemList.value?.find { offeredItem ->
                offeredItem.name == savedItem.name
            }
            if (itemToCheck == null) {
                outOfStockNameList.value!!.add(savedItem.name!!)
            } else {
                itemToCheck.qtyOrdered = savedItem.qtyOrdered
                val categoryToUpdate = itemList.value!!.find { item ->
                    item.name == itemToCheck.category
                }
                categoryToUpdate!!.categoryPointsUsed = categoryToUpdate.categoryPointsUsed + itemToCheck.pointValue!! * itemToCheck.qtyOrdered
            }
        }
    }


    private fun generateHeadings() {
        categoriesList.value?.forEach { category ->
            val heading = Item(
                0,
                category.name,
                category.name,
                0,
                0,
                0,
                true,
                category.id,
                category.calculatePoints(familySize)
            )
//            Log.d("TAG", "familySize $familySize")
//            Log.d("TAG", "category.calculatePoints(familySize) ${category.calculatePoints(familySize)}")
//            Log.d("TAG", "categoryPointsAllocated: ${heading.categoryPointsAllocated}")
            itemList.value!!.add(heading)
        }
    }

//    fun lookUpPointsAllocated(category: String): Int {
//        val thisCategory = categories.value?.find { it.name == category }
//        return thisCategory!!.calculatePoints(familySize)
//    }

    fun addItem(itemName: String) {
        val myList = itemList.value
        val selectedItem = myList?.find {
            it.name == itemName
        }
        selectedItem?.qtyOrdered = selectedItem?.qtyOrdered?.plus(1)!!
        val selectedCategoryHeading = myList.find {
            it.name == selectedItem.category
        }
//        Log.d("TAG", "selectedCategoryHeading.name: ${selectedCategoryHeading?.name}")
        selectedCategoryHeading?.categoryPointsUsed!!.plus(1)

    }

    fun removeItem() {
    }

    fun signIn(enteredAccountID: String, context: Context) {
        val myFirebaseMessagingService = MyFirebaseMessagingService()
        val token = myFirebaseMessagingService
        familySizeFromFireStore = null
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("accounts").document(enteredAccountID)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                when (documentSnapshot["role"]) {
                    "client" -> {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("ACCOUNT_ID", enteredAccountID)
                        familySizeFromFireStore = documentSnapshot["familySize"] as Long
                        familySize = familySizeFromFireStore!!.toInt()
                        intent.putExtra("FAMILY_SIZE", familySize)
                        lastOrderTimestamp = documentSnapshot["lastOrderDate"] as Timestamp
                        Log.d("TAG", "timestamp.seconds: ${lastOrderTimestamp!!.seconds}")
                        Log.d("TAG", "lastOrderDate: ${lastOrderDate}")
                        intent.putExtra("LAST_ORDER_DATE_TIMESTAMP", lastOrderTimestamp)
                        context.startActivity(intent)
                    }
                    "volunteer" -> {
                        val intent = Intent(context, VolunteerActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                    "manager" -> {
                        val intent = Intent(context, ManagerActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                    else -> {
                        Toast.makeText(
                            context,
                            "Sorry, Not a valid account.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve family size from database failed.")
            }
    }

    fun saveOrder(view: View) {
        val thisOrder = Order(accountID, Date(), itemList.value!!, "SAVED")
        val filteredOrder = filterOutZeros(thisOrder)
        val db = FirebaseFirestore.getInstance()
        db.collection(("orders")).document().set(filteredOrder)
            .addOnSuccessListener {
                Log.d("TAG", "canOrderNow: $canOrderNow")
                if (canOrderNow) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_checkoutFragment_to_askWhetherToSubmitSavedOrderFragment)
                } else {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_checkoutFragment_to_orderSavedFragment)
                }
            }
    }

    fun submitOrder(view: View) {
        val thisOrder = Order(accountID, Date(), itemList.value!!, "SUBMITTED")
        val filteredOrder = filterOutZeros(thisOrder)
        val db = FirebaseFirestore.getInstance()
        db.collection(("orders")).document().set(filteredOrder)
            .addOnSuccessListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_askWhetherToSubmitSavedOrderFragment_to_orderSubmittedFragment)
            }
    }


    private fun filterOutZeros(order: Order): Order {
        val itemList = order.itemList
        val filteredList = itemList.filter { item ->
            item.qtyOrdered != 0
        }
        val filteredOrder = Order()
        filteredOrder.itemList = filteredList as MutableList<Item>
        filteredOrder.accountID = order.accountID
        filteredOrder.date = order.date
        filteredOrder.orderState = order.orderState
        return filteredOrder
    }

    val suggestedNextOrderDate: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = lastOrderDate
            calendar.add(Calendar.MONTH, 1)
            return calendar.time
        }

    val earliestOrderDate: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = suggestedNextOrderDate
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.add(Calendar.DATE, -dayOfMonth + 1)
            return calendar.time
        }

    val canOrderNow: Boolean
        get() = earliestOrderDate.before(Date(System.currentTimeMillis()))

}
