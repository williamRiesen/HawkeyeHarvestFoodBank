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


class NextDayOrderConfirmedFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_next_day_order_confirmed, container, false)
        val textViewPickUpInstructions = fragment.findViewById<TextView>(R.id.textViewPickUpInstructions)
        val startOfWindow = viewModel.pickUpHour24 - 12
        val endOfWindow = startOfWindow + 1
        textViewPickUpInstructions.text = "Please go to the food bank between $startOfWindow PM and $endOfWindow PM to pick up your order."
        val buttonShowNumber = fragment.findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_nextDayOrderConfirmedFragment_to_displayNumberFragment2)
        }
        return fragment
    }


}