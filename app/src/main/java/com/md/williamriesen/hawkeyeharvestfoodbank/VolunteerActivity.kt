package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
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


}