package com.md.williamriesen.hawkeyeharvestfoodbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_checkout.view.*

class MainActivity : AppCompatActivity() {

//    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.retrieveObjectCatalogFromFireStore()
        viewModel.generateChoices()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.menu_item_volunteer_sign_in -> startActivity(Intent(this,VolunteerActivity::class.java))
            R.id.menu_item_manager_sign_in -> startActivity(Intent(this,ManagerActivity::class.java))
            R.id.menu_item_director_sign_in -> startActivity(Intent(this,DirectorActivity::class.java))
        }
        return true
    }

    fun retrieveRecyclerView(): RecyclerView{
        return findViewById<RecyclerView>(R.id.recyclerviewChoices)
    }

    fun onAddItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.textview_item_name).text.toString()
        viewModel.addItem(itemName)
        view.recyclerviewChoices.adapter?.notifyDataSetChanged()
    }

    fun onRemoveItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.textview_item_name).text.toString()
        val updatedCount = viewModel.removeItem(itemName)
        val textViewCount = findViewById<TextView>(R.id.textView_count)
        textViewCount.text = updatedCount.toString()
        view.recyclerviewChoices.adapter?.notifyDataSetChanged()
    }

    fun onCartButtonClick(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_selectionFragment_to_checkoutFragment)
    }

    fun onShopButtonClick(view: View){

        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
         val accountID = editTextAccountID.text.toString()
        viewModel.signIn(accountID, applicationContext, view)!!
    }

    fun onSubmitButtonClick(view: View){
        viewModel.submitOrder(view)
    }

    fun onDoneButtonClick(view: View){
        Navigation.findNavController(view).navigate(R.id.action_doneFragment_to_signInFragment)
        }
    }



