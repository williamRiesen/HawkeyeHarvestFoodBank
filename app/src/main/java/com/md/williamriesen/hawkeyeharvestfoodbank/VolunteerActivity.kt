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
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.android.synthetic.main.fragment_pack_order.view.*
import kotlinx.android.synthetic.main.item_to_pack.*

class VolunteerActivity : AppCompatActivity() {

    private lateinit var viewModel: VolunteerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
        viewModel = ViewModelProviders.of(this).get(VolunteerActivityViewModel::class.java)
    }

    fun onReadyButtonClicked(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_volunteerSignInFragment_to_packOrderFragment)
    }

    fun onCheckBoxClicked(view: View){
        val itemName = findViewById<TextView>(R.id.textview_item_to_pack_name).text.toString()
        Log.d("TAG","itemName: $itemName")

        viewModel.togglePackedState(itemName)
        view.recyclerviewChoices.adapter?.notifyDataSetChanged()
        Log.d("TAG","all packed: ${viewModel.checkIfAllItemsPacked()}")
        if (viewModel.checkIfAllItemsPacked()){
            Navigation.findNavController(view).navigate(R.id.action_packOrderFragment_to_confirmPacked)
        }
    }
}