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
import java.util.*


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
        val buttonOK = orderSavedFragment.findViewById<Button>(R.id.buttonExit)
        buttonOK.setOnClickListener {
            activity?.onBackPressed()
        }
        val textViewEarliestNextOrderDate =
            orderSavedFragment.findViewById<TextView>(R.id.textViewEarliestNextOrderDate2)
        val orderReceivedMessage =
            orderSavedFragment.findViewById<TextView>(R.id.textViewOrderSavedMessage)
        if (viewModel.mayOrderNow) {
            val formattedNextDayOpen =
                DateFormat.getDateInstance().format((viewModel.nextDayOpen))
            textViewEarliestNextOrderDate.text = formattedNextDayOpen
            orderReceivedMessage.text =
                "Your order has been saved. You may activate this order for packing and pickup when the food bank opens at 12 noon on"
        } else {
            val formattedEarliestDate =
                DateFormat.getDateInstance().format(viewModel.earliestOrderDate)
            textViewEarliestNextOrderDate.text = formattedEarliestDate
        }
        return orderSavedFragment
    }
}