package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*


class OrderBeingPackedFragment : Fragment() {
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val orderReadyFragment = inflater.inflate(R.layout.fragment_order_ready, container, false)
        val buttonShowNumber = orderReadyFragment.findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_orderReadyFragment_to_displayNumberFragment)
        }

        return orderReadyFragment
    }
}