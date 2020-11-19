package com.md.williamriesen.hawkeyeharvestfoodbank.orderoffsite

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceId
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.ObjectCatalog
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.Order
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.CategoriesListing
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.Category
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodItem
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel() : ViewModel() {

    var accountID = "Turnip"
    var orderID: String? = null
    val itemList = MutableLiveData<MutableList<FoodItem>>()
    private var savedItemList = mutableListOf<FoodItem>()
    val outOfStockNameList: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf<String>())
    val categoriesList = MutableLiveData<MutableList<Category>>()

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
    var pickUpMonth = 0


    val accountNumberForDisplay: String
        get() = accountID.takeLast(4)

    private val whenOrdered: String
        get() {
            val foodBank = FoodBank()
            val startOfToday: Date = foodBank.getCurrentDateWithoutTime()
            val calendar = Calendar.getInstance()
            calendar.time = startOfToday
            calendar.add(Calendar.DAY_OF_YEAR,-1)
            val startOfYesterday = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startOfThisMonth = calendar.time
            Log.d("TAG",
            "lastOrderDate: $lastOrderDate, startOfToday: $startOfToday, startOfThisMonth: $startOfThisMonth")
            return when {
                lastOrderDate!! > startOfToday -> "TODAY"
                lastOrderDate!!> startOfYesterday -> "YESTERDAY"
                lastOrderDate!! > startOfThisMonth -> "EARLIER_THIS_MONTH"
                else -> "PRIOR_TO_THIS_MONTH"
            }
        }

    private val needToStartNewOrder: Boolean
        get() = orderState.value == "PACKED" && (!isOpen.value!! || whenOrdered != "TODAY")

    fun shop(view: View){
        Navigation.findNavController(view)
            .navigate(R.id.action_clientStartFragment_to_instructionsFragment)
    }

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
                    compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID })
                if (!needToStartNewOrder) retrieveSavedOrder()
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
                itemList.value = availableItemsList as MutableList<FoodItem>?
                retrieveCategoriesFromFireStore()
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
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

//    fun saveOrder(view: View) {
//        val thisOrder = Order(accountID, Date(), itemList.value!!, "SAVED")
//        val filteredOrder = filterOutZeros(thisOrder)
//        val db = FirebaseFirestore.getInstance()
//        if (orderID != null) {
//            db.collection(("orders")).document(orderID!!).set(filteredOrder)
//                .addOnSuccessListener {
//                    Log.d("TAG", "mayOrderNow: $mayOrderNow")
//                    if (mayOrderNow) {
//                        Navigation.findNavController(view)
//                            .navigate(R.id.action_checkoutFragment_to_askWhetherToSubmitSavedOrderFragment)
//                    } else {
//                        val action = if (true) {
//                            if (true) {
//                                TODO() // implement action if BOTH kinds of orders accepted.
//                            } else {
//                                R.id.action_checkoutFragment2_to_nextDayOrderConfirmedFragment
//                            }
//                        } else {
//                            R.id.action_checkoutFragment_to_orderSavedFragment
//                        }
//                        Navigation.findNavController(view).navigate(action)
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d("TAG", "save order failed with exception: $exception")
//                }
//        } else {
//            db.collection(("orders")).document().set(filteredOrder)
//                .addOnSuccessListener {
//                    if (mayOrderNow) {
//                        Navigation.findNavController(view)
//                            .navigate(R.id.action_checkoutFragment_to_askWhetherToSubmitSavedOrderFragment)
//                    } else {
//                        Navigation.findNavController(view)
//                            .navigate(R.id.action_checkoutFragment_to_orderSavedFragment)
//                    }
//                }
//        }
//    }

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






    private fun filterOutZeros(order: Order): Order {
        val itemList = order.itemList
        val filteredList = itemList.filter { item ->
            item.qtyOrdered != 0
        }
        val filteredOrder = Order()
        filteredOrder.itemList = filteredList as MutableList<FoodItem>
        filteredOrder.accountID = order.accountID
        filteredOrder.date = order.date
        filteredOrder.orderState = order.orderState
        filteredOrder.pickUpHour24 = order.pickUpHour24
        filteredOrder.pickUpMonth = order.pickUpMonth
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

    val takingOrders: Boolean
        get() = FoodBank().isTakingNextDayOrders

    val nextPickUpDay: String
        get() = simpleDateFormat.format(foodBank.nextDayOpen(afterTomorrow = true))

    val nextPreOrderDay: String
        get() = simpleDateFormat.format(foodBank.nextDayTakingOrders(afterToday = true))

    val nextDayTakingOrders: String
        get() = simpleDateFormat.format(foodBank.nextDayTakingOrders())

    val nextDayOpen: String?
        get() = simpleDateFormat.format(foodBank.nextDayOpen())

//    fun goToNextFragment(pickUpHour24Arg: Int, view: View) {
//        pickUpHour24 = pickUpHour24Arg
//        if (pickUpHour24Arg == 0) {
//            Navigation.findNavController(view)
//                .navigate(R.id.action_selectPickUpTimeFragment_to_returnAnotherDayFragment)
//        } else {
//            Navigation.findNavController(view)
//                .navigate(R.id.action_selectPickUpTimeFragment_to_selectionFragment2)
//        }
//    }
}
