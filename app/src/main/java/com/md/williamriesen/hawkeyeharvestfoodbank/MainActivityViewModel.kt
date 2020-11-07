package com.md.williamriesen.hawkeyeharvestfoodbank

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceId
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel() : ViewModel() {

    var accountID = "Turnip"
    var orderID: String? = null
    val itemList = MutableLiveData<MutableList<Item>>()
    private var savedItemList = mutableListOf<Item>()
    val outOfStockNameList: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf<String>())
    val categoriesList = MutableLiveData<MutableList<Category>>()
    private var familySizeFromFireStore: Long? = null
    var orderState: MutableLiveData<String> = MutableLiveData("NONE")
    var familySize = 0
    var points: Int? = null
    var lastOrderDate: Date? = null
    var lastOrderTimestamp: Timestamp? = null
    var categories: MutableLiveData<MutableList<Category>> =
        MutableLiveData(mutableListOf())
    var pleaseWait = MutableLiveData<Boolean>()
    var isOpen = MutableLiveData<Boolean>(false)
    var pickUpHour24 = 0

    val accountNumberForDisplay: String
        get() = accountID.takeLast(4)

    private val whenOrdered: String
        get() {
            val foodBank = FoodBank()
            val startOfToday: Date = foodBank.getCurrentDateWithoutTime()
            val calendar = Calendar.getInstance()
            calendar.time = startOfToday
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startOfThisMonth = calendar.time
            return when {
                lastOrderDate!! > startOfToday -> "TODAY"
                lastOrderDate!! > startOfThisMonth -> "EARLIER_THIS_MONTH"
                else -> "PRIOR_TO_THIS_MONTH"
            }
        }

    private val needToStartNewOrder: Boolean
        get() = orderState.value == "PACKED" && (!isOpen.value!! || whenOrdered != "TODAY")

    private fun retrieveCategoriesFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("categories").document("categories")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesList.value = categoriesListing?.categories as MutableList<Category>
                generateHeadings()
                val filteredList = itemList.value?.filter {
                    canAfford(it)
                } as MutableList

                itemList.value = filteredList
                itemList.value?.sortWith(
                    compareBy<Item> { it.categoryId }.thenBy { it.itemID })
                if (!needToStartNewOrder) retrieveSavedOrder()
            }
    }

    fun retrieveObjectCatalogFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
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

    fun canAfford(item: Item): Boolean {
        Log.d("TAG", "item.name ${item.name}")
        Log.d("TAG", "item.category ${item.category}")

        val thisCategory = categoriesList.value?.find {
            it.name == item.category
        }
        Log.d("TAG", "thisCategory.name ${thisCategory!!.name}")
        val pointsAllocated = thisCategory.calculatePoints(familySize)
        Log.d("TAG", "pointsAllocated $pointsAllocated")
        Log.d("TAG", "pointValue: ${item.pointValue}")
        Log.d("TAG", "return ${pointsAllocated >= item.pointValue!!}")
        return pointsAllocated >= item.pointValue!!
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
                    savedItemList = savedOrder?.itemList!!
                    orderID = querySnapshot.documents[0].id
                    checkSavedOrderAgainstCurrentOfferings()
                }
            }
    }

    private fun checkSavedOrderAgainstCurrentOfferings() {
        savedItemList.forEach { savedItem ->
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
                categoryToUpdate!!.categoryPointsUsed =
                    categoryToUpdate.categoryPointsUsed + itemToCheck.pointValue!! * itemToCheck.qtyOrdered
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
            itemList.value!!.add(heading)
        }
    }


    fun addItem(itemName: String) {
        val myList = itemList.value
        val selectedItem = myList?.find {
            it.name == itemName
        }
        selectedItem?.qtyOrdered = selectedItem?.qtyOrdered?.plus(1)!!
        val selectedCategoryHeading = myList.find {
            it.name == selectedItem.category
        }
        selectedCategoryHeading?.categoryPointsUsed!!.plus(1)

    }

    fun removeItem() {
    }

    fun signIn(enteredAccountID: String, context: Context) {
        isOpen.value = false
        pleaseWait.value = true
        if (enteredAccountID == "STAFF") {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            pleaseWait.value = false
            context.startActivity(intent)
        } else {
            val myFirebaseMessagingService = MyFirebaseMessagingService()
            val token = myFirebaseMessagingService
            familySizeFromFireStore = null
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("accounts").document(enteredAccountID)
            docRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val intent = if (acceptNextDayOrders) {
                            if (acceptSameDayOrders) {
                                TODO() // Implement logic for when accepting BOTH next day and same day orders
                            } else {
                                Intent(context, NextDayOrderActivity::class.java)
                            }
                        } else {
                            Intent(context, MainActivity::class.java)
                        }
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("ACCOUNT_ID", enteredAccountID)
                        orderState.value = if (documentSnapshot["orderState"] != null) {
                            documentSnapshot["orderState"] as String
                        } else {
                            "NOT STARTED YET"
                        }
                        intent.putExtra("ORDER_STATE", orderState.value)
                        familySizeFromFireStore = documentSnapshot["familySize"] as Long
                        familySize = familySizeFromFireStore!!.toInt()
                        intent.putExtra("FAMILY_SIZE", familySize)
                        lastOrderTimestamp = documentSnapshot["lastOrderDate"] as Timestamp
                        intent.putExtra("LAST_ORDER_DATE_TIMESTAMP", lastOrderTimestamp)
                        pleaseWait.value = false
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(
                            context,
                            "Sorry, Not a valid account.",
                            Toast.LENGTH_LONG
                        ).show()
                        pleaseWait.value = false
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Retrieve family size from database failed.")
                }
        }
    }

    fun saveOrder(view: View) {
        val thisOrder = Order(accountID, Date(), itemList.value!!, "SAVED")
        val filteredOrder = filterOutZeros(thisOrder)
        val db = FirebaseFirestore.getInstance()
        if (orderID != null) {
            db.collection(("orders")).document(orderID!!).set(filteredOrder)
                .addOnSuccessListener {
                    Log.d("TAG", "mayOrderNow: $mayOrderNow")
                    if (mayOrderNow) {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_checkoutFragment_to_askWhetherToSubmitSavedOrderFragment)
                    } else {
                        val action = if (acceptNextDayOrders){
                            if (acceptSameDayOrders){
                                TODO() // implement action if BOTH kinds of orders accepted.
                            } else {
                                R.id.action_checkoutFragment2_to_nextDayOrderConfirmedFragment
                            }
                        } else {
                            R.id.action_checkoutFragment_to_orderSavedFragment
                        }
                        Navigation.findNavController(view).navigate(action)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "save order failed with exception: $exception")
                }
        } else {
            db.collection(("orders")).document().set(filteredOrder)
                .addOnSuccessListener {
                    if (mayOrderNow) {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_checkoutFragment_to_askWhetherToSubmitSavedOrderFragment)
                    } else {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_checkoutFragment_to_orderSavedFragment)
                    }
                }
        }
    }

    fun postSaveNavigation(view: View) {
        if (mayOrderNow) {
            Navigation.findNavController(view)
                .navigate(R.id.action_checkoutFragment_to_askWhetherToSubmitSavedOrderFragment)
        } else {
            Navigation.findNavController(view)
                .navigate(R.id.action_checkoutFragment_to_orderSavedFragment)
        }
    }


    fun saveOrderWithoutNavigating() {
        val thisOrder = Order(accountID, Date(), itemList.value!!, "SAVED")
        val filteredOrder = filterOutZeros(thisOrder)
        val db = FirebaseFirestore.getInstance()
        if (orderID != null) {
        } else {
            db.collection(("orders")).document().set(filteredOrder)
        }
    }

    fun submitOrder(view: View) {
        val thisOrder = Order(accountID, Date(), itemList.value!!, "SUBMITTED")
        val filteredOrder = filterOutZeros(thisOrder)

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
                            Navigation.findNavController(view)
                                .navigate(R.id.action_askWhetherToSubmitSavedOrderFragment_to_orderSubmittedFragment)
                        }
                } else {
                    db.collection(("orders")).document().set(filteredOrder)
                        .addOnSuccessListener {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_askWhetherToSubmitSavedOrderFragment_to_orderSubmittedFragment)
                        }
                }
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

    val mayOrderNow: Boolean
        get() =
            isOpen.value!! &&
                    !(orderState.value == "PACKED" && whenOrdered == "EARLIER_THIS_MONTH") &&
                    !(orderState.value == "NO SHOW" && whenOrdered == "EARLIER_THIS_MONTH")

    val nextFragment: Int
        get() {
            Log.d("TAG", "nextFragment getter called.")
            Log.d("TAG", "isOpen.value: ${isOpen.value}")
            Log.d("TAG", "orderState.value ${orderState.value}")
            Log.d("TAG", "whenOrdered: $whenOrdered")
            return if (isOpen.value!!) {
                when (orderState.value) {
                    "SAVED" -> R.id.action_clientStartFragment_to_shopVsCheckOutFragment
                    "SUBMITTED" -> {
                        Log.d("TAG", "whenOrdered: $whenOrdered")
                        if (whenOrdered == "TODAY") {
                            R.id.action_clientStartFragment_to_orderBeingPackedFragment
                        } else {
                            R.id.action_clientStartFragment_to_errorMessageFragment
                        }
                    }
                    "PACKED" -> {
                        when (whenOrdered) {
                            "TODAY" -> R.id.action_clientStartFragment_to_orderReadyFragment
                            "EARLIER_THIS_MONTH" -> R.id.action_clientStartFragment_to_shopForNextMonthFragment
                            else -> R.id.action_clientStartFragment_to_instructionsFragment
                        }
                    }
                    "NO SHOW" -> {
                        if (whenOrdered == "PRIOR_TO_THIS_MONTH") {
                            R.id.action_clientStartFragment_to_instructionsFragment
                        } else {
                            R.id.action_clientStartFragment_to_notPickedUpFragment
                        }
                    }
                    else -> R.id.action_clientStartFragment_to_instructionsFragment
                }
            } else {
                when (orderState.value) {
                    "SAVED" -> R.id.action_clientStartFragment_to_reviseSavedOrderOptionFragment
                    "SUBMITTED" -> R.id.action_clientStartFragment_to_errorMessageFragment
                    "PACKED" -> R.id.action_clientStartFragment_to_shopForNextMonthFragment
                    "NO SHOW" -> {
                        if (whenOrdered == "PRIOR_TO_THIS_MONTH") {
                            R.id.action_clientStartFragment_to_instructionsFragment
                        } else {
                            R.id.action_clientStartFragment_to_notPickedUpFragment
                        }
                    }
                    else -> R.id.action_clientStartFragment_to_instructionsFragment

                }
            }
        }

    val foodBank = FoodBank()
    val simpleDateFormat = SimpleDateFormat("E, MMM d")
    val takingOrders : Boolean
        get() = FoodBank().isTakingNextDayOrders
    val nextPickUpDay: String
        get() = simpleDateFormat.format(foodBank.nextDayOpen(afterTomorrow = true))

    val nextPreOrderDay : String
        get() = simpleDateFormat.format(foodBank.nextDayTakingOrders(afterToday = true))

    val nextDayTakingOrders: String
        get() = simpleDateFormat.format(foodBank.nextDayTakingOrders())

    val nextDayOpen : String?
        get() = simpleDateFormat.format(foodBank.nextDayOpen())

    fun goToNextFragment(pickUpHour24Arg: Int, view: View){
        pickUpHour24 = pickUpHour24Arg
        if (pickUpHour24Arg == 0){
            Navigation.findNavController(view).navigate(R.id.action_selectPickUpTimeFragment_to_returnAnotherDayFragment)

        } else {
            Navigation.findNavController(view).navigate(R.id.action_selectPickUpTimeFragment_to_selectionFragment2)
        }
    }
}
