package com.md.williamriesen.hawkeyeharvest.volunteer

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.messaging.FirebaseMessaging
import com.md.williamriesen.hawkeyeharvest.HawkeyeHarvestFoodBankApplication
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.signin.AccountService
import com.md.williamriesen.hawkeyeharvest.signin.OrderService
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import javax.inject.Inject


class VolunteerActivity : AppCompatActivity() {
    @Inject
    lateinit var accountService: AccountService
    @Inject
    lateinit var orderService: OrderService

    private var initialEntry = true
    private var pressedTime: Long = 0

    private lateinit var viewModel: VolunteerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as HawkeyeHarvestFoodBankApplication).appComponent.inject(this)

        setContentView(R.layout.activity_volunteer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(VolunteerActivityViewModel::class.java)
        viewModel.accountService = accountService
        viewModel.orderService = orderService
        viewModel.setUpSubmittedOrdersListener()


        // TODO what is this?
        FirebaseMessaging.getInstance().subscribeToTopic("volunteer")
    }

    fun onReadyButtonClicked(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_volunteerSignInFragment_to_packOrderFragment)
    }

    fun onCheckBoxClicked(view: View){
        val itemName = findViewById<TextView>(R.id.textview_item_to_pack_name).text.toString()
        viewModel.togglePackedState(itemName)
        view.recyclerviewChoices.adapter?.notifyDataSetChanged()
        if (viewModel.checkIfAllItemsPacked()){
            Navigation.findNavController(view).navigate(R.id.action_packOrderFragment_to_confirmPacked)
        }
    }
}