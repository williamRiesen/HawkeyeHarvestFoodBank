package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat


class OrderSavedFragment : Fragment() {
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
        val orderSavedFragment = inflater.inflate(R.layout.fragment_order_saved, container, false)
        val buttonOK = orderSavedFragment.findViewById<Button>(R.id.buttonOK)
        buttonOK.setOnClickListener { activity?.onBackPressed() }
        val textViewEarliestNextOrderDate = orderSavedFragment.findViewById<TextView>(R.id.textViewEarliestNextOrderDate2)
        val formattedEarliestDate =
            DateFormat.getDateInstance().format(viewModel.earliestOrderDate)
        textViewEarliestNextOrderDate.text = formattedEarliestDate
        return orderSavedFragment
    }
}