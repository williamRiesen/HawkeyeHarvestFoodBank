package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.*
import com.md.williamriesen.hawkeyeharvest.orderonsite.OnSiteOrderingViewModel
import java.sql.Timestamp
import java.util.*

class SecureTabletOrderViewModel : ViewModel() {
    var accountID = ""
    val foodItemList = MutableLiveData<MutableList<FoodItem>>()
    val categoriesList = MutableLiveData<MutableList<Category>>()
    var familySize = 0
    private var savedItemList = mutableListOf<FoodItem>()
    var orderID: String? = null
    var isOpen = MutableLiveData<Boolean>(false)
    var orderState: MutableLiveData<OrderState> = MutableLiveData(OrderState.NOT_STARTED_YET)
    var lastOrderDate: Date? = null
    var points: Int? = null

    private val needToStartNewOrder: Boolean
        get() = orderState.value == OrderState.PACKED && (!isOpen.value!! || whenOrdered != "TODAY")

    private val whenOrdered: String
        get() {
            val foodBank = FoodBank()
            val startOfToday: Date = foodBank.getCurrentDateWithoutTime()
            val calendar = Calendar.getInstance()
            calendar.time = startOfToday
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startOfThisMonth = calendar.time
            Log.d(
                "TAG",
                "lastOrderDate: $lastOrderDate, startOfToday: $startOfToday, startOfThisMonth: $startOfThisMonth"
            )
            return when {
                lastOrderDate!! > startOfToday -> "TODAY"
                lastOrderDate!! > startOfThisMonth -> "EARLIER_THIS_MONTH"
                else -> "PRIOR_TO_THIS_MONTH"
            }
        }

    fun lookUpAccount(accountNumber: Int, context: Context, view: View) {

        val db = FirebaseFirestore.getInstance()
        val accountsRef = db.collection("accounts")
        Log.d("TAG", "ready to look up account using number $accountNumber")
        val query = accountsRef
            .whereEqualTo("accountNumber", accountNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                when (querySnapshot.size()) {
                    0 -> Toast.makeText(
                        context,
                        "No match found for this number.",
                        Toast.LENGTH_LONG
                    ).show()
                    1 -> {
                        val document = querySnapshot.documents[0]
                        accountID = document.id
                        familySize = (document.get("familySize") as Long).toInt()
                        val orderStateString = document.get("orderState") as String
                        val timeStamp =
                            document.get("lastOrderDate") as com.google.firebase.Timestamp
                        val lastOrderDate = Date(timeStamp.seconds * 1000)


                        val calendar = Calendar.getInstance()
                        val thisMonth = calendar[Calendar.MONTH]
                        val thisYear = calendar[Calendar.YEAR]
                        val startOfMonth = FoodBank().makeDate(thisMonth, 1, thisYear)
                        Log.d ("TAG", "accountID $accountID")
                        val orderedAlready =
                            lastOrderDate > startOfMonth && orderStateString != "SAVED"

                        if (orderedAlready) {
                            Log.d("TAG","ordered already")
                            Navigation.findNavController(view)
                                .navigate(R.id.action_secureTabletOrderStartFragment_to_alreadyOrderedMessageFragment)
                        } else {
                            Log.d("TAG","Not ordered already: $orderStateString")
                            retrieveObjectCatalogFromFireStore(view)
                        }
                    }


                    else -> Toast.makeText(
                        context,
                        "Multiple matches, should not be: please contact Dr. Riesen.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun retrieveObjectCatalogFromFireStore(view: View) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                if (myObjectCatalog != null) {
//                    Log.d("TAG", "myObjectCatalog.foodItemList: ${myObjectCatalog.foodItemList}")
                }
                val availableItemsList = myObjectCatalog?.foodItemList?.filter { foodItem ->
                    foodItem.isAvailable!! && foodItem.numberAvailable!! > 0
                }
                foodItemList.value = availableItemsList as MutableList<FoodItem>?
                retrieveCategoriesFromFireStore(view)
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
            }
    }

    private fun retrieveCategoriesFromFireStore(view: View) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("categories").document("categories")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesList.value = categoriesListing?.categories as MutableList<Category>
                val filteredList = foodItemList.value?.filter {
                    canAfford(it)
                } as MutableList
                foodItemList.value = filteredList
                generateHeadings(view)
                foodItemList.value?.sortWith(
                    compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID })
                if (!needToStartNewOrder) retrieveSavedOrder(view)
                Navigation.findNavController(view)
                    .navigate(R.id.action_secureTabletOrderStartFragment_to_secureTabletOrderSelectionFragment)
            }
    }

    private fun generateHeadings(view: View) {
        categoriesList.value?.forEach { category ->
            val heading = FoodItem(
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
            Log.d("TAG", "foodItemList.value = ${foodItemList.value}")
            foodItemList.value!!.add(heading)
        }
    }

    fun canAfford(foodItem: FoodItem): Boolean {
        Log.d("TAG", "foodItem.name ${foodItem.name}")
        Log.d("TAG", "foodItem.category ${foodItem.category}")

        val thisCategory = categoriesList.value?.find {
            it.name == foodItem.category
        }
        Log.d("TAG", "thisCategory.name ${thisCategory!!.name}")
        val pointsAllocated = thisCategory.calculatePoints(familySize)
        Log.d("TAG", "pointsAllocated $pointsAllocated")
        Log.d("TAG", "pointValue: ${foodItem.pointValue}")
        Log.d("TAG", "return ${pointsAllocated >= foodItem.pointValue!!}")
        return pointsAllocated >= foodItem.pointValue!!
    }

    private fun retrieveSavedOrder(view: View) {
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val query = ordersRef
            .whereEqualTo("accountID", accountID)
            .whereEqualTo("orderState", "SAVED")
            .orderBy("date", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.size() > 0) {
                    val savedOrder = querySnapshot.documents[0].toObject<Order>()
                    savedItemList = savedOrder?.itemList!!
                    orderID = querySnapshot.documents[0].id
                    checkSavedOrderAgainstCurrentOfferings()
                }
//                Navigation.findNavController(view)
//                    .navigate(R.id.action_secureTabletOrderStartFragment_to_secureTabletOrderSelectionFragment)
            }
    }

    private fun checkSavedOrderAgainstCurrentOfferings() {
        savedItemList.forEach { savedItem ->
            val itemToCheck = foodItemList.value?.find { offeredItem ->
                offeredItem.name == savedItem.name
            }
            if (itemToCheck == null) {
                outOfStockNameList.value!!.add(savedItem.name!!)
            } else {
                itemToCheck.qtyOrdered = savedItem.qtyOrdered
                val categoryToUpdate = foodItemList.value!!.find { item ->
                    item.name == itemToCheck.category
                }
                categoryToUpdate!!.categoryPointsUsed =
                    categoryToUpdate.categoryPointsUsed + itemToCheck.pointValue!! * itemToCheck.qtyOrdered
            }
        }
    }

    fun saveOrder(view: View) {
        val thisOrder = Order(accountID, Date(), foodItemList.value!!, "SAVED")
        val filteredOrder = thisOrder.filterOutZeros()
        val db = FirebaseFirestore.getInstance()
        if (orderID != null) {
            db.collection(("orders")).document(orderID!!).set(filteredOrder)
        } else {
            db.collection(("orders")).document().set(filteredOrder)
                .addOnSuccessListener {
                    retrieveSavedOrder(view)
// Save then immediately retrieve is done this way to obtain orderID //
// which is subsequently used to submit.//
// The transition from "SAVED" to "SUBMITTED" status is unnecessary on the client side,//
// but is done this way to fire a rule on the server side which is working and //
// I would rather not change.
                }
        }
    }

    val outOfStockNameList: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf<String>())

}