package com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite

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
import java.util.*

class OnSiteOrderingViewModel : ViewModel() {

    var accountID = ""
    val itemList = MutableLiveData<MutableList<Item>>()
    var orderID: String? = null
    var isOpen = MutableLiveData<Boolean>(false)
    var familySize = 0
    var orderState: MutableLiveData<String> = MutableLiveData("NONE")
    var lastOrderDate: Date? = null
    val categoriesList = MutableLiveData<MutableList<Category>>()
    private var savedItemList = mutableListOf<Item>()
    var points: Int? = null
    val outOfStockNameList: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf<String>())


    private val needToStartNewOrder: Boolean
        get() = orderState.value == "PACKED" && (!isOpen.value!! || whenOrdered != "TODAY")

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
        get() = accountID.takeLast(4)

    val nextFragment: Int
        get() {
            return when (orderState.value) {
                "SAVED" -> R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
                "SUBMITTED" -> {
                        R.id.action_onSiteOrderStartFragment_to_onSiteOrderBeingPackedFragment
                }
                "PACKED" -> {
                    when (whenOrdered) {
                        "TODAY" -> R.id.action_onSiteOrderStartFragment_to_onSiteOrderReadyFragment
                        "EARLIER_THIS_MONTH" -> R.id.action_onSiteOrderStartFragment_to_onSiteOrderReadyFragment
                        else -> R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
                    }
                }
                "NO SHOW" -> {
                    R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
                }
                else -> R.id.action_onSiteOrderStartFragment_to_onSiteInstructionsFragment
            }
        }

    fun submitOnSiteOrder(view: View) {
        val thisOrder = Order(accountID, Date(), itemList.value!!, "SUBMITTED")
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
                Log.d("TAG","orderID: $orderID")
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
}