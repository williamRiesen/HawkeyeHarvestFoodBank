package com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.md.williamriesen.hawkeyeharvestfoodbank.FoodBank
import com.md.williamriesen.hawkeyeharvestfoodbank.Item
import com.md.williamriesen.hawkeyeharvestfoodbank.Order
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import java.util.*

class OnSiteOrderingViewModel : ViewModel() {

    var accountID = ""
    val itemList = MutableLiveData<MutableList<Item>>()
    var orderID: String? = null
    var isOpen = MutableLiveData<Boolean>(false)
    var familySize = 0
    var orderState: MutableLiveData<String> = MutableLiveData("NONE")
    var lastOrderDate: Date? = null

    fun shop(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_clientStartFragment_to_instructionsFragment)
    }

    fun navigateToNextFragment(view: View) {
        Log.d("TAG", "nextFragment: $nextFragment")
        Navigation.findNavController(view).navigate(nextFragment)
    }

    private val whenOrdered: String
        get() {
            val foodBank = FoodBank()
            val startOfToday: Date = foodBank.getCurrentDateWithoutTime()
            val calendar = Calendar.getInstance()
            calendar.time = startOfToday
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startOfThisMonth = calendar.time
            Log.d(
                "TAG",
                "lastOrderDate: $lastOrderDate, startOfToday: $startOfToday, startOfThisMonth: $startOfThisMonth"
            )
            return when {
                lastOrderDate!! > startOfToday -> "TODAY"
                lastOrderDate!! > startOfThisMonth -> "EARLIER_THIS_MONTH"
                else -> "PRIOR_TO_THIS_MONTH"
            }
        }

    val nextFragment: Int
        get() {
            return when (orderState.value) {
                "SAVED" -> R.id.action_clientStartFragment_to_shopVsCheckOutFragment
                "SUBMITTED" -> {
                    Log.d("TAG", "whenOrdered: $whenOrdered")
                    if (whenOrdered == "TODAY") {
                        R.id.action_clientStartFragment_to_orderBeingPackedFragment
                    } else {
                        R.id.action_clientStartFragment_to_errorMessageFragment
                    }
                }
                "PACKED" -> {
                    when (whenOrdered) {
                        "TODAY" -> R.id.action_clientStartFragment_to_orderReadyFragment
                        "EARLIER_THIS_MONTH" -> R.id.action_clientStartFragment_to_shopForNextMonthFragment
                        else -> R.id.action_clientStartFragment_to_instructionsFragment
                    }
                }
                "NO SHOW" -> {
                    if (whenOrdered == "PRIOR_TO_THIS_MONTH") {
                        R.id.action_clientStartFragment_to_instructionsFragment
                    } else {
                        R.id.action_clientStartFragment_to_notPickedUpFragment
                    }
                }
                else -> R.id.action_onSiteOrderStartFragment_to_instructionsFragment2
            }
        }

    fun submitOnSiteOrder(view: View) {
        val thisOrder = Order(accountID, Date(), itemList.value!!, "SUBMITTED")
        val filteredOrder = thisOrder.filterOutZeros()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("TAG", "getInstanceID failed ${it.exception}")
                }
                val token = it.result?.token
                filteredOrder.deviceToken = token
                Log.d("TAG", "token: $token")

                val db = FirebaseFirestore.getInstance()
                if (orderID != null) {
                    db.collection(("orders")).document(orderID!!).set(filteredOrder)
                        .addOnSuccessListener {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_askWhetherToSubmitSavedOrderFragment_to_orderSubmittedFragment)
                        }
                } else {
                    db.collection(("orders")).document().set(filteredOrder)
                        .addOnSuccessListener {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_askWhetherToSubmitSavedOrderFragment_to_orderSubmittedFragment)
                        }
                }
            }
    }
}