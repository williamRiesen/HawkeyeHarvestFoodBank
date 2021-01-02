package com.md.williamriesen.hawkeyeharvest.signin

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.messaging.FirebaseMessaging
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvest.foodbank.Order
import javax.inject.Inject

class OrderService @Inject constructor(
    private val db: FirebaseFirestore,
    private val messaging: FirebaseMessaging
) {

    fun saveOrder(order: Order): Task<Order> {
        val filteredOrder = order.filterOutZeros()

        // Select document by id if present, otherwise just create a new document
        val document = if (order.orderID != null) {
            db.collection(("orders")).document(order.orderID!!)
        } else {
            db.collection(("orders")).document()
        }

        // Set fetched id from db. Return the original order
        return document.set(filteredOrder)
            .continueWith {
                order.accountID = document.id
                order
            }
    }


    fun submitOnSiteOrder(order: Order): Task<Void> {
        val filteredOrder = order.filterOutZeros()

        // Set the device token to indicate where to post messages back to.
        return messaging.token.continueWithTask { deviceToken ->
            filteredOrder.deviceToken = deviceToken.result

            // Select document by id if present, otherwise just create a new document
            val document = if (order.orderID != null) {
                db.collection(("orders")).document(order.orderID!!)
            } else {
                db.collection(("orders")).document()
            }

            // Submit order
            document.set(filteredOrder)
        }
    }

    fun listenForOrders(eventListener: (Int, List<Order>) -> Unit) {

        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        val startOfTodayTimestamp = Timestamp(today)

        db.collection("orders")
            .whereGreaterThan("date", startOfTodayTimestamp)
            .whereEqualTo("orderState", "SUBMITTED")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (!querySnapshot?.isEmpty!!) {
                    val size = querySnapshot.size()
                    val orderList = querySnapshot.toObjects<Order>()
                    eventListener(size, orderList)
                } else {
                    eventListener(0, emptyList())
                }
            }
    }

    fun getNextOrder(): Task<Order?> {
        return db.collection("orders")
            .whereEqualTo("orderState", "SUBMITTED")
            .orderBy("date")
            .limit(1)
            .get()
            .continueWith {
                val querySnapshot: QuerySnapshot = it.result!!
                if (querySnapshot.isEmpty) {
                    null
                } else {
                    val nextOrder: Order = querySnapshot.documents[0].toObject<Order>()!!
                    nextOrder.orderID = querySnapshot.documents[0].id
                    nextOrder
                }
            }
    }
}