package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.*
import java.util.*
import kotlin.Exception


class SecureTabletOrderViewModel : ViewModel() {

    var account = Account("", 0, "", "", 0)
    var accountNumber: Int? = null
    val categories = MutableLiveData<MutableList<Category>>()
    val db = FirebaseFirestore.getInstance()
    val foodItems = MutableLiveData<MutableList<FoodItem>>(mutableListOf())
    var order = Order("", Date(), mutableListOf(FoodItem()), "")
    private var orderID: String? = null

    //    var orderState: MutableLiveData<OrderState> = MutableLiveData(OrderState.NOT_STARTED_YET)
    val outOfStockItems: MutableLiveData<MutableList<OutOfStockItem>> =
        MutableLiveData<MutableList<OutOfStockItem>>(mutableListOf<OutOfStockItem>())
    var pleaseWait = MutableLiveData<Boolean>(false)
    var points: Int? = null
    var startupAccountNumber: Int? = null
    var view: View? = null


    fun saveOrder(fragment: Fragment) {
        view = fragment.view
        assembleOrderAs("SAVED")
        if (orderID == null) {
            val newOrderDocumentRef = db.collection("orders").document()
            orderID = newOrderDocumentRef.id
            newOrderDocumentRef.set(order)
        } else {
            db.collection("orders").document(orderID!!).set(order)
        }
            .addOnSuccessListener {
                toast("Order saved.")
            }
            .addOnFailureListener {
                toast("Save failed with exception: $it")
            }
    }


    fun processOrder(viewArg: View) {
        view = viewArg
        retrieveInventory()
            .continueWith { task ->
                val retrievedInventory = task.result.toObject(ObjectCatalog::class.java)
                val retrievedFoodItems = retrievedInventory?.foodItemList
                Log.d("TAG", "retrievedInventory: $retrievedInventory")
                if (retrievedFoodItems != null) {
                    updateFoodItemListUsing(retrievedFoodItems)
                }
                Log.d("TAG", "FoodItems updated.")
                separateOutOfStockItems()
                if (outOfStockItems.value!!.isEmpty()) {
                    assembleOrderAs("SUBMITTED")
                    submit(order)
                } else {
                    askClientForAlternativeChoices()
                }
            }
            .addOnSuccessListener {
//                toast("Out of stock items separated from main list.")
            }
            .addOnFailureListener {
                toast("Inventory retrieval failed with: $it")
            }
    }


    private fun retrieveInventory(): Task<DocumentSnapshot> {
        return db.collection("catalogs").document("objectCatalog").get()
    }

    private fun updateFoodItemListUsing(retrievedList: MutableList<FoodItem>) {
        foodItems.value!!.forEach { foodListItem ->
            if (foodListItem.isFoundIn(retrievedList)) {
                foodListItem.updateUsing(retrievedList)
            } else {
                foodListItem.isAvailable = false
            }
        }
    }

    private fun updateFoodItemListUsing(order: Order?) {

        order?.itemList?.forEach { orderedFoodItem ->
            Log.d("TAG", "orderedFoodItem.name ${orderedFoodItem.name}")
            val foundInFoodList = foodItems.value?.find { viewModelFoodItem ->
                viewModelFoodItem.name == orderedFoodItem.name
            }
            if (foundInFoodList != null) {
                Log.d("TAG", "In stock.")
                foundInFoodList!!.qtyOrdered = orderedFoodItem.qtyOrdered
            } else {
                Log.d("TAG", "Out of stock.")
                outOfStockItems.value?.add(
                    OutOfStockItem(orderedFoodItem.name!!, orderedFoodItem.qtyOrdered)
                )
                order.itemList.remove(orderedFoodItem)
            }
        }
    }

    private fun separateOutOfStockItems() {
        outOfStockItems.value?.clear()
        foodItems.value?.forEach {
            if (it.qtyOrdered > 0 && !it.isAvailable!!) {
                outOfStockItems.value?.add(OutOfStockItem(it.name!!, it.qtyOrdered))
                refundPointsFor(it)
                it.qtyOrdered = 0
            }
        }
    }

    private fun assembleOrderAs(orderState: String) {
        Log.d("TAG", "account.accountID in assembleOrder: ${account.accountID}")
        order = Order(account.accountID, Date(), foodItems.value!!, orderState).filterOutZeros()
    }

    private fun submit(orderArg: Order) {
        order = orderArg
        FirebaseInstanceId.getInstance().instanceId
            .continueWith(getToken)
            .continueWithTask {
                db.collection("orders").document().set(order)
            }
            .continueWith { returnToStart() }

    }

//    private val sendOrderToFirestore =
//        if (order.orderID != null) {
//            db.collection("orders").document(order.orderID!!)
//        } else {
//            db.collection("orders").document()
//        }.set(order)
//            .addOnSuccessListener { Log.d("TAG","Set completed successfully.") }
//            .addOnFailureListener { Log.d("TAG","Set failed: $it") }

    private val getToken = Continuation<InstanceIdResult, Unit> {
        order.deviceToken = it.result.token
        it.result
    }


    private fun refundPointsFor(foodItem: FoodItem) {
        val categoryOfItem = foodItems.value?.find {
            it.name == foodItem.name
        }?.category
        categories.value?.find { category ->
            categoryOfItem == category.name
        }!!.pointsUsed -= foodItem.qtyOrdered
    }

    private fun askClientForAlternativeChoices() {
        Navigation.findNavController(view!!)
            .navigate(R.id.action_secureTabletOrderStartFragment_to_outOfStockFragment3)
    }


    fun go(accountNumber: Int, viewArg: View) {
        view = viewArg
        lookUpAccount(accountNumber)
            .continueWith(validateAccount)
            .continueWith { isValidAccount ->
                if (isValidAccount.result) {
                    Log.d("TAG", "account.lastOrderDate: ${account.lastOrderDate}")
                    if (orderedAlready()) {
                        Log.d("TAG", "ordered already.")
                        navigateToAlreadyOrderedMessage()
                    } else {
                        Log.d("TAG", "not ordered already.")
                        prepareSelections()
                    }
                }
            }.addOnSuccessListener { toast("Go routine completed successfully.") }
            .addOnFailureListener { toast("Go routine failed: $it") }
    }


    private fun lookUpAccount(accountNumber: Int) =
        db.collection("accounts").whereEqualTo("accountNumber", accountNumber).get()

    private val validateAccount = Continuation<QuerySnapshot, Boolean> { task ->
        val accountFetchResult = task.result
        Log.d("TAG", "isUniqueMatch: ${isUniqueMatch(accountFetchResult)}")
        if (isUniqueMatch(accountFetchResult)) {

            val retrievedAccount = task.result.documents[0].toObject(Account::class.java)
            retrievedAccount!!.accountID = task.result.documents[0].id
            account = retrievedAccount

            true
        } else {
            false
        }
    }

    private fun isUniqueMatch(querySnapshot: QuerySnapshot): Boolean {
        return when (querySnapshot.size()) {
            0 -> {
                toast("No match found for this number.")
                false
            }
            1 -> true

            else -> {
                toast("Multiple matches: this should not be. Please contact Dr. Riesen.")
                false
            }
        }
    }

    private fun orderedAlready(): Boolean {
        val calendar = Calendar.getInstance()
        val thisMonth = calendar[Calendar.MONTH]
        val thisYear = calendar[Calendar.YEAR]
        val startOfMonth = FoodBank().makeDate(thisMonth, 1, thisYear)
        Log.d("TAG", "lastOrderDate: ${account.lastOrderDate}")
        Log.d("TAG", "startOfMonth: $startOfMonth")
        return (account.lastOrderDate > startOfMonth
                && account.orderState != "SAVED")
    }


    private fun navigateToAlreadyOrderedMessage() {
        Navigation.findNavController(view!!)
            .navigate(R.id.action_secureTabletOrderStartFragment_to_alreadyOrderedMessageFragment)
    }


    private fun prepareSelections() {
        Log.d("TAG", "account.accountID: ${account.accountID}")
        getInventoryFromFirestore
            .continueWith(transcribeInventoryToViewModel)
            .continueWithTask { getCategoriesFromFireStore }
            .continueWith(insertCategoriesIntoFoodListAndSort)
            .continueWithTask {
                db.collection("orders")
                    .whereEqualTo("accountID", account.accountID)
                    .whereEqualTo("orderState", "SAVED")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
            }
            .continueWith {
                val retrievedOrder =
                    if (it.result.isEmpty) null
                        else it.result.documents[0].toObject<Order>()
                updateFoodItemListUsing(retrievedOrder)
                if (outOfStockItems.value.isNullOrEmpty()) {
                    navigateToSelectionFragment()
                } else
                    askClientForAlternativeChoices()
            }
//            .continueWith {someItemsAreOutOfStock ->
//                Log.d("TAG", "Before...")
//                if (someItemsAreOutOfStock.result == null) {
//                    Log.d("TAG", "it is null.")
//                } else {
//                    Log.d("TAG", "it is not null.")
//                }
//                Log.d("TAG", "someItemsAreOutOfStock.result: ${someItemsAreOutOfStock.result}")
//                Log.d("TAG", "... After: someItemsAreOutOfStock: $someItemsAreOutOfStock")
//                if (someItemsAreOutOfStock.result) {
//                    askClientForAlternativeChoices()
//                } else {
//                    navigateToSelectionFragment()
//                }
//            }
    }

    private val unpackAndCheckSavedOrder = Continuation<QuerySnapshot, Boolean> { task ->
        var someItemsAreOutOfStock = false
        Log.d("TAG", "retrievedOrderQuery.isComplete: ${task.isComplete}")
        Log.d("TAG", "retrievedOrderQuery.size: ${task.result.size()}")
        Log.d("TAG", "retrieved orderID: ${task.result.documents[0].id}")
        if (task.result.size() > 0) {
            Log.d("TAG", "Starting if branch.")
            orderID = task.result.documents[0].id
            Log.d("TAG", "orderID assigned.")
            val order = task.result.documents[0].toObject<Order>()
            Log.d("TAG", "toObject method completed.")
            Log.d("TAG", "order: ${order?.orderID}")
            Log.d("TAG", "order: ${order?.accountID}")
            Log.d("TAG", "order: ${order?.orderState}")
            Log.d("TAG", "order: ${order?.date}")
            Log.d("TAG", "order: ${order?.itemList}")
            updateFoodItemListUsing(order!!)
            Log.d("TAG", "updateFoodItemListUsing completed.")
//            separateOutOfStockItems()
//            Log.d("TAG", "separateOutOfStockItems completed.")
            someItemsAreOutOfStock = !outOfStockItems.value.isNullOrEmpty()
            Log.d("TAG", "someItemsAreOutOfStock(inside): $someItemsAreOutOfStock")
        }
        Log.d("TAG", "someItemsAreOutOfStock: $someItemsAreOutOfStock")
        someItemsAreOutOfStock
    }


    private val getInventoryFromFirestore =
        db.collection("catalogs").document("objectCatalog").get()

    private val transcribeInventoryToViewModel = Continuation<DocumentSnapshot, Unit> {
        val inventory = it.result.toObject<ObjectCatalog>()
        val availableItems = inventory?.foodItemList?.filter { foodItem ->
            foodItem.isAvailable!! && foodItem.numberAvailable!! > 0
        }
        foodItems.value = availableItems as MutableList<FoodItem>
    }

    private val getCategoriesFromFireStore =
        db.collection("categories").document("categories").get()

    private val insertCategoriesIntoFoodListAndSort =
        Continuation<DocumentSnapshot, Unit> { task ->
            val categoriesListing = task.result.toObject<CategoriesListing>()
            categories.value =
                categoriesListing?.categories as MutableList<Category>
            val filteredList = foodItems.value?.filter {
                canAfford(it)
            } as MutableList
            foodItems.value = filteredList
            generateHeadings()
            foodItems.value?.sortWith(
                compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID }
            )
        }

    private fun navigateToSelectionFragment() {
        Navigation.findNavController(view!!)
            .navigate(R.id.action_secureTabletOrderStartFragment_to_secureTabletOrderSelectionFragment)
    }

    private fun navigateToBlankFragment() {
        Navigation.findNavController(view!!)
            .navigate(R.id.action_secureTabletOrderStartFragment_to_blankFragment)
    }

    private fun generateHeadings() {
        categories.value?.forEach { category ->
            val heading = FoodItem(
                0,
                category.name,
                category.name,
                0,
                0,
                0,
                true,
                category.id,
                category.calculatePoints(account.familySize)
            )
            foodItems.value!!.add(heading)
        }
    }

    private fun canAfford(foodItem: FoodItem): Boolean {
        val thisCategory = categories.value?.find {
            it.name == foodItem.category
        }
        val pointsAllocated = thisCategory!!.calculatePoints(account.familySize)
        return pointsAllocated >= foodItem.pointValue!!
    }


    private val retrieveSavedOrder = {
        Log.d("TAG", "account.accountID: ${account.accountID}")
        db.collection("orders")
            .whereEqualTo("accountID", "QQC0064")
            .whereEqualTo("orderState", "SAVED")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(1)
            .get()
    }

    fun sendAccountToFirestore(accountArg: Account, viewArg: View) {
        view = viewArg
        if (isValidAccount(accountArg)) account = accountArg
        account.accountNumber = account.accountID.takeLast(4).toInt()
        db.collection("accounts").document(account.accountID).set(account)
            .addOnSuccessListener {
                pleaseWait.value = false
                toast("Account ${account.accountID} updated.")
                returnToStart("accountNumber", accountNumber)
            }
            .addOnFailureListener {
                pleaseWait.value = false
                toast("Update failed with error $it")
            }
    }


    private fun returnToStart(extraName: String? = null, extraString: Int? = null) {

        val activity = view!!.context as FragmentActivity
        activity.finish()
        val intent = Intent(activity, SecureTabletOrderActivity::class.java)
        if (extraName != null) {
            intent.putExtra(extraName, extraString)
        }
        startActivity(activity, intent, null)
    }


    private fun isValidAccount(accountArg: Account): Boolean {
        var valid = false
        when {
            accountArg.accountID == "" -> toast("Please enter Account ID.")
            accountArg.familySize == 0 -> toast("Please enter family size.")
            accountArg.city == "" -> toast("Please enter city.")
            accountArg.county == "" -> toast("Please enter county.")
            accountArg.accountNumber == 0 -> toast("Account ID must end with four digits.")
            else -> {
                toast("Data validated.")
                valid = true
            }
        }
        return valid
    }


    fun resetOrder(accountNumber: Int, viewArg: View) {
        view = viewArg
        db.collection("accounts")
            .whereEqualTo("accountNumber", accountNumber).get()
            .continueWith {
                val retrievedAccountID =
                    if (it.result.isEmpty) "No account found with this number."
                    else it.result.documents[0].id
                retrievedAccountID
            }
            .continueWith {
                db.collection("accounts").document(it.result)
                    .update("orderState", "SAVED")
                it.result
            }
            .continueWithTask {
                db.collection("orders")
                    .whereEqualTo("accountID", it.result)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
            }.continueWith {
                if (it.result.isEmpty) throw Exception("No order found for this number.")
                else {
                    db.collection("orders")
                        .document(it.result.documents[0].id)
                        .update("orderState", "SAVED")
                }
            }
            .addOnSuccessListener {
                toast("Order has been restored.")
            }
            .addOnFailureListener {
                toast("Reset failed: $it")
            }
    }

    private fun toast(string: String) {
        Toast.makeText(view!!.context, string, Toast.LENGTH_LONG).show()
    }

    fun breadAndSweets(accountNumber: Int, viewArg: View) {
        view = viewArg
        lookUpAccount(accountNumber)
            .continueWith(validateAccount)
            .continueWith { isValidAccount ->
                if (isValidAccount.result) {
                    val breadAndSweets = mapOf(
                        "accountID" to account.accountID,
                        "date" to Date()
                    )
                    db.collection("breadAndSweets").document()
                        .set(breadAndSweets)
                        .addOnSuccessListener {
                            toast("Bread and sweets pickup recorded.")
                        }
                        .addOnFailureListener {
                            toast("Write failed: $it")
                        }
                } else {
                    toast("Invalid account.")
                }
            }
            .addOnFailureListener {
                toast("Unable to retrieve account: $it")
            }
    }
}


