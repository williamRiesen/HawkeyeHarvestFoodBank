package com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import com.md.williamriesen.hawkeyeharvestfoodbank.MainActivityViewModel
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import java.util.*

class NextDayOrderActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_day_order)
        FirebaseMessaging.getInstance().unsubscribeFromTopic("volunteer")

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        var accountID = intent.extras["ACCOUNT_ID"].toString()
        var familySize = intent.extras["FAMILY_SIZE"]
        var timeStamp = intent.extras["LAST_ORDER_DATE_TIMESTAMP"] as Timestamp
        val lastOrderDate = Date(timeStamp.seconds * 1000)
        var orderState = intent.extras["ORDER_STATE"] as String

        viewModel.accountID = accountID
        viewModel.lastOrderDate = lastOrderDate
        viewModel.orderState = MutableLiveData(orderState)
        viewModel.familySize = familySize as Int
        viewModel.retrieveObjectCatalogFromFireStore()
    }

    fun onCartButtonClick(view: View){
        Navigation.findNavController(view).navigate(R.id.action_selectionFragment2_to_checkoutFragment2)
    }
}