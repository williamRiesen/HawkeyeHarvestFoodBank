package com.md.williamriesen.hawkeyeharvest.manager

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.foodbank.CategoriesListing
import com.md.williamriesen.hawkeyeharvest.foodbank.Category
import com.md.williamriesen.hawkeyeharvest.foodbank.ObjectCatalog
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem

class ManagerActivityViewModel : ViewModel() {

    var itemsToInventoryList = MutableLiveData<MutableList<FoodItem>>()
    var preliminaryItemList = mutableListOf<FoodItem>()

    fun toggleIsAvailableStatus(itemName: String) {
        val myList = itemsToInventoryList.value
        val thisItem = myList?.find { item ->
            item.name == itemName
        }
        if (thisItem != null) {
            thisItem.isAvailable = !thisItem.isAvailable!!
        }
    }

    fun getInventoryFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                myObjectCatalog?.foodItemList?.forEach { foodItem ->
                    preliminaryItemList.add(foodItem)
//                    itemsToInventoryList.value?.add(foodItem)
                }
                preliminaryItemList.sortWith(
                    compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID }
                )
                itemsToInventoryList.value = preliminaryItemList
            }
    }

    fun submitUpdatedInventory(context: Context) {
//        val objectCatalog = ObjectCatalog(itemsToInventoryList.value!!)
//        val db = FirebaseFirestore.getInstance()
//        db.collection("catalogs").document("objectCatalog").set(objectCatalog)
//            .addOnSuccessListener {
//                Toast.makeText(context, "Inventory Updated.", Toast.LENGTH_LONG).show()
//            }
    }

    fun retrieveCategoriesFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("categories").document("categories")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                val categoriesList = categoriesListing?.categories as MutableList<Category>
                generateHeadings(categoriesList)

                getInventoryFromFirestore()
            }
    }

    private fun generateHeadings(categoriesList: MutableList<Category>) {
        categoriesList?.forEach { category ->
            val heading = FoodItem(
                0,
                category.name,
                category.name,
                0,
                0,
                0,
                true,
                category.id,
                0
            )
            Log.d("TAG", "heading: $heading")
            Log.d("TAG", "itemsToInventory.value: ${itemsToInventoryList.value}")
            preliminaryItemList.add(heading)
//            itemsToInventoryList.value!!.add(heading)
        }
    }

}
