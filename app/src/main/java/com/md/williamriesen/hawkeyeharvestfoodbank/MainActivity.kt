package com.md.williamriesen.hawkeyeharvestfoodbank

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().unsubscribeFromTopic("volunteer")

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        var accountID = intent.extras["ACCOUNT_ID"].toString()
        var familySize = intent.extras["FAMILY_SIZE"]
        var timeStamp = intent.extras["LAST_ORDER_DATE_TIMESTAMP"] as Timestamp
        val lastOrderDate = Date(timeStamp.seconds * 1000)
        var orderState = intent.extras["ORDER_STATE"] as String

        viewModel.accountID = accountID
        viewModel.lastOrderDate = lastOrderDate
        viewModel.orderState.value = orderState
        viewModel.familySize = familySize as Int

        viewModel.retrieveObjectCatalogFromFireStore()
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_volunteer_sign_in -> startActivity(
                Intent(
                    this,
                    VolunteerActivity::class.java
                )
            )
            R.id.menu_item_manager_sign_in -> startActivity(
                Intent(
                    this,
                    ManagerActivity::class.java
                )
            )
            R.id.menu_item_director_sign_in -> startActivity(
                Intent(
                    this,
                    DirectorActivity::class.java
                )
            )
        }
        return true
    }

    fun onAddItem(view: View) {
        val itemName = findViewById<TextView>(R.id.textview_item_name).text.toString()
        viewModel.addItem(itemName)
        view.recyclerviewChoices.adapter?.notifyDataSetChanged()
    }

    fun onRemoveItem(view: View) {
        val itemName = findViewById<TextView>(R.id.textview_item_name).text.toString()
        val updatedCount = viewModel.removeItem()
        val textViewCount = findViewById<TextView>(R.id.textView_count)
        textViewCount.text = updatedCount.toString()
        view.recyclerviewChoices.adapter?.notifyDataSetChanged()
    }

    fun onCartButtonClick(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_selectionFragment_to_checkoutFragment)
    }

    fun onShopButtonClick(view: View) {

        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        val accountID = editTextAccountID.text.toString()
        viewModel.signIn(accountID, applicationContext)
    }

    fun onSubmitButtonClick(view: View) {
        Log.d("TAG", "onSubmitButtonClick called.")
        viewModel.saveOrder(view)
    }


    fun onExitButtonClick(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}





