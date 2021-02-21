package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.*
import java.util.*


class SecureTabletOrderViewModel : ViewModel() {
//
    var account = Account("", 0, "", "", 0)
    var accountNumber: Int? = null
    val categories = MutableLiveData<MutableList<Category>>()
//    val db = FirebaseFirestore.getInstance()
    val foodItems = MutableLiveData<MutableList<FoodItem>>(mutableListOf())
//    var order = Order("", Date(), mutableListOf(FoodItem()), "")
//    private var orderID: String? = null
//    var orderState: MutableLiveData<OrderState> = MutableLiveData(OrderState.NOT_STARTED_YET)
    val outOfStockItems = MutableLiveData<MutableList<OutOfStockItem>>()
    var pleaseWait = MutableLiveData<Boolean>(false)
    var points: Int? = null
    var startupAccountNumber: Int? = null
//    var view: View? = null
//
//
    fun saveOrder() {
//        assembleOrderAs("SAVED")
//        sendOrderToFirestore
//            .continueWith { retrieveOrder }
//            .continueWith { transcribeOrderID }
    }
//
//    private val sendOrderToFirestore: Task<Void> =
//        db.collection(("orders")).document(orderID ?: "").set(order)
//
//
//    private val retrieveOrder =
//        db.collection("orders")
//            .whereEqualTo("accountID", account.accountID)
//            .whereEqualTo("orderState", "SAVED")
//            .orderBy("date", Query.Direction.DESCENDING).limit(1)
//            .get()
//
//    private val transcribeOrderID = Continuation<QuerySnapshot, Unit> { task ->
//        if (task.result.size() > 0) {
//            orderID = task.result.documents[0].id
//        }
//    }
//
//
    fun processOrder(viewArg: View) {
//        view = viewArg
//        retrieveInventory()
//            .continueWith { task ->
//                val retrievedInventory = task.result.get("foodItemList") as List<FoodItem>
//                updateFoodItemListUsing(retrievedInventory)
//                separateOutOfStockItems()
//                if (outOfStockItems.value!!.isEmpty()) {
//                    assembleOrderAs("SUBMITTED")
//                    submit(order)
//                } else {
//                    askClientForAlternativeChoices()
//                }
//            }
    }
//
//    private fun retrieveInventory(): Task<DocumentSnapshot> {
//        return db.collection("catalogs").document("objectCatalog").get()
//    }
//
//    private fun updateFoodItemListUsing(retrievedList: List<FoodItem>) {
//        foodItems.value!!.forEach { foodListItem ->
//            if (foodListItem.isFoundIn(retrievedList)) {
//                foodListItem.updateUsing(retrievedList)
//            } else {
//                foodListItem.isAvailable = false
//            }
//        }
//    }
//
//    private fun updateFoodItemListUsing(order: Order) {
//        order.itemList.forEach { orderedFoodItem ->
//            foodItems.value?.find { viewModelFoodItem ->
//                viewModelFoodItem.name == orderedFoodItem.name
//            }!!.qtyOrdered = orderedFoodItem.qtyOrdered
//        }
//    }
//
//    private fun separateOutOfStockItems() {
//        outOfStockItems.value?.clear()
//        foodItems.value?.forEach {
//            if (it.qtyOrdered > 0 && !it.isAvailable!!) {
//                outOfStockItems.value?.add(OutOfStockItem(it.name!!, it.qtyOrdered))
//                refundPointsFor(it)
//                it.qtyOrdered = 0
//            }
//        }
//    }
//
//    private fun assembleOrderAs(orderState: String) {
//        order = Order(account.accountID, Date(), foodItems.value!!, orderState)
//        order.filterOutZeros()
//    }
//
//    private fun submit(orderArg: Order) {
//        order = orderArg
//        FirebaseInstanceId.getInstance().instanceId
//            .continueWith(getToken)
//            .continueWithTask { sendOrderToFirestore }
//            .continueWith { returnToStart() }
//    }
//
//    private val getToken = Continuation<InstanceIdResult, Unit> {
//        order.deviceToken = it.result.token
//        it.result
//    }
//
//
//    private fun refundPointsFor(foodItem: FoodItem) {
//        val categoryOfItem = foodItems.value?.find {
//            it.name == foodItem.name
//        }?.category
//        categories.value?.find { category ->
//            categoryOfItem == category.name
//        }!!.pointsUsed -= foodItem.qtyOrdered
//    }
//
//    private fun askClientForAlternativeChoices() {
//        Navigation.findNavController(view!!)
//            .navigate(R.id.action_secureTabletOrderConfirmAndReset_to_outOfStockFragment2)
//    }
//
//
    fun go(accountNumber: Int, viewArg: View) {
//        Log.d("TAG", "Starting Go function")
////        view = viewArg
////        lookUpAccount(accountNumber)
////            .continueWith(validateAccount)
////            .continueWith { validAccount ->
////                if (validAccount.result != null) {
////                    account = validAccount.result!!
////                    if (orderedAlready()) {
////                        navigateToAlreadyOrderedMessage()
////                    } else {
////                        prepareSelections()
////                    }
////                }
////            }
    }
//
//    private fun lookUpAccount(accountNumber: Int) =
//        db.collection("accounts").whereEqualTo("accountNumber", accountNumber).get()
//
//    private val validateAccount = Continuation<QuerySnapshot, Account?> { task ->
//        val accountFetchResult = task.result
//        if (isUniqueMatch(accountFetchResult)) {
//            task.result.documents[0].toObject(Account::class.java)
//        } else {
//            null
//        }
//    }
//
//    private fun isUniqueMatch(querySnapshot: QuerySnapshot): Boolean {
//        return when (querySnapshot.size()) {
//            0 -> {
//                toast("No match found for this number.")
//                false
//            }
//            1 -> true
//
//            else -> {
//                toast("Multiple matches: this should not be. Please contact Dr. Riesen.")
//                false
//            }
//        }
//    }
//
//    private fun orderedAlready(): Boolean {
//        val calendar = Calendar.getInstance()
//        val thisMonth = calendar[Calendar.MONTH]
//        val thisYear = calendar[Calendar.YEAR]
//        val startOfMonth = FoodBank().makeDate(thisMonth, 1, thisYear)
//        return (account.lastOrderDate > startOfMonth
//                && account.orderState != "SAVED")
//    }
//
//    private fun navigateToAlreadyOrderedMessage() {
//        Navigation.findNavController(view!!)
//            .navigate(R.id.action_secureTabletOrderStartFragment_to_alreadyOrderedMessageFragment)
//    }
//
//
//    private fun prepareSelections() {
//        getInventoryFromFirestore
//            .continueWith(transcribeInventoryToViewModel)
//            .continueWithTask { getCategoriesFromFireStore }
//            .continueWith(insertCategoriesIntoFoodListAndSort)
//            .continueWithTask { retrieveSavedOrder }
//            .continueWith(unpackAndCheckSavedOrder)
//            .continueWith { someItemsAreOutOfStock ->
//                if (someItemsAreOutOfStock.result) {
//                    askClientForAlternativeChoices()
//                } else {
//                    navigateToSelectionFragment()
//                }
//            }
//    }
//
//    private val unpackAndCheckSavedOrder = Continuation<QuerySnapshot, Boolean> { task ->
//        var someItemsAreOutOfStock = false
//        if (task.result.size() > 0) {
//            orderID = task.result.documents[0].id
//            val order = task.result.documents[0].toObject<Order>()
//            updateFoodItemListUsing(order!!)
//            separateOutOfStockItems()
//            someItemsAreOutOfStock = !outOfStockItems.value.isNullOrEmpty()
//        }
//        someItemsAreOutOfStock
//    }
//
//
//    private val getInventoryFromFirestore =
//        db.collection("catalogs").document("objectCatalog").get()
//
//    private val transcribeInventoryToViewModel = Continuation<DocumentSnapshot, Unit> {
//        val inventory = it.result.toObject<ObjectCatalog>()
//        val availableItems = inventory?.foodItemList?.filter { foodItem ->
//            foodItem.isAvailable!! && foodItem.numberAvailable!! > 0
//        }
//        foodItems.value = availableItems as MutableList<FoodItem>
//    }
//
//    private val getCategoriesFromFireStore =
//        db.collection("categories").document("categories").get()
//
//    private val insertCategoriesIntoFoodListAndSort =
//        Continuation<DocumentSnapshot, Unit> { task ->
//            val categoriesListing = task.result.toObject<CategoriesListing>()
//            categories.value =
//                categoriesListing?.categories as MutableList<Category>
//            val filteredList = foodItems.value?.filter {
//                canAfford(it)
//            } as MutableList
//            foodItems.value = filteredList
//            generateHeadings()
//            foodItems.value?.sortWith(
//                compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID }
//            )
//        }
//
//    private fun navigateToSelectionFragment() {
//        Navigation.findNavController(view!!)
//            .navigate(R.id.action_secureTabletOrderStartFragment_to_secureTabletOrderSelectionFragment)
//    }
//
//    private fun generateHeadings() {
//        categories.value?.forEach { category ->
//            val heading = FoodItem(
//                0,
//                category.name,
//                category.name,
//                0,
//                0,
//                0,
//                true,
//                category.id,
//                category.calculatePoints(account.familySize)
//            )
//            foodItems.value!!.add(heading)
//        }
//    }
//
//    private fun canAfford(foodItem: FoodItem): Boolean {
//        val thisCategory = categories.value?.find {
//            it.name == foodItem.category
//        }
//        val pointsAllocated = thisCategory!!.calculatePoints(account.familySize)
//        return pointsAllocated >= foodItem.pointValue!!
//    }
//
//
//    private val retrieveSavedOrder =
//        db.collection("orders")
//            .whereEqualTo("accountID", account.accountID)
//            .whereEqualTo("orderState", "SAVED")
//            .orderBy("date", Query.Direction.DESCENDING)
//            .limit(1)
//            .get()
//
//
    fun sendAccountToFirestore(accountArg: Account, viewArg: View) {
//        view = viewArg
//        if (isValidAccount(accountArg)) account = accountArg
//        account.accountNumber = account.accountID.takeLast(4).toInt()
//        db.collection("accounts").document(account.accountID).set(account)
//            .addOnSuccessListener {
//                pleaseWait.value = false
//                toast("Account ${account.accountID} updated.")
//                returnToStart("accountNumber", accountNumber)
//            }
//            .addOnFailureListener {
//                pleaseWait.value = false
//                toast("Update failed with error $it")
//            }
    }
//
//
//    private fun returnToStart(extraName: String? = null, extraString: Int? = null) {
//
//        val activity = view!!.context as FragmentActivity
//        activity.finish()
//        val intent = Intent(activity, SecureTabletOrderActivity::class.java)
//        if (extraName != null) {
//            intent.putExtra(extraName, extraString)
//        }
//        startActivity(activity, intent, null)
//    }
//
//
//    private fun isValidAccount(accountArg: Account): Boolean {
//        var valid = false
//        when {
//            accountArg.accountID == "" -> toast("Please enter Account ID.")
//            accountArg.familySize == 0 -> toast("Please enter family size.")
//            accountArg.city == "" -> toast("Please enter city.")
//            accountArg.county == "" -> toast("Please enter county.")
//            accountArg.accountNumber == 0 -> toast("Account ID must end with four digits.")
//            else -> {
//                toast("Data validated.")
//                valid = true
//            }
//        }
//        return valid
//    }
//
//
    fun resetOrder(accountNumber: Int, viewArg: View) {
//        view = viewArg
//        retrieveAccountFromFirestore
//            .continueWith(inspectAccountAndIfValidResetToSaved)
//            .continueWith(retrieveLastOrderForThisAcount)
//            .continueWith(resetOrder)
//            .continueWith(toastOrderRestored)
    }
//
//    private val retrieveAccountFromFirestore = db.collection("accounts")
//        .whereEqualTo("accountNumber", accountNumber).get()
//
//    private val inspectAccountAndIfValidResetToSaved =
//        Continuation<QuerySnapshot, Unit> { task ->
//            if (isUniqueMatch(task.result)) {
//                account.accountID = task.result.documents[0].id
//                db.collection("accounts").document(account.accountID).update(
//                    "orderState",
//                    "SAVED"
//                )
//            }
//        }
//
//    private val retrieveLastOrderForThisAcount = Continuation<Unit, QuerySnapshot> {
//        db.collection("orders")
//            .whereEqualTo("accountID", account.accountID)
//            .orderBy("date", Query.Direction.DESCENDING)
//            .limit(1)
//            .get()
//            .result
//    }
//
//    private val resetOrder = Continuation<QuerySnapshot, Unit> { task ->
//        if (task.result.isEmpty) {
//            val orderID = task.result.documents[0].id
//            db.collection("orders").document(orderID)
//                .update("orderState", "SAVED")
//        }
//    }
//
//    private val toastOrderRestored = Continuation<Unit, Unit> { task ->
//        if (task.isSuccessful) toast("Order has been restored.")
//    }
//
//    private fun toast(string: String) {
//        Toast.makeText(view!!.context, string, Toast.LENGTH_LONG).show()
//    }
}