package com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceId
import com.md.williamriesen.hawkeyeharvestfoodbank.*
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.*
import java.text.SimpleDateFormat
import java.util.*

class NextDayOrderingActivityViewModel : ViewModel() {

    var accountID = ""
    var lastOrderDate: Date? = null
//    var orderState = "NOT_STARTED_YET"
    var familySize = 0
    var points: Int? = null
    val foodItemList = MutableLiveData<MutableList<FoodItem>>()
    val categoriesList = MutableLiveData<MutableList<Category>>()

    var pickUpHour24 = 0
    val foodBank = FoodBank()
    var orderID: String? = null
    val simpleDateFormat = SimpleDateFormat("E, MMM d")

    val nextDayOpen: String?
        get() = simpleDateFormat.format(foodBank.nextDayOpen())

    val nextDayTakingOrders: String
        get() = simpleDateFormat.format(foodBank.nextDayTakingOrders())

    val nextPickUpDay: String
        get() = simpleDateFormat.format(foodBank.nextDayOpen(afterTomorrow = true))

    val nextPreOrderDay: String
        get() = simpleDateFormat.format(foodBank.nextDayTakingOrders(afterToday = true))

    val returnOnMessage: String
        get() = "Please return to this app on $nextDayTakingOrders before 5 PM, to place your order for pick up on $nextDayOpen"

    val takingOrders: Boolean
        get() = FoodBank().isTakingNextDayOrders

    val accountNumberForDisplay: String
        get() = accountID.takeLast(4)


    fun goToNextFragment(pickUpHour24Arg: Int, view: View) {
        pickUpHour24 = pickUpHour24Arg
        if (pickUpHour24Arg == 0) {
            Navigation.findNavController(view)
                .navigate(R.id.action_selectPickUpTimeFragment_to_returnAnotherDayFragment)

        } else {
            Navigation.findNavController(view)
                .navigate(R.id.action_selectPickUpTimeFragment_to_nextDayOrderSelectionFragment)
        }
    }

    fun retrieveObjectCatalogFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                val availableItemsList = myObjectCatalog?.foodItemList?.filter { item ->
                    item.isAvailable as Boolean
                }
                foodItemList.value = availableItemsList as MutableList<FoodItem>?
                retrieveCategoriesFromFireStore()
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
            }
    }

    private fun retrieveCategoriesFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("categories").document("categories")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesList.value = categoriesListing?.categories as MutableList<Category>
                generateHeadings()
                val filteredList = foodItemList.value?.filter {
                    canAfford(it)
                } as MutableList

                foodItemList.value = filteredList
                foodItemList.value?.sortWith(
                    compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID })
                Log.d("TAG", "Data retrieval done.")
                foodItemList.value!!.forEach { item ->
                    Log.d("TAG", "${item.name}")
                }

//                if (!needToStartNewOrder) retrieveSavedOrder()
            }
    }

    private fun generateHeadings() {
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
            foodItemList.value!!.add(heading)
        }
    }

    fun canAfford(foodItem: FoodItem): Boolean {
        val thisCategory = categoriesList.value?.find {
            it.name == foodItem.category
        }
        val pointsAllocated = thisCategory!!.calculatePoints(familySize)
        return pointsAllocated >= foodItem.pointValue!!
    }

    private fun retrieveSavedOrder() {
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
//                    savedItemList = savedOrder?.itemList!!
                    orderID = querySnapshot.documents[0].id
//                    checkSavedOrderAgainstCurrentOfferings()
                }
            }
    }

    fun saveOrder() {
        val thisOrder = Order(accountID, Date(), foodItemList.value!!, "SAVED")
        val filteredOrder = thisOrder.filterOutZeros()
        val db = FirebaseFirestore.getInstance()
        if (orderID != null) {
            db.collection(("orders")).document(orderID!!).set(filteredOrder)
        } else {
            db.collection(("orders")).document().set(filteredOrder)
                .addOnSuccessListener {
                    retrieveSavedOrder()
// Save then immediately retrieve is done this way to obtain orderID //
// which is subsequently used to submit.//
// The transition from "SAVED" to "SUBMITTED" status is unnecessary on the client side,//
// but is done this way to fire a rule on the server side which is working and //
// I would rather not change.
                }
        }
    }

    fun submitNextDayOrder(view: View) {
        Log.d("TAG", "Starting submitNextDayOrder...")
        val thisOrder = Order(
            accountID,
            Date(),
            foodItemList.value!!,
            "SUBMITTED",
            pickUpHour24,
            foodBank.monthTomorrow
        )
        val filteredOrder = thisOrder.filterOutZeros()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("TAG", "getInstanceID failed ${it.exception}")
                }

                val token = it.result?.token
                filteredOrder.deviceToken = token
                Log.d("TAG", "token: $token")

                val db = FirebaseFirestore.getInstance()
                if (orderID != null) {
                    db.collection(("orders")).document(orderID!!).set(filteredOrder)
                        .addOnSuccessListener {
                            Log.d("TAG", "Updated order save successful.")
                            Navigation.findNavController(view)
                                .navigate(R.id.action_nextDayCheckoutFragment_to_nextDayOrderConfirmedFragment)
                        }
                } else {
                    db.collection(("orders")).document().set(filteredOrder)
                        .addOnSuccessListener {
                            Log.d("TAG", "New order save successful.")
                            Navigation.findNavController(view)
                                .navigate(R.id.action_nextDayCheckoutFragment_to_nextDayOrderConfirmedFragment)
                        }
                }
            }
    }
}