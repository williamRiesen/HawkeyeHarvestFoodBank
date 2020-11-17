package com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvestfoodbank.*
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.*
import java.text.SimpleDateFormat
import java.util.*

class NextDayOrderingActivityViewModel : ViewModel() {

    var accountID = ""
    var lastOrderDate: Date? = null
    var orderState = "NOT STARTED YET"
    var familySize = 0
    var points: Int? = null
    val itemList = MutableLiveData<MutableList<FoodItem>>()
    val categoriesList = MutableLiveData<MutableList<Category>>()

    var pickUpHour24 = 0
    val foodBank = FoodBank()
    val simpleDateFormat = SimpleDateFormat("E, MMM d")

    val nextDayOpen : String?
        get() = simpleDateFormat.format(foodBank.nextDayOpen())

    val nextDayTakingOrders: String
    get() = simpleDateFormat.format(foodBank.nextDayTakingOrders())

    val nextPickUpDay: String
    get() = simpleDateFormat.format(foodBank.nextDayOpen(afterTomorrow = true))

    val nextPreOrderDay : String
    get() = simpleDateFormat.format(foodBank.nextDayTakingOrders(afterToday = true))

    val returnOnMessage: String
    get() = "Please return to this app on $nextDayTakingOrders before 5 PM, to place your order for pick up on $nextDayOpen"

    val takingOrders : Boolean
    get() = FoodBank().isTakingNextDayOrders

    val accountNumberForDisplay: String
        get() = accountID.takeLast(4)



    fun goToNextFragment(pickUpHour24Arg: Int, view: View){
        pickUpHour24 = pickUpHour24Arg
        if (pickUpHour24Arg == 0){
            Navigation.findNavController(view).navigate(R.id.action_selectPickUpTimeFragment_to_returnAnotherDayFragment)

        } else {
            Navigation.findNavController(view).navigate(R.id.action_selectPickUpTimeFragment_to_selectionFragment2)
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
                Log.d("TAG","Data retrieval done.")
                itemList.value!!.forEach { item->
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
            itemList.value!!.add(heading)
        }
    }
    fun canAfford(foodItem: FoodItem): Boolean {
        val thisCategory = categoriesList.value?.find {
            it.name == foodItem.category
        }
        val pointsAllocated = thisCategory!!.calculatePoints(familySize)
        return pointsAllocated >= foodItem.pointValue!!
    }

}