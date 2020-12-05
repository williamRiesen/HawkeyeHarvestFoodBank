package com.md.williamriesen.hawkeyeharvest.volunteer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.R

class VolunteerConfirmExitFragment : Fragment() {
    var initialEntry = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_volunteer_confirm_exit, container, false)
        val buttonExit = fragment.findViewById<Button>(R.id.buttonExit)
        buttonExit.setOnClickListener {
            activity?.onBackPressed()
        }
        val buttonDontExit = fragment.findViewById<Button>(R.id.buttonDontExit)
        buttonDontExit.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_volunteerConfirmExitFragment2_to_volunteerSignInFragment)
        }

        return fragment
    }


}