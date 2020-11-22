package com.md.williamriesen.hawkeyeharvestfoodbank.signin;

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.ClientState
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodBank
import java.util.*

class Account(
    val accountID: String,
    val familySize: Long,
    val lastOrderDate: Date,
    val lastOrderType: String,
    val orderState: String,
    val pickUpHour24: Int
) {

    val clientState: ClientState
        get() =
            when (whenOrdered) {
                "PRIOR_TO_THIS_MONTH" -> ClientState.ELIGIBLE_TO_ORDER
                "EARLIER_THIS_MONTH" -> {
                    when (orderState) {
                        "PACKED" -> ClientState.PICKED_UP
                        "NO_SHOW" -> ClientState.NO_SHOWED
                        "SAVED" -> ClientState.ELIGIBLE_TO_ORDER
                        else -> ClientState.ERROR_STATE
                    }
                }
                "YESTERDAY" -> {
                    when (orderState) {
                        "SUBMITTED" -> ClientState.PLACED_YESTERDAY_PENDING
                        "PACKED" -> ClientState.PLACED_YESTERDAY_PACKED
                        "SAVED" -> ClientState.ELIGIBLE_TO_ORDER
                        "NO_SHOW" -> ClientState.NO_SHOWED
                        else -> ClientState.ERROR_STATE
                    }
                }
                "TODAY" -> {
                    when (lastOrderType) {
                        "NEXT_DAY" -> ClientState.PLACED_TODAY_FOR_TOMORROW
                        "ON_SITE" -> ClientState.PLACED_ON_SITE
                        else -> {
                            if (orderState == "SAVED") {
                                ClientState.ELIGIBLE_TO_ORDER
                            } else ClientState.ERROR_STATE
                        }
                    }
                }
                else -> ClientState.ELIGIBLE_TO_ORDER
            }

    private val whenOrdered: String
        get() {
            val foodBank = FoodBank()
            val startOfToday: Date = foodBank.getCurrentDateWithoutTime()
            val calendar = Calendar.getInstance()
            calendar.time = startOfToday
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val startOfYesterday = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startOfThisMonth = calendar.time
            Log.d(
                "TAG",
                "lastOrderDate: $lastOrderDate, startOfToday: $startOfToday, startOfThisMonth: $startOfThisMonth"
            )
            return when {
                lastOrderDate > startOfToday -> "TODAY"
                lastOrderDate > startOfYesterday -> "YESTERDAY"
                lastOrderDate > startOfThisMonth -> "EARLIER_THIS_MONTH"
                else -> "PRIOR_TO_THIS_MONTH"
            }
        }
}

class AccountService(val db: FirebaseFirestore) {

    fun fetchAccount(accountID: String): Task<Account> {

        // Create doc reference
        val docRef = db.collection("accounts").document(accountID)
        // Fetches data

        return docRef.get()
            .continueWith {
                val documentSnapshot: DocumentSnapshot = it.getResult()!!

                val lastOrderDate = dbToDate(documentSnapshot, "lastOrderDate")
                val familySize = dbToLong(documentSnapshot, "familySize")

                val lastOrderType = dbToString(documentSnapshot, "lastOrderType", "ON_SITE")
                val orderState = dbToString(documentSnapshot, "orderState", "NOT STARTED YET")

                val pickUpHour24 = dbToInt(documentSnapshot, "pickUpHour24", 0)

                Account(
                    accountID,
                    familySize,
                    lastOrderDate,
                    lastOrderType,
                    orderState,
                    pickUpHour24
                )
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
