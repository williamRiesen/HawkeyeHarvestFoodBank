package com.md.williamriesen.hawkeyeharvest.manager

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.foodbank.ObjectCatalog
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem

class ManagerActivityViewModel : ViewModel() {

    var itemsToInventoryList = MutableLiveData<MutableList<FoodItem>>()


    fun retrieveObjectCatalogFromFireStore(view: View) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                itemsToInventoryList.value = myObjectCatalog?.foodItemList as MutableList<FoodItem>?
//                retrieveCategoriesFromFireStore(view)
            }
    }

    fun toggleIsAvailableStatus(itemName: String){
        val myList = itemsToInventoryList.value
        val thisItem = myList?.find { item ->
            item.name == itemName
        }
        if (thisItem != null) {
            thisItem.isAvailable= !thisItem.isAvailable!!
        }
    }

    fun getInventoryFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                itemsToInventoryList.value = myObjectCatalog?.foodItemList as MutableList<FoodItem>?
//                retrieveCategoriesFromFireStore(view)
            }
    }

    fun submitUpdatedInventory(context: Context) {
        val objectCatalog = ObjectCatalog(itemsToInventoryList.value!!)
        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(objectCatalog)
            .addOnSuccessListener {
                Toast.makeText(context,"Inventory Updated.", Toast.LENGTH_LONG).show()
            }
    }
}
