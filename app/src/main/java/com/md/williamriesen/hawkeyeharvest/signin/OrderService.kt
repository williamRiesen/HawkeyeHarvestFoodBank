package com.md.williamriesen.hawkeyeharvest.signin

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
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
}