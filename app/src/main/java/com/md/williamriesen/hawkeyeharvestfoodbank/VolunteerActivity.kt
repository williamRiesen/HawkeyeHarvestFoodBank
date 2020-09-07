package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation

class VolunteerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
    }
    fun onReadyButtonClicked(view: View){
        Navigation.findNavController(view).navigate(R.id.action_volunteerSignInFragment_to_packOrderFragment)
    }

}