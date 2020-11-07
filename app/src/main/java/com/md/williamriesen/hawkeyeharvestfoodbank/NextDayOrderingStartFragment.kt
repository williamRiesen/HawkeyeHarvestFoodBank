package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

class NextDayOrderingStartFragment : Fragment() {

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
        val fragment = inflater.inflate(R.layout.fragment_next_day_ordering_start, container, false)
        val buttonNext = fragment.findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener {
            if (viewModel.takingOrders) {
                Navigation.findNavController(it)
                    .navigate(R.id.action_nextDayOrderingStartFragment_to_selectPickUpTimeFragment)
            } else {
                Navigation.findNavController(it)
                    .navigate(R.id.action_nextDayOrderingStartFragment_to_notTakingNextDayOrdersFragment)
            }
        }
        return fragment
    }
}
