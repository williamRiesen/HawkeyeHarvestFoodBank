package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.md.williamriesen.hawkeyeharvestfoodbank.*
import com.md.williamriesen.hawkeyeharvestfoodbank.communication.MyFirebaseMessagingService
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.ClientState
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday.NextDayOrderActivity
import com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite.OnSiteOrderActivity
import java.util.*

class SignInViewModel() : ViewModel() {
    var accountID = ""
    var pleaseWait = MutableLiveData<Boolean>()
    private var familySizeFromFireStore: Long? = null
    var orderState: MutableLiveData<String> = MutableLiveData("NONE")
    var lastOrderDate: Date? = null
    var lastOrderType: String? = null
    var clientIsOnSite = false
    var pickUpHour24 = 0

    fun determineClientLocation(accountIdArg: String, view: View, context: Context) {
        accountID = accountIdArg
        val foodBank = FoodBank()
        if (foodBank.isOpen) {
            Navigation.findNavController(view)
                .navigate(R.id.action_loginByAccountIdFragment_to_askIfOnSiteFragment)
        } else {
            signIn(accountID, false, context)
        }
    }

    fun signIn(accountIdArg: String, clientIsOnSiteArg: Boolean, context: Context) {
        accountID = accountIdArg
        pleaseWait.value = true
        if (accountID == "STAFF") {
            val intent = Intent(context, SignStaffInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            pleaseWait.value = false
            context.startActivity(intent)
        } else {
            val myFirebaseMessagingService = MyFirebaseMessagingService()
            val token = myFirebaseMessagingService
            familySizeFromFireStore = null
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("accounts").document(accountID)
            docRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val timeStamp: Timestamp =
                            documentSnapshot["lastOrderDate"] as Timestamp
                        lastOrderDate = Date(timeStamp.seconds * 1000)
                        lastOrderType = if (documentSnapshot["lastOrderType"] != null) {
                            documentSnapshot["lastOrderType"] as String
                        } else {
                            "ON_SITE"
                        }
                        orderState.value = if (documentSnapshot["orderState"] != null) {
                            documentSnapshot["orderState"] as String
                        } else {
                            "NOT STARTED YET"
                        }
                        pickUpHour24 = if (documentSnapshot["pickUpHour24"] != null) {
                            (documentSnapshot["pickUpHour24"] as Long).toInt()
                        } else {
                            0
                        }
                        Log.d("TAG", "clientState: $clientState")
                        val intent =
                            if (clientState == ClientState.ELIGIBLE_TO_ORDER) {
                                when {
                                    FoodBank().isOpen && clientIsOnSiteArg -> Intent(
                                        context,
                                        OnSiteOrderActivity::class.java
                                    )
                                    else -> Intent(context, NextDayOrderActivity::class.java)
                                }
                            } else Intent(context, destinationActivity::class.java)
                        Log.d("TAG", "intent: $intent")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        Log.d("TAG", "accountId: $accountID")
                        intent.putExtra("ACCOUNT_ID", accountID)

                        Log.d("TAG", "orderState: ${orderState.value}")
                        intent.putExtra("ORDER_STATE", orderState.value)
                        familySizeFromFireStore = documentSnapshot["familySize"] as Long
                        Log.d("TAG", "familySize: ${familySizeFromFireStore}")
                        intent.putExtra("FAMILY_SIZE", familySizeFromFireStore!!.toInt())

                        Log.d("TAG", "lastOrderDate (from signInActivity): $lastOrderDate")
                        intent.putExtra(
                            "LAST_ORDER_DATE_TIMESTAMP",
                            documentSnapshot["lastOrderDate"] as Timestamp
                        )
                        intent.putExtra("LAST_ORDER_TYPE", lastOrderType)
                        intent.putExtra("PICKUP_HOUR24", pickUpHour24)
                        pleaseWait.value = false
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(
                            context,
                            "Sorry, Not a valid account.",
                            Toast.LENGTH_LONG
                        ).show()
                        pleaseWait.value = false
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Retrieve family size from database failed.")
                }
        }
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
                lastOrderDate!! > startOfToday -> "TODAY"
                lastOrderDate!! > startOfYesterday -> "YESTERDAY"
                lastOrderDate!! > startOfThisMonth -> "EARLIER_THIS_MONTH"
                else -> "PRIOR_TO_THIS_MONTH"
            }
        }
    val clientState: ClientState
        get() =
            when (whenOrdered) {
                "PRIOR_TO_THIS_MONTH" -> ClientState.ELIGIBLE_TO_ORDER
                "EARLIER_THIS_MONTH" -> {
                    when (orderState.value) {
                        "PACKED" -> ClientState.PICKED_UP
                        "NO_SHOW" -> ClientState.NO_SHOWED
                        "SAVED" -> ClientState.ELIGIBLE_TO_ORDER
                        else -> ClientState.ERROR_STATE
                    }
                }
                "YESTERDAY" -> {
                    when (orderState.value) {
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
                            if (orderState.value == "SAVED") {
                                ClientState.ELIGIBLE_TO_ORDER
                            } else ClientState.ERROR_STATE
                        }
                    }
                }
                else -> ClientState.ELIGIBLE_TO_ORDER
            }

    val destinationActivity: Activity
        get() = when (clientState) {
            ClientState.ELIGIBLE_TO_ORDER -> {
                if (clientIsOnSite) {
                    OnSiteOrderActivity()
                } else {
                    NextDayOrderActivity()
                }
            }
            ClientState.PLACED_ON_SITE -> InProgressOnSiteInstructionsActivity()
            ClientState.PLACED_YESTERDAY_PENDING -> PickUpLaterTodayInstructionsActivity()
            ClientState.PLACED_YESTERDAY_PACKED -> PickUpNowInstructionsActivity()
            ClientState.PLACED_TODAY_FOR_TOMORROW -> PickUpTomorrowInstructionsActivity()
            ClientState.NO_SHOWED -> NoShowMessageActivity()
            ClientState.PICKED_UP -> AlreadyServedMessageActivity()
            ClientState.ERROR_STATE -> ErrorMessageActivity()
        }
}