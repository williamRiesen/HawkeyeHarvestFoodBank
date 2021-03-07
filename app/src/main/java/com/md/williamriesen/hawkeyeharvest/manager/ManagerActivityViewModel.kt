package com.md.williamriesen.hawkeyeharvest.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.*
import com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet.SecureTabletOrderActivity

class ManagerActivityViewModel : ViewModel() {

    var searchString = ""
    var showNewItemButton: MutableLiveData<Boolean> = MutableLiveData(false)
    var pleaseWait = MutableLiveData<Boolean>(false)
    var itemsToInventory = MutableLiveData<MutableList<FoodItem>>(mutableListOf())
//    var filteredInventoryList = MutableLiveData<MutableList<FoodItem>>(mutableListOf())
    var preliminaryItemList = mutableListOf<FoodItem>()
    lateinit var categoriesList: MutableList<Category>

    fun updateNumberAvailable(itemName: String, numberAvailable: Editable?, context: Context) {
        val myList = itemsToInventory.value
        val thisItem = myList?.find { foodItem ->
            foodItem.name == itemName
        }
        if (thisItem != null) {
            thisItem.numberAvailable = numberAvailable.toString().toInt()
            updateFoodItem(thisItem, context)
//            submitUpdatedInventory(context)
        }
    }

    fun updateSpecial(itemID: Int, special: Boolean, context: Context){
        val thisItem = itemsToInventory.value?.find { foodItem ->
            foodItem.itemID == itemID
        }
        if (thisItem != null) {
            thisItem.special = special
            updateFoodItem(thisItem, context)
        }
    }


    fun toggleIsAvailableStatus(itemName: String, context: Context) {
        val myList = itemsToInventory.value
        val thisItem = myList?.find { item ->
            item.name == itemName
        }
        if (thisItem != null) {
            thisItem.isAvailable = !thisItem.isAvailable!!
//            submitUpdatedInventory(context)
            updateFoodItem(thisItem, context)
        }
    }


    fun updateFoodItem(updatedFoodItem: FoodItem, context: Context) {
        //retrieve latest catalog (may have been updated by others since last read)
        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val catalog = documentSnapshot.toObject(ObjectCatalog::class.java)

                //replace old with new foodItem within the local catalog
                catalog?.foodItemList!!.removeIf {
                    it.itemID == updatedFoodItem.itemID
                }
                catalog.foodItemList!!.add(updatedFoodItem)
                db.collection("catalogs").document("objectCatalog").set(catalog)
                    .addOnSuccessListener {
                        Toast.makeText(context,"Inventory Updated.",Toast.LENGTH_LONG).show()
                    }
            }
    }

    private fun getNextFoodItemNumber(): Int {
        val max = itemsToInventory.value?.maxBy { foodItem ->
            foodItem.itemID!!
        }
        return max!!.itemID!! + 1
    }

    private fun getCategoryId(categoryName: String): Int {
        val thisCategory = categoriesList.find { category ->
            category.name == categoryName
        }
        return thisCategory!!.id
    }

    private fun getInventoryFromFirestore(fragment: UpdateInventoryFragment) {
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
                itemsToInventory.value = preliminaryItemList
//                filteredInventoryList = itemsToInventoryList


                val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progressBar2)
                progressBar?.visibility = View.INVISIBLE
                val buttonAddFoodItem =
                    fragment.view?.findViewById<FloatingActionButton>(R.id.floatingActionButtonAddItem)
//                buttonAddFoodItem?.visibility = View.VISIBLE
            }
    }

    fun submitUpdatedInventory(context: Context) {
        val foodItemListWithoutHeadings = itemsToInventory.value?.filter { foodItem ->
            foodItem.name != foodItem.category
        }
        val newObjectCatalog = ObjectCatalog()
        newObjectCatalog.foodItemList = foodItemListWithoutHeadings as MutableList<FoodItem>?
        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(newObjectCatalog)
            .addOnSuccessListener {
                Toast.makeText(context, "Inventory Updated.", Toast.LENGTH_LONG).show()
            }
    }

    fun retrieveCategoriesFromFireStore(fragment: UpdateInventoryFragment) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("categories").document("categories")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesList = categoriesListing?.categories as MutableList<Category>
                generateHeadings(categoriesList)
                getInventoryFromFirestore(fragment)
            }
    }

    private fun generateHeadings(categoriesList: MutableList<Category>) {
        preliminaryItemList.clear()
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
            Log.d("TAG", "itemsToInventory.value: ${itemsToInventory.value}")
            preliminaryItemList.add(heading)
//            itemsToInventoryList.value!!.add(heading)
        }
    }

    fun submitAccount(
        accountID: String,
        familySize: String,
        city: String,
        county: String,
        context: Context,
        activity: FragmentActivity
    ) {
        if (isValidAccount(accountID, familySize, city, county, context)) {
            val accountNumber = accountID.takeLast(4).toIntOrNull()
            val account = Account(accountID, familySize.toInt(), city, county, accountNumber)
            val db = FirebaseFirestore.getInstance()
            db.collection("accounts").document(account.accountID).set(account)
                .addOnSuccessListener {
                    pleaseWait.value = false
                    activity.finish()
                    val intent = Intent(activity, SecureTabletOrderActivity::class.java)
                    intent.putExtra("accountNumber", accountNumber);
                    ContextCompat.startActivity(activity, intent, null)
                    Toast.makeText(context, "Account $accountID updated.", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Update failed with error $it", Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    fun isValidAccount(
        accountID: String,
        familySize: String,
        city: String,
        county: String,
        context: Context
    ): Boolean {
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


    fun submitNewFoodItem(
        name: String,
        category: String,
        pointValue: String,
        limit: String,
        context: Context
    ) {
        if (isValidFoodItem(name, category, pointValue, limit, context)) {
            val parentheticalPhrase = if (pointValue != "1" && limit != "100") {
                " ($pointValue pts, Limit $limit)"
            } else if (pointValue != "1") {
                " ($pointValue pts)"
            } else if (limit != "100") {
                " (Limit $limit)"
            } else {
                ""
            }


            val foodItem = FoodItem(
                getNextFoodItemNumber(),
                name + parentheticalPhrase,
                category,
                pointValue.toInt(),
                limit.toInt(),
                100,
                true,
                getCategoryId(category)
            )
            itemsToInventory.value!!.add(foodItem)
            itemsToInventory.value!!.sortWith(
                compareBy<FoodItem> { it.categoryId }.thenBy { it.itemID }
            )
            submitUpdatedInventory(context)
            val activity = context as Activity
            activity.onBackPressed()
        }
    }

    fun isValidFoodItem(
        name: String,
        category: String,
        pointValue: String,
        limit: String,
        context: Context
    ): Boolean {
        var valid = false
        when {
            name == "" -> {
                Toast.makeText(context, "Please enter name of item to add.", Toast.LENGTH_LONG)
                    .show()
            }
            category == "" -> {
                Toast.makeText(context, "Please select category.", Toast.LENGTH_LONG).show()
            }
            pointValue == "" -> {
                Toast.makeText(
                    context,
                    "Please enter point value (usually 1).",
                    Toast.LENGTH_LONG
                ).show()
            }
            limit == "" -> {
                Toast.makeText(
                    context,
                    "Please enter limit (use 100 if no limit).",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    context,
                    "Data validated.",
                    Toast.LENGTH_LONG
                ).show()
                valid = true
            }
        }
        return valid
    }

    fun sendReport(view: View) {
        val db = FirebaseFirestore.getInstance()
        val data = mapOf(Pair("command", "send"))
        db.collection("triggers").document("sendReport")
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(
                    view.context,
                    "Report request sent!",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

}
