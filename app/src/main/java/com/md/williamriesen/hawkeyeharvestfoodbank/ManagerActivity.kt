package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.android.synthetic.main.fragment_update_inventory.view.*

class ManagerActivity : AppCompatActivity() {

    private lateinit var viewModel: ManagerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this).get(ManagerActivityViewModel::class.java)
    }

    fun submitUpdatedInventory(view: View) {
        val objectCatalog = ObjectCatalog(viewModel.itemsToInventoryList.value!!)
        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(objectCatalog)
            .addOnSuccessListener {
                Toast.makeText(this,"Inventory Updated.",Toast.LENGTH_LONG).show()
            }
    }

//    fun onCheckBoxClicked(view: View){
//        val itemName = findViewById<TextView>(R.id.textview_item_to_inventory_name).text.toString()
//        viewModel.toggleIsAvailableStatus(itemName)
//        view.recyclerviewInventoryForUpdate.adapter?.notifyDataSetChanged()
//    }

}
