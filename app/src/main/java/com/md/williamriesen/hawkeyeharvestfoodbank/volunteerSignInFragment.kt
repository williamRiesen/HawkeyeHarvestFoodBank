package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_volunteer_sign_in.*

class VolunteerSignInFragment : Fragment() {

    lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val volunteerSignInFragment =
            inflater.inflate(R.layout.fragment_volunteer_sign_in, container, false)
        val textViewOrderCount =
            volunteerSignInFragment.findViewById<TextView>(R.id.textViewOrderCount)
        val countObserver = Observer<Int> { newCount: Int ->
            textViewOrderCount.text = newCount.toString()
        }
        val imageButtonNoShow = volunteerSignInFragment.findViewById<ImageButton>(R.id.imageButtonNoShow)
        imageButtonNoShow.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_volunteerSignInFragment_to_noShowFragment)
        }
        viewModel.ordersToPackCount.observe(viewLifecycleOwner, countObserver)
        viewModel.getNextOrderFromFireStore()
        return volunteerSignInFragment
    }
}