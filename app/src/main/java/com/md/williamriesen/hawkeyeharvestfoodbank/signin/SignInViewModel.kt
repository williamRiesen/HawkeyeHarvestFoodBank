package com.md.williamriesen.hawkeyeharvestfoodbank.signin

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
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday.NextDayOrderActivity
import com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite.OnSiteOrderActivity
import java.util.*

class SignInViewModel() : ViewModel() {
    var accountID = ""
    var clientIsOnSite = false
    var pleaseWait = MutableLiveData<Boolean>()
    private var familySizeFromFireStore: Long? = null
    var orderState: MutableLiveData<String> = MutableLiveData("NONE")

    fun determineClientLocation(accountIdArg: String, view: View, context: Context) {
        accountID = accountIdArg
        val foodBank = FoodBank()
        if (foodBank.isOpen) {
            Navigation.findNavController(view)
                .navigate(R.id.action_loginByAccountIdFragment_to_askIfOnSiteFragment)
        } else {
            signIn(accountID, clientIsOnSite, context)
        }
    }

    fun signIn(accountIdArg: String, clientIsOnSiteArg: Boolean, context: Context) {
        accountID = accountIdArg
        pleaseWait.value = true
        clientIsOnSite = clientIsOnSiteArg
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
                        val foodBank = FoodBank()
                        val intent = when {
                            foodBank.isOpen && clientIsOnSite -> Intent(
                                context,
                                OnSiteOrderActivity::class.java
                            )
                            else -> Intent(context, NextDayOrderActivity::class.java)
                        }

                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        Log.d("TAG", "accountId: $accountID")
                        intent.putExtra("ACCOUNT_ID", accountID)
                        orderState.value = if (documentSnapshot["orderState"] != null) {
                            documentSnapshot["orderState"] as String
                        } else {
                            "NOT STARTED YET"
                        }
                        Log.d("TAG", "orderState: ${orderState.value}")
                        intent.putExtra("ORDER_STATE", orderState.value)
                        familySizeFromFireStore = documentSnapshot["familySize"] as Long
                        Log.d("TAG", "familySize: ${familySizeFromFireStore}")
                        intent.putExtra("FAMILY_SIZE", familySizeFromFireStore!!.toInt())
                        val timeStamp: Timestamp = documentSnapshot["lastOrderDate"] as Timestamp
                        val lastOrderDate = Date(timeStamp.seconds * 1000)
                        Log.d("TAG", "lastOrderDate (from signInActivity): $lastOrderDate")
                        intent.putExtra(
                            "LAST_ORDER_DATE_TIMESTAMP",
                            documentSnapshot["lastOrderDate"] as Timestamp
                        )
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
}