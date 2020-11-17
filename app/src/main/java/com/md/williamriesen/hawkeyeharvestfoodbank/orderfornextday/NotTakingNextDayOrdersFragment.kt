package com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvestfoodbank.MainActivityViewModel
import com.md.williamriesen.hawkeyeharvestfoodbank.R


class NotTakingNextDayOrdersFragment : Fragment() {
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_not_taking_next_day_orders, container, false)
        val textViewNextDayOpen = fragment.findViewById<TextView>(R.id.textViewNextDayOpen)
        textViewNextDayOpen.text = viewModel.nextDayOpen
        val textViewNextDayTakingOrders = fragment.findViewById<TextView>(R.id.textViewNextDayTakingOrders)
        textViewNextDayTakingOrders.text = viewModel.nextDayTakingOrders
        return fragment
    }
}