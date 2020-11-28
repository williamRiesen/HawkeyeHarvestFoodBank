package com.md.williamriesen.hawkeyeharvestfoodbank.signin;

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.*
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*

class Account(
    val accountID: String,
    val familySize: Long,
    val lastOrderDate: Date,
    val lastOrderType: OrderType,
    val orderState: OrderState,
    val pickUpHour24: Int
) {
    val clientState: ClientState
        get() =
            when (lastOrderDate?.let { WhenOrdered.fromDate(it, FoodBank()) }) {
                WhenOrdered.PRIOR_TO_THIS_MONTH -> ClientState.ELIGIBLE_TO_ORDER
                WhenOrdered.EARLIER_THIS_MONTH -> {
                    when (orderState) {
                        OrderState.PACKED -> ClientState.PICKED_UP
                        OrderState.NO_SHOW -> ClientState.NO_SHOWED
                        OrderState.SAVED -> ClientState.ELIGIBLE_TO_ORDER
                        else -> ClientState.ERROR_STATE
                    }
                }
                WhenOrdered.YESTERDAY -> {
                    when (orderState) {
                        OrderState.SUBMITTED -> ClientState.PLACED_YESTERDAY_PENDING
                        OrderState.PACKED -> ClientState.PLACED_YESTERDAY_PACKED
                        OrderState.SAVED -> ClientState.ELIGIBLE_TO_ORDER
                        OrderState.NO_SHOW -> ClientState.NO_SHOWED
                        else -> ClientState.ERROR_STATE
                    }
                }
                WhenOrdered.TODAY -> {
                    when (lastOrderType) {
                        OrderType.NEXT_DAY -> ClientState.PLACED_TODAY_FOR_TOMORROW
                        OrderType.ON_SITE -> ClientState.PLACED_ON_SITE
                        else -> {
                            if (orderState == OrderState.SAVED) {
                                ClientState.ELIGIBLE_TO_ORDER
                            } else ClientState.ERROR_STATE
                        }
                    }
                }
                else -> ClientState.ELIGIBLE_TO_ORDER
            }
}

class AccountService(val db: FirebaseFirestore) {

    fun fetchAccount(accountID: String): Task<Account> {

        // Create doc reference
        val docRef = db.collection("accounts").document(accountID)
        Log.d("TAG", "docRef: $docRef")

        // Fetches data
        return docRef.get()
            .continueWith {
                val documentSnapshot: DocumentSnapshot = it.result!!
                if (documentSnapshot.exists()) {
                    Log.d("TAG", "lastOrderDate: ${documentSnapshot["lastOrderDate"]}")
                    val lastOrderDate = dbToDate(documentSnapshot, "lastOrderDate")
                    val familySize = dbToLong(documentSnapshot, "familySize")
                    val lastOrderType =
                        OrderType.valueOf(dbToString(documentSnapshot, "lastOrderType", "ON_SITE"))
                    val orderState = OrderState.valueOf(
                        dbToString(
                            documentSnapshot,
                            "orderState",
                            "NOT_STARTED_YET"
                        )
                    )

                    val pickUpHour24 = dbToInt(documentSnapshot, "pickUpHour24", 0)

                    Account(
                        accountID,
                        familySize,
                        lastOrderDate,
                        lastOrderType,
                        orderState,
                        pickUpHour24
                    )
                } else {
                    throw NoSuchAccountException("No account retrieved with this id.")
                }
            }
    }

    private fun dbToDate(doc: DocumentSnapshot, key: String): Date {
        val timeStamp = doc[key] as Timestamp
        return Date(timeStamp.seconds * 1000)
    }

    private fun dbToLong(doc: DocumentSnapshot, key: String): Long =
        doc[key] as Long

    private fun dbToInt(doc: DocumentSnapshot, key: String, default: Int): Int =
        if (doc[key] != null) {
            (doc[key] as Long).toInt()
        } else {
            0
        }

    private fun dbToString(doc: DocumentSnapshot, key: String, default: String): String =
        if (doc[key] != null) {
            doc[key] as String
        } else {
            default
        }

}

class NoSuchAccountException(message: String): Exception(message)
