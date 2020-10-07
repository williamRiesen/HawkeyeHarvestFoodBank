package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ManagerActivityViewModel : ViewModel() {

    var itemsToInventoryList = MutableLiveData<MutableList<Item>>()


    fun retrieveObjectCatalogFromFireStore(view: View) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("catalogs").document("objectCatalog")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                itemsToInventoryList.value = myObjectCatalog?.itemList as MutableList<Item>?
//                retrieveCategoriesFromFireStore(view)
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
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
                itemsToInventoryList.value = myObjectCatalog?.itemList as MutableList<Item>?
//                retrieveCategoriesFromFireStore(view)
            }
            .addOnFailureListener {
                Log.d("TAG", "Retrieve objectCatalog from database failed.")
            }
    }






}
