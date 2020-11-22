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
    var clientState = ClientState.ELIGIBLE_TO_ORDER
    var pleaseWait = MutableLiveData<Boolean>()
    var clientIsOnSite = false

    fun determineClientLocation(accountIdArg: String, view: View, context: Context) {
        Navigation.findNavController(view)
            .navigate(R.id.action_loginByAccountIdFragment_to_askIfOnSiteFragment)
    }


    fun retrieveClientInformation(accountID: String, view: View, context: Context) {
        pleaseWait.value = true

        // Handling staff gets a special condition to redirect them to a more secure login. This is
        // for food bank volunteers and workers
        if (accountID == "STAFF") {
            val intent = Intent(context, SignStaffInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            pleaseWait.value = false
            context.startActivity(intent)
        }
        // General case is for food bank customers
        else {
            this.accountID = accountID

            // Account Service (this should be injected into the activity eventually)
            val accountService = AccountService(FirebaseFirestore.getInstance())

            // Fetch the user account from the database
            accountService.fetchAccount(accountID)
                .addOnSuccessListener { account ->
                    if (account != null) {
                        clientState = account.clientState
                        when {
                            account.clientState == ClientState.PLACED_ON_SITE -> {
                                clientIsOnSite = true
                                generateIntentAndStartNextActivity(context, account.clientState)
                            }
                            account.clientState == ClientState.ELIGIBLE_TO_ORDER && FoodBank().isOpen -> {
                                determineClientLocation(accountID, view, context)
                            }
                            !FoodBank().isTakingNextDayOrders -> {
                                clientIsOnSite = false
                                generateIntentAndStartNextActivity(context, account.clientState)
                            }
                            else -> {
                                determineClientLocation(accountID, view, context)
                            }
                        }
                    } else {
                        pleaseWait.value = false
                        Toast.makeText(context, "Sorry, Not a valid account.", Toast.LENGTH_LONG)
                            .show()
                    }

                }
                .addOnFailureListener {
                    pleaseWait.value = false
                    Toast.makeText(context, "Check your internet connection.", Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    fun generateIntentAndStartNextActivity(context: Context, clientState: ClientState) {
        Log.d("TAG", "clientState: $clientState")
        Log.d("TAG", "clientIsOnSite: $clientIsOnSite")
        Log.d("TAG", "isTakingNextDayOrders: ${FoodBank().isTakingNextDayOrders}")
        val intent =
            if (clientState == ClientState.ELIGIBLE_TO_ORDER) {
                when {
                    FoodBank().isOpen && clientIsOnSite -> Intent(
                        context,
                        OnSiteOrderActivity::class.java
                    )
                    else -> if (FoodBank().isTakingNextDayOrders) {
                        Intent(context, NextDayOrderActivity::class.java)
                    } else {
                        Intent(context, NotTakingOrdersNowMessageActivity::class.java)
                    }
                }
            } else Intent(context, getDestinationActivity(clientState)::class.java)
        Log.d("TAG", "intent: $intent")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        pleaseWait.value = false
        context.startActivity(intent)
    }

    private fun getDestinationActivity(clientState: ClientState): Activity
         = when (clientState) {
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