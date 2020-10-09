package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore

class ManagerActivity : AppCompatActivity() {

    private lateinit var viewModel: ManagerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this).get(ManagerActivityViewModel::class.java)
    }

    fun submitUpdatedInventory() {
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
