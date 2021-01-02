package com.md.williamriesen.hawkeyeharvest.signin

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.md.williamriesen.hawkeyeharvest.foodbank.CategoriesListing
import com.md.williamriesen.hawkeyeharvest.foodbank.Category
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import com.md.williamriesen.hawkeyeharvest.foodbank.ObjectCatalog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogService @Inject constructor(private val db: FirebaseFirestore) {

    fun fetchCatalog(): Task<List<FoodItem>> {
        val docRef = db.collection("catalogs").document("objectCatalog")
        return docRef
            .get()
            .continueWith {
                val documentSnapshot: DocumentSnapshot = it.result!!
                val myObjectCatalog = documentSnapshot.toObject<ObjectCatalog>()
                if (myObjectCatalog != null) {
                    Log.d("TAG", "myObjectCatalog.foodItemList: ${myObjectCatalog.foodItemList}")
                }
                val availableItemsList = myObjectCatalog?.foodItemList?.filter { foodItem ->
                    foodItem.isAvailable as Boolean

                }

                availableItemsList
            }
    }

    fun fetchCategories(): Task<List<Category>> {
        val docRef = db.collection("categories").document("categories")
        return docRef.get()
            .continueWith {
                val documentSnapshot: DocumentSnapshot = it.result!!
                val categoriesListing = documentSnapshot.toObject<CategoriesListing>()
                categoriesListing?.categories
            }
    }
}