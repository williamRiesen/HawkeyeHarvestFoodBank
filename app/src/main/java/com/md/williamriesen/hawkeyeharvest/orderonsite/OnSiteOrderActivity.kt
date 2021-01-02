package com.md.williamriesen.hawkeyeharvest.orderonsite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.HawkeyeHarvestFoodBankApplication
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.OrderState
import com.md.williamriesen.hawkeyeharvest.signin.AccountService
import com.md.williamriesen.hawkeyeharvest.signin.CatalogService
import com.md.williamriesen.hawkeyeharvest.signin.OrderService
import java.util.*
import javax.inject.Inject

class OnSiteOrderActivity : AppCompatActivity() {
    @Inject
    lateinit var accountService: AccountService
    @Inject
    lateinit var catalogService: CatalogService
    @Inject
    lateinit var orderService: OrderService

    private lateinit var viewModel: OnSiteOrderingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as HawkeyeHarvestFoodBankApplication).appComponent.inject(this)

        // TODO fix lazy inject into component
        viewModel = ViewModelProviders.of(this).get(OnSiteOrderingViewModel::class.java)
        viewModel.accountService = accountService
        viewModel.catalogService = catalogService
        viewModel.orderService = orderService

        viewModel.lastOrderDate = intent.extras?.get("LAST_ORDER_DATE") as Date
        viewModel.orderState.value = intent.extras?.get("ORDER_STATE") as OrderState

        viewModel.init()
//        viewModel.retrieveObjectCatalogFromFireStore()
        setContentView(R.layout.activity_on_site_ordering)
    }
    fun onCartButtonClick(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_onSiteOrderSelectionFragment_to_onSiteCheckoutFragment)
    }
}