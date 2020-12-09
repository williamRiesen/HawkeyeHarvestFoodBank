package com.md.williamriesen.hawkeyeharvest.volunteer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.messaging.FirebaseMessaging
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.android.synthetic.main.order.*
import kotlin.concurrent.fixedRateTimer

class VolunteerActivity : AppCompatActivity() {

    private var initialEntry = true

    private lateinit var viewModel: VolunteerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this).get(VolunteerActivityViewModel::class.java)
        FirebaseMessaging.getInstance().subscribeToTopic("volunteer")


//        val foodBank = FoodBank()
//        foodBank.sendCategoriesListToFireStore()
//        foodBank.sendObjectCatalogToFireStore()
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