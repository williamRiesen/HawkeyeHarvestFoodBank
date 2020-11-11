package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.android.synthetic.main.fragment_pack_order.view.*
import kotlinx.android.synthetic.main.item_to_pack.*

class VolunteerActivity : AppCompatActivity() {

    private lateinit var viewModel: VolunteerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this).get(VolunteerActivityViewModel::class.java)
        FirebaseMessaging.getInstance().subscribeToTopic("volunteer")
        val foodBank = FoodBank()
        foodBank.sendCategoriesListToFireStore()
        foodBank.sendObjectCatalogToFireStore()
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