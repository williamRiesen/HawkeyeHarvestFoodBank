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
            Item(1, "Pork Chops 1lb", "Meat", 1, 100, 100, true,1),
            Item(2, "Pork & Bacon Sausage 1.5lb", "Meat", 1, 100, 100, true,1),
            Item(3, "Ground Beef 1lb (Limit 2)", "Meat", 1, 2, 100, true,1),
            Item(4, "Sliced Cooked Ham 2lb (Limit 1)", "Meat", 1, 1, 100, true,1),
            Item(5, "Sliced Cotto Salami 2lb (Limit 1)", "Meat", 1, 1, 100, true,1),
            Item(6, "Whole Chicken 3lb (Limit 1)", "Meat", 1, 1, 100, true,1),
            Item(7, "Chicken Legs 5lb", "Meat", 1, 100, 100, true,1),
            Item(8, "Whole Ham", "Meat", 2, 1, 100, true,1),
            Item(9, "Catfish Fillets 2lb", "Meat", 2, 100, 100, true,1),
            Item(10, "Pork Loin 4lb", "Meat", 2, 100, 100, true,1),
            Item(11, "Chicken Thighs 5lb", "Meat", 2, 100, 100, true,1),
            Item(12, "Pork Shoulder Roast 6lb", "Meat", 2, 100, 100, true,1),
            Item(13, "Cooked Chicken Fajita 5lb", "Meat", 4, 100, 100, true,1),
            Item(14, "Cooked Chicken Fillets 5lb", "Meat", 4, 100, 100, true,1),

            Item(15, "Spaghetti / Meatballs", "Protein", 1, 100, 100, true,2),
            Item(16, "Tuna", "Protein", 1, 100, 100, true,2),
            Item(17, "Beef Ravioli", "Protein", 1, 100, 100, true,2),
            Item(18, "Chicken", "Protein", 1, 100, 100, true,2),
            Item(19, "Peanut Butter", "Protein", 1, 100, 100, true,2),
            Item(20, "Beef Stew", "Protein", 1, 100, 100, true,2),

            Item(21, "Carrots", "Vegetables", 1, 100, 100, true,3),
            Item(22, "Potatoes", "Vegetables", 1, 100, 100, true,3),
            Item(23, "Corn", "Vegetables", 1, 100, 100, true,3),
            Item(24, "Green Beans", "Vegetables", 1, 100, 100, true,3),

            Item(25, "Peaches", "Fruits",1,100,100, true,4),
            Item(26, "Pears", "Fruits",1,100,100, true,4),
            Item(27, "Cranberry Juice", "Fruits",1,100,100, true,4),
            Item(28, "Dried Cranberries", "Fruits",1,100,100, true,4),
            Item(29, "Raisins", "Fruits",1,100,100, true,4),
            Item(30, "Apricots", "Fruits",1,100,100, true,4),
            Item(31, "Mandarin Oranges", "Fruits",1,100,100, true,4),
            Item(32, "Applesauce", "Fruits",1,100,100, true,4),
            Item(33, "Pineapple", "Fruits",1,100,100, true,4),
            Item(34, "Juice", "Fruits",1,100,100, true,4),

            Item(35, "Pinto","Beans",1,100,100,true,5),
            Item(36, "Refried","Beans",1,100,100,true,5),
            Item(37, "Chili","Beans",1,100,100,true,5),
            Item(38, "Pork & Beans","Beans",1,100,100,true,5),
            Item(39, "Kidney","Beans",1,100,100,true,5),
            Item(40, "Black","Beans",1,100,100,true,5),
            Item(41, "Dried Lentils","Beans",1,100,100,true,5),
            Item(42, "Dried Red Beans","Beans",1,100,100,true,5),
            Item(43, "Dried Black Beans","Beans",1,100,100,true,5),
            Item(44, "Dried Pinto Beans","Beans",1,100,100,true,5),

            Item(45, "Chicken Broth","Soups",1,100,100,true,6),
            Item(46, "Vegetarian","Soups",1,100,100,true,6),
            Item(47, "Tomato","Soups",1,100,100,true,6),
            Item(48, "Cream of Chicken","Soups",1,100,100,true,6),
            Item(49, "Cream of Mushroom","Soups",1,100,100,true,6),
            Item(50, "Chicken Noodle","Soups",1,100,100,true,6),
            Item(51, "Chicken/Dumpling","Soups",1,100,100,true,6),

            Item(52, "Sloppy Joe Mix","Tomato",1,100,100,true,7),
            Item(53, "Tomato Sauce","Tomato",1,100,100,true,7),
            Item(54, "Diced Tomatoes","Tomato",1,100,100,true,7),
            Item(55, "Pasta Sauce","Tomato",1,100,100,true,7),

            Item(56, "Oatmeal","Cereals",1,100,100,true,8),
            Item(57, "Oatmeal Packets","Cereals",1,100,100,true,8),
            Item(58, "Toasted Oats","Cereals",1,100,100,true,8),
            Item(59, "Corn Flakes","Cereals",1,100,100,true,8),
            Item(60, "Crisp Rice","Cereals",1,100,100,true,8),

            Item(61, "Stuffing","Sides",1,100,100,true,9),
            Item(62, "Instant Potatoes","Sides",1,100,100,true,9),
            Item(63, "White Rice","Sides",1,100,100,true,9),
            Item(64, "Brown Rice","Sides",1,100,100,true,9),

            Item(65, "Spaghetti","Pasta",1,100,100,true,10),
            Item(66, "Rotini/Cellentani","Pasta",1,100,100,true,10),
            Item(67, "Elbow Macaroni","Pasta",1,100,100,true,10),
            Item(68, "Spaghetti","Pasta",1,100,100,true,10),
            Item(69, "Egg Noodles","Pasta",1,100,100,true,10),

            Item(70, "Mac & Cheese","Meal Helper",1,100,100,true,11),
            Item(71, "Spaghetti Rings","Meal Helper",1,100,100,true,11),
            Item(72, "Lasagna Helper","Meal Helper",1,100,100,true,11),
            Item(73, "Cheeseburger Helper","Meal Helper",1,100,100,true,11),
            Item(74, "Beef Pasta Helper","Meal Helper",1,100,100,true,11),
            Item(75, "Alfredo Chicken Helper","Meal Helper",1,100,100,true,11),
            Item(76, "Strognaff Helper","Meal Helper",1,100,100,true,11),
            Item(77, "Tuna Helper","Meal Helper",1,100,100,true,11),

            Item(78, "Whole Wheat Flour","Extra",1,100,100,true,12),
            Item(79, "Dry Milk","Extra",1,100,100,true,12),
            Item(80, "Boxed Milk","Extra",1,100,100,true,12),
            Item(81, "Vegetable Oil","Extra",1,100,100,true,12),
            Item(82, "Mini Pies (Cherry/Apple)","Extra",1,100,100,true,12),
            Item(83, "Cheese Balls","Extra",1,100,100,true,12),

            Item(84, "Toothbrush","Nonedibles",1,100,100,true,13),
            Item(85, "Toothpaste","Nonedibles",1,100,100,true,13),
            Item(84, "Bar of Soap","Nonedibles",1,100,100,true,13),
            Item(84, "Toilet Paper","Nonedibles",1,100,100,true,13),
            Item(84, "Laundry Soap (Homemade- Limit 1)","Nonedibles",1,1,100,true,13)
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(myObjectCatalog)
    }

    fun sendCategoriesListToFireStore() {
        val categoriesListing = CategoriesListing()
        categoriesListing.categoriesListingName = "myCategories"
        categoriesListing.categories = listOf(
            Category(1,  "Meat", 2, 0),
            Category(2,"Protein", 1, 1),
            Category(3,"Vegetables", 2, 0),
            Category(4,"Fruits", 2, 0),
            Category(5,"Beans", 2, 0),
            Category(6,"Soups", 2, 0),
            Category(7,"Tomato", 1, 0),
            Category(8,"Cereals", 1, 0),
            Category(9,"Sides", 1, 0),
            Category(10,"Pasta", 1, 0),
            Category(11,"Meal Helper", 1, 0),
            Category(12,"Extra", 1, 0),
            Category(13,"Nonedibles", 1, 1)
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
                    compareBy<Item> { it.categoryId }.thenBy { it.itemID })
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

    private fun generateHeadings() {
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
                category.id,
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

    }

    fun removeItem(itemName: String) {
    }

    fun isOption(itemName: String): Boolean {
        val myList = itemList.value
        val selectedItem = myList?.find {
            it.name == itemName
        }
        return points!! > selectedItem?.qtyOrdered!!

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
