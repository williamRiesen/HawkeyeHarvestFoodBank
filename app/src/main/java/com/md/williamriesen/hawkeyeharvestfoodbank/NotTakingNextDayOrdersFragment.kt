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


class NotTakingNextDayOrdersFragment : Fragment() {
    lateinit var viewModel: NextDayOrderingActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(NextDayOrderingActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_not_taking_next_day_orders, container, false)
        val textViewPleaseReturnOn = fragment.findViewById<TextView>(R.id.textViewPleaseReturnOn)
        textViewPleaseReturnOn.text = viewModel.returnOnMessage
        return fragment
    }
}