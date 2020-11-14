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

class SignInViewModel() : ViewModel() {
    var accountID = ""
    var clientIsOnSite = false
    var pleaseWait = MutableLiveData<Boolean>()
    private var familySizeFromFireStore: Long? = null
    var orderState: MutableLiveData<String> = MutableLiveData("NONE")

    fun determineClientLocation(accountIdArg: String, view: View, context: Context) {
        accountID = accountIdArg
        Log.d("TAG", "determineClientLocation Called; accountID = $accountID")
        Log.d("TAG", "context: $context")

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
//        isOpen.value = false
        pleaseWait.value = true
        if (accountID == "STAFF") {
            val intent = Intent(context, LoginActivity::class.java)
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
                        val intent = if (acceptNextDayOrders) {
                            if (acceptSameDayOrders) {
                                if (clientIsOnSiteArg) {
                                    Intent(context, MainActivity::class.java)
                                } else {
                                    Intent(context, NextDayOrderActivity::class.java)
                                }
                            } else {
                                Intent(context, NextDayOrderActivity::class.java)
                            }
                        } else {
                            Intent(context, MainActivity::class.java)
                        }
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("ACCOUNT_ID", accountID)
                        orderState.value = if (documentSnapshot["orderState"] != null) {
                            documentSnapshot["orderState"] as String
                        } else {
                            "NOT STARTED YET"
                        }
                        intent.putExtra("ORDER_STATE", orderState.value)
                        familySizeFromFireStore = documentSnapshot["familySize"] as Long
                        intent.putExtra("FAMILY_SIZE", familySizeFromFireStore!!.toInt())
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