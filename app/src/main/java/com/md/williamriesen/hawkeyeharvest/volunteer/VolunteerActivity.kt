package com.md.williamriesen.hawkeyeharvest.volunteer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import com.google.firebase.messaging.FirebaseMessaging
import com.md.williamriesen.hawkeyeharvest.R
import kotlinx.android.synthetic.main.fragment_checkout.view.*


class VolunteerActivity : AppCompatActivity() {

    private var initialEntry = true
    private var pressedTime: Long = 0

    private lateinit var viewModel: VolunteerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this).get(VolunteerActivityViewModel::class.java)
        FirebaseMessaging.getInstance().subscribeToTopic("volunteer")
    }

//    override fun onBackPressed() {
//
//        val primaryFragment = supportFragmentManager.primaryNavigationFragment
//        val currentFragment = supportFragmentManager.fragments.last()
//        val isAtStart = currentFragment == primaryFragment
//        Log.d("TAG","primaryFragment: $primaryFragment")
//        Log.d("TAG", "currentFragment: $currentFragment")
//        Log.d("TAG", "isAtStart: $isAtStart")
//        Log.d("TAG", "fragments: ${supportFragmentManager.fragments}")
//        if (pressedTime + 2000 > System.currentTimeMillis()) {
//            super.onBackPressed()
//            finish()
//        } else {
//            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
//        }
//        pressedTime = System.currentTimeMillis()
//    }


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