package com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.Timestamp
import com.md.williamriesen.hawkeyeharvestfoodbank.MainActivityViewModel
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import java.util.*

class OnSiteOrderActivity : AppCompatActivity() {
    private lateinit var viewModel: OnSiteOrderingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OnSiteOrderingViewModel::class.java)

        var accountID = intent.extras["ACCOUNT_ID"].toString()
        var familySize = intent.extras["FAMILY_SIZE"]
        var timeStamp = intent.extras["LAST_ORDER_DATE_TIMESTAMP"] as Timestamp
        val lastOrderDate = Date(timeStamp.seconds * 1000)
        Log.d("TAG","lastOrderDate received as activity parameter: $lastOrderDate")
        var orderState = intent.extras["ORDER_STATE"] as String

        viewModel.accountID = accountID
        viewModel.lastOrderDate = lastOrderDate
        viewModel.orderState.value = orderState
        viewModel.familySize = familySize as Int
        viewModel.retrieveObjectCatalogFromFireStore()
        setContentView(R.layout.activity_on_site_ordering)
    }
}