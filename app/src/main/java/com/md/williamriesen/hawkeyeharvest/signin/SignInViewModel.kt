package com.md.williamriesen.hawkeyeharvest.signin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.md.williamriesen.hawkeyeharvest.*
import com.md.williamriesen.hawkeyeharvest.foodbank.ClientState
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvest.orderfornextday.NextDayOrderActivity
import com.md.williamriesen.hawkeyeharvest.orderonsite.OnSiteOrderActivity

class SignInViewModel() : ViewModel() {
    private var currentContext: Context? = null
    private var currentView: View? = null
    private var currentAccount: Account? = null
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
        currentView = view

        currentContext = context

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
                    if (account == null) {
                        pleaseWait.value = false
                        Toast.makeText(context, "Sorry, Not a valid account.", Toast.LENGTH_LONG)
                            .show()
                        return@addOnSuccessListener
                    }
                    currentAccount = account
                    clientState = account.clientState
                    Log.d("TAG", "clientState: $clientState")
                    if (clientState == ClientState.ELIGIBLE_TO_ORDER) {
                        val foodBank = FoodBank()
                        if (foodBank.isOpen && foodBank.isTakingNextDayOrders) {
                            // Requires user to indicate if they are at the food bank or not
                            determineClientLocation(accountID, view, context)
                        } else if (foodBank.isOpen) {
                            clientIsOnSite = true
                            generateIntentAndStartNextActivity(context, clientState, account)
                        } else {
                            clientIsOnSite = false
                            generateIntentAndStartNextActivity(context, clientState, account)
                        }
                    } else {
                        generateIntentAndStartNextActivity(context, clientState, account)
                    }
                }
                .addOnFailureListener { exception ->
                    pleaseWait.value = false
                    Log.e("TAG", "exception report",exception)
                    val errorMessage = when (exception::class) {
                        NoSuchAccountException::class -> "Not a valid account"
                        FirebaseFirestoreException::class -> "Unable to connect: please check your internet connection."
                        else -> "An unknown problem occurred."
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG)
                        .show()

                }
        }
    }

    fun reportClientIsOnSite(clientIsOnSiteArg: Boolean) {
        clientIsOnSite = clientIsOnSiteArg
        generateIntentAndStartNextActivity(currentContext!!, clientState, currentAccount!!)
    }

    fun generateIntentAndStartNextActivity(
        context: Context,
        clientState: ClientState,
        account: Account
    ) {
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
            } else Intent(context, clientState.getNextActivity(clientIsOnSite))
        Log.d("TAG", "intent: $intent")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("ACCOUNT_ID", account.accountID)
        intent.putExtra("LAST_ORDER_DATE", account.lastOrderDate)
        intent.putExtra("FAMILY_SIZE", account.familySize)
        intent.putExtra("ORDER_STATE", account.orderState)
        intent.putExtra("PICK_UP_HOUR24", account.pickUpHour24)
        intent.putExtra("COMING_FROM_SIGN_IN", true)

        pleaseWait.value = false
        context.startActivity(intent)
    }


}