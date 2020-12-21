package com.md.williamriesen.hawkeyeharvest.manager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.foodbank.*

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
        val foodItemListWithoutHeadings = itemsToInventoryList.value?.filter { foodItem ->
            foodItem.name != foodItem.category
        }
        val newObjectCatalog = ObjectCatalog()
        newObjectCatalog.foodItemList = foodItemListWithoutHeadings
        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(newObjectCatalog)
            .addOnSuccessListener {
                Toast.makeText(context, "Inventory Updated.", Toast.LENGTH_LONG).show()
            }
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

    fun submitAccount(accountID: String, familySize: String, city: String, county: String, context: Context) {

        if (isValidAccount(accountID,familySize,city,county, context)){
            val account = Account(accountID,familySize.toInt(),city,county)
            val db = FirebaseFirestore.getInstance()
            db.collection("accounts").document(account.accountID).set(account)
        }
    }

    fun isValidAccount(accountID: String, familySize: String, city: String, county: String, context: Context): Boolean {
        var valid = false
        when {
            accountID == "" -> {
                Toast.makeText(context, "Please enter Account ID.", Toast.LENGTH_LONG).show()
            }
            familySize == "" -> {
                Toast.makeText(context, "Please enter family size.", Toast.LENGTH_LONG).show()
            }
            city == "" -> {
                Toast.makeText(context, "Please enter city.", Toast.LENGTH_LONG).show()
            }
            county == "" -> {
                Toast.makeText(context, "Please enter county.", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "Data validated.", Toast.LENGTH_LONG).show()
                valid = true
            }
        }
        return valid
    }


    fun submitNewFoodItem(name: String, category: String, pointValue: String, limit: String, context: Context) {
        if (isValidFoodItem(name,category,pointValue,limit,context)){
            TODO() // need to determine next unused foodItem number,
            // need to determine category number from category name
            // then create new foodItem and submit to firestore
            //(without messing up the rest of the catalog
        }
    }

    fun isValidFoodItem(name: String, category: String,pointValue: String,limit: String, context: Context): Boolean {
        var valid = false
        when {
            name == "" -> {
                Toast.makeText(context, "Please enter name of item to add.", Toast.LENGTH_LONG).show()
            }
            category == "" -> {
                Toast.makeText(context, "Please select category.", Toast.LENGTH_LONG).show()
            }
            pointValue == "" -> {
                Toast.makeText(context, "Please enter point value (usually 1).", Toast.LENGTH_LONG).show()
            }
            limit == "" -> {
                Toast.makeText(context, "Please enter limit (use 100 if no limit).", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "Data validated; update not yet implemented.", Toast.LENGTH_LONG).show()
                valid = true
            }
        }
        return valid
    }
}
