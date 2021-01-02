package com.md.williamriesen.hawkeyeharvest.orderonsite

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceId
import com.md.williamriesen.hawkeyeharvest.*
import com.md.williamriesen.hawkeyeharvest.foodbank.*
import com.md.williamriesen.hawkeyeharvest.signin.Account
import com.md.williamriesen.hawkeyeharvest.signin.AccountService
import java.util.*

class OnSiteOrderingViewModel : ViewModel() {
    var accountService: AccountService? = null

    val foodItemList = MutableLiveData<MutableList<FoodItem>>()
    var orderID: String? = null
    var isOpen = MutableLiveData<Boolean>(false)
    var orderState: MutableLiveData<OrderState> = MutableLiveData(OrderState.NOT_STARTED_YET)
    var lastOrderDate: Date? = null
    val categoriesList = MutableLiveData<MutableList<Category>>()
    private var savedItemList = mutableListOf<FoodItem>()
    var points: Int? = null
    val outOfStockNameList: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf<String>())

    // Forwards active account property from the AccountService.
    // Unsure if this is a great pattern or not.
    val activeAccount: Account?
        get() = accountService?.activeAccount

    private val needToStartNewOrder: Boolean
        get() = orderState.value == OrderState.PACKED && (!isOpen.value!! || whenOrdered != "TODAY")

    fun shop(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_clientStartFragment_to_instructionsFragment)
    }

    fun navigateToNextFragment(view: View) {
        Log.d("TAG", "nextFragment: $nextFragment")
        Navigation.findNavController(view).navigate(nextFragment)
    }

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
    val accountNumberForDisplay: String
        get() = accountService?.activeAccount?.accountID?.takeLast(4) ?: ""

    val nextFragment: Int
        get() {
            return when (orderState.value) {
                OrderState.SAVED -> R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
                OrderState.SUBMITTED -> {
                    R.id.action_onSiteOrderStartFragment_to_onSiteOrderBeingPackedFragment
                }
                OrderState.BEING_PACKED -> {
                    R.id.action_onSiteOrderStartFragment_to_onSiteOrderBeingPackedFragment
                }
                OrderState.PACKED -> {
                    when (whenOrdered) {
                        "TODAY" -> R.id.action_onSiteOrderStartFragment_to_onSiteOrderReadyFragment
                        "EARLIER_THIS_MONTH" -> R.id.action_onSiteOrderStartFragment_to_onSiteOrderReadyFragment
                        else -> R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
                    }
                }
                OrderState.NO_SHOW -> {
                    R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
                }
                else -> R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
            }
        }

    fun submitOnSiteOrder(view: View) {
        val thisOrder = Order(activeAccount?.accountID ?: "", Date(), foodItemList.value!!, "SUBMITTED")
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
                db.useEmulator("10.0.2.2", 8080)

                Log.d("TAG", "orderID: $orderID")
                if (orderID != null) {
                    db.collection(("orders")).document(orderID!!).set(filteredOrder)
                        .addOnSuccessListener {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_onSiteCheckoutFragment_to_onSiteOrderConfirmedFragment)
                        }
                } else {
                    Log.d("TAG", "about to attempt Submit.")
                    db.collection(("orders")).document().set(filteredOrder)
                        .addOnSuccessListener {
                            Log.d("TAG", "filteredOrder $filteredOrder")
                            Navigation.findNavController(view)
                                .navigate(R.id.action_onSiteCheckoutFragment_to_onSiteOrderConfirmedFragment)
                        }
                        .addOnFailureListener {
                            Log.d("TAG", "Submit failed due to $it")
                        }
                }
            }
    }

    fun saveOrder() {
        // TODO fix this
        val thisOrder = Order(activeAccount?.accountID ?: "", Date(), foodItemList.value!!, "SAVED")
        val filteredOrder = thisOrder.filterOutZeros()

        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)

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

    fun retrieveObjectCatalogFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)

        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                if (myObjectCatalog != null) {
                    Log.d("TAG", "myObjectCatalog.foodItemList: ${myObjectCatalog.foodItemList}")
                }
                val availableItemsList = myObjectCatalog?.foodItemList?.filter { foodItem ->
                    foodItem.isAvailable as Boolean

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
        db.useEmulator("10.0.2.2", 8080)

        val docRef = db.collection("categories").document("categories")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesList.value = categoriesListing?.categories as MutableList<Category>
                val filteredList = foodItemList.value?.filter {
                    canAfford(it)
                } as MutableList
                foodItemList.value = filteredList
                generateHeadings()
                foodItemList.value?.sortWith(
                    compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID })

                if (!needToStartNewOrder) retrieveSavedOrder()
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
                category.calculatePoints((activeAccount?.familySize ?: 0L).toInt())
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
        val pointsAllocated = thisCategory.calculatePoints((activeAccount?.familySize ?: 0L).toInt())
        Log.d("TAG", "pointsAllocated $pointsAllocated")
        Log.d("TAG", "pointValue: ${foodItem.pointValue}")
        Log.d("TAG", "return ${pointsAllocated >= foodItem.pointValue!!}")
        return pointsAllocated >= foodItem.pointValue!!
    }

    private fun retrieveSavedOrder() {
        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)

        val ordersRef = db.collection("orders")
        val query = ordersRef
            .whereEqualTo("accountID", activeAccount?.accountID ?: "")
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

}