package com.md.williamriesen.hawkeyeharvest.orderoffsite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.R


class OrderSubmittedFragment : Fragment() {
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
        val orderSubmittedFragment = inflater.inflate(R.layout.fragment_order_submitted, container, false)
        val buttonOK = orderSubmittedFragment.findViewById<Button>(R.id.buttonOK2)
        buttonOK.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_orderSubmittedFragment_to_displayNumberFragment)
        }
        return orderSubmittedFragment
    }



}