package com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.OrderState
import java.util.*

class OnSiteOrderActivity : AppCompatActivity() {
    private lateinit var viewModel: OnSiteOrderingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OnSiteOrderingViewModel::class.java)

        viewModel.accountID = intent.extras?.get("ACCOUNT_ID").toString()
        viewModel.lastOrderDate = intent.extras?.get("LAST_ORDER_DATE") as Date
        viewModel.orderState.value = intent.extras?.get("ORDER_STATE") as OrderState
        viewModel.familySize = (intent.extras?.get("FAMILY_SIZE") as Long).toInt()
        viewModel.retrieveObjectCatalogFromFireStore()
        setContentView(R.layout.activity_on_site_ordering)
    }
    fun onCartButtonClick(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_onSiteOrderSelectionFragment_to_onSiteCheckoutFragment)
    }
}