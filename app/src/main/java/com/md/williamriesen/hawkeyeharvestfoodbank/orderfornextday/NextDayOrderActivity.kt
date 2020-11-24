package com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import com.md.williamriesen.hawkeyeharvestfoodbank.orderoffsite.MainActivityViewModel
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.OrderState
import java.util.*

class NextDayOrderActivity : AppCompatActivity() {

    private lateinit var viewModel: NextDayOrderingActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_day_order)
        FirebaseMessaging.getInstance().unsubscribeFromTopic("volunteer")

        viewModel = ViewModelProviders.of(this).get(NextDayOrderingActivityViewModel::class.java)

        viewModel.accountID = intent.extras?.get("ACCOUNT_ID").toString()
        viewModel.lastOrderDate = intent.extras?.get("LAST_ORDER_DATE") as Date
//        viewModel.orderState = intent.extras?.get("ORDER_STATE") as OrderState
        viewModel.familySize = (intent.extras?.get("FAMILY_SIZE") as Long).toInt()
        viewModel.retrieveObjectCatalogFromFireStore()
    }

    fun onCartButtonClick(view: View){
        Navigation.findNavController(view).navigate(R.id.action_nextDayOrderSelectionFragment_to_nextDayCheckoutFragment)
    }
}