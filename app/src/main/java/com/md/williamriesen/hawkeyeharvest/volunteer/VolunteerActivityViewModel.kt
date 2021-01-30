package com.md.williamriesen.hawkeyeharvest.volunteer

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import com.md.williamriesen.hawkeyeharvest.foodbank.ObjectCatalog
import com.md.williamriesen.hawkeyeharvest.foodbank.Order

class VolunteerActivityViewModel : ViewModel() {

    var itemToMarkOutOfStock = MutableLiveData("")

    var itemsToPackList = MutableLiveData<MutableList<FoodItem>>()
    var todaysSubmittedOrdersList = MutableLiveData<MutableList<Order>>()
    var todaysPackedOrdersList = MutableLiveData<MutableList<Order>>()
    var nextOrder: Order? = null
    var orderID: String? = null
    var ordersToPackCount = MutableLiveData<Int>()
    var accountID: MutableLiveData<String> = MutableLiveData("")
    var accountNumberForDisplay: String? = ""
        get() = accountID.value?.takeLast(4)
    var itemCount = itemsToPackList.value?.size ?: 0
    var familySize: MutableLiveData<Long> = MutableLiveData(0)


    fun getNextOrderFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val queryCount = ordersRef.whereEqualTo("orderState", "SUBMITTED")
            .get()
            .addOnSuccessListener {
                ordersToPackCount.value = it.size()
                if (ordersToPackCount.value!! > 0) {
                }
                val query = ordersRef.whereEqualTo("orderState", "SUBMITTED")
                    .orderBy("date").limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.size() > 0) {
                            orderID = querySnapshot.documents[0].id
                            nextOrder = querySnapshot.documents[0].toObject<Order>()
                            val myList = nextOrder?.itemList
                            accountID.value = nextOrder?.accountID
                            itemsToPackList.value = myList
                        }
                    }
            }
    }

    fun getOrderFromFiresStore(orderIdArg: String) {
        orderID = orderIdArg
        Log.d("TAG", "orderIdArg at getOrderFromFireStore: $orderIdArg")
        val db = FirebaseFirestore.getInstance()
        val orderDocRef = db.collection("orders").document(orderIdArg)
        orderDocRef.get()
            .addOnSuccessListener {
                nextOrder = it.toObject<Order>()
                val myList = nextOrder?.itemList
                accountID.value = nextOrder?.accountID
                itemsToPackList.value = myList
                updateOrderAsBeingPacked()
            }
    }

    fun getFamilySize(accountID: String) {
        val db = FirebaseFirestore.getInstance()
        Log.d("TAG", "accountID argument used in getFamilySize: $accountID")
        val accountDocRef = db.collection("accounts").document(accountID)
        accountDocRef.get()
            .addOnSuccessListener {
                Log.d("TAG", "documentSnapShot: $it")
                val thisFamilySize = it.getLong("familySize")
                Log.d("TAG", "familySize within fun getFamilySize: $thisFamilySize")
                familySize.value = it.get("familySize") as Long
            }
    }

    fun updateOrderAsBeingPacked() {
        val updatedOrder = nextOrder
        updatedOrder?.orderState = "BEING_PACKED"
        val db = FirebaseFirestore.getInstance()
        Log.d("TAG", "orderID from updateOrderAsBeingPacked: $orderID")
        db.collection("orders").document(orderID!!).set(updatedOrder!!)
            .addOnSuccessListener {
                Log.d("TAG", "Order has been updated as BEING_PACKED.")
            }
    }

    fun getTodaysPackedOrdersList() {
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val startOfTodayTimestamp = Timestamp(today)
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val query = ordersRef
            .whereGreaterThan("date", startOfTodayTimestamp)
            .whereEqualTo("orderState", "PACKED")
            .get()
            .addOnSuccessListener { querySnapshot ->
//                if (!querySnapshot.isEmpty) {
                todaysPackedOrdersList.value = querySnapshot.toObjects<Order>(Order().javaClass)
                querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                    (todaysPackedOrdersList.value as MutableList<Order>)[index].orderID =
                        queryDocumentSnapshot.id
                }
//                }
            }
    }


    fun getTodaysSubmittedOrdersList() {
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val startOfTodayTimestamp = Timestamp(today)
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val query = ordersRef
//            .whereGreaterThan("date", startOfTodayTimestamp)  //For now, look at all submitted orders.
            .whereEqualTo("orderState", "SUBMITTED")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    todaysSubmittedOrdersList.value =
                        querySnapshot.toObjects<Order>(Order().javaClass)
                    querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                        (todaysSubmittedOrdersList.value as MutableList<Order>)[index].orderID =
                            queryDocumentSnapshot.id
                    }
                }
                todaysSubmittedOrdersList.value?.sortBy {
                    it.pickUpHour24
                }
            }
    }

    fun setUpSubmittedOrdersListener() {
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val startOfTodayTimestamp = Timestamp(today)
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("orders")
        val query = ordersRef
            .whereGreaterThan("date", startOfTodayTimestamp)
            .whereEqualTo("orderState", "SUBMITTED")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
//                if (!querySnapshot?.isEmpty!!) {
                if (querySnapshot != null) {
                    ordersToPackCount.value = querySnapshot.size()
                } else {
                    ordersToPackCount.value = 0
                }
                todaysSubmittedOrdersList.value =
                    querySnapshot?.toObjects<Order>(Order().javaClass)
                querySnapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                    (todaysSubmittedOrdersList.value as MutableList<Order>)[index].orderID =
                        queryDocumentSnapshot.id
                }
                todaysSubmittedOrdersList.value?.sortBy {
                    it.pickUpHour24
                }
//                }
            }
    }

    fun packOrder(orderID: String, accountID: String, view: View) {
        Log.d("TAG", "orderID argument passed to packOrder: $orderID")
        getOrderFromFiresStore(orderID)
        getFamilySize(accountID)
        Navigation.findNavController(view)
            .navigate(R.id.action_volunteerSignInFragment_to_packOrderFragment)
    }


    fun togglePackedState(itemName: String) {
        val myList = itemsToPackList.value
        val thisItem = myList?.find { item ->
            item.name == itemName
        }
        if (thisItem != null) {
            thisItem.packed = !thisItem.packed
        }
    }

    fun checkIfAllItemsPacked(): Boolean {
        itemsToPackList.value
        val itemsOrderedList = itemsToPackList.value!!.filter { item ->
            item.qtyOrdered != 0
        }
        return itemsOrderedList.all { item ->
            (item.packed)
        }
    }

    fun upDateOrderAsPacked(activity: Activity) {
        val updatedOrder = nextOrder
        updatedOrder?.orderState = "PACKED"
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document(orderID!!).set(updatedOrder!!)
            .addOnSuccessListener {
                activity.onBackPressed()
            }
    }

    fun recordNoShow(orderID: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document(orderID).update("orderState", "NO SHOW")
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "No Show has been recorded.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun markOutOfStock(context: Context) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener {documentSnapshot ->
                val workingCatalog = documentSnapshot.toObject<ObjectCatalog>()
                workingCatalog?.foodItemList?.find {
                    it.name == itemToMarkOutOfStock.value
                }
                    ?.isAvailable = false
                if (workingCatalog != null) {
                    db.collection("catalogs").document("objectCatalog").set(workingCatalog)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Inventory has been updated.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener {
                            Log.d("TAG", "Mark out of stock failed with exception: $it")
                        }
                }
            }
    }


}
