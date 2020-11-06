package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class NextDayOrderingStartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_next_day_ordering_start, container, false)
        val buttonNext = fragment.findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nextDayOrderingStartFragment_to_notTakingNextDayOrdersFragment)
        }
        return fragment
    }
}
