package com.md.williamriesen.hawkeyeharvestfoodbank

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import kotlinx.android.synthetic.main.fragment_checkout.view.*

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        var accountID = intent.extras["ACCOUNT_ID"].toString()
        var familySize = intent.extras["FAMILY_SIZE"]
        viewModel.accountID = accountID

        Log.d("TAG", " MainActivity viewModel.accountID: ${viewModel.accountID}")
        viewModel.familySize = familySize as Int
        viewModel.retrieveObjectCatalogFromFireStore()


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val myNotificationManager = MyNotificationManager(applicationContext)
//            val mNotificationManager = myNotificationManager.getInstance(applicationContext)
//            val mChannel = NotificationChannel(
//                "myChannelId",
//                "myChannelName",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            mChannel.description = "myChannelDescription"
//            mChannel.enableLights(true)
//            mChannel.lightColor = Color.GREEN
//            mChannel.enableVibration(true)
//            val myVibrationPattern: LongArray =
//                longArrayOf(100L, 200L, 300L, 400L, 500L, 400L, 300L, 200L, 400L)
//            mChannel.vibrationPattern = myVibrationPattern
//
//        } else {
//            Log.d("TAG", "Below Oreo branch used.")
//        }
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

    fun onShopButtonClick(view: View) {

        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        val accountID = editTextAccountID.text.toString()
        viewModel.signIn(accountID, applicationContext, view)
    }

    fun onSubmitButtonClick(view: View) {
        viewModel.submitOrder(view)
    }

    fun onDoneButtonClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_doneFragment_to_clientStartFragment)
    }

    fun onExitButtonClick(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}





