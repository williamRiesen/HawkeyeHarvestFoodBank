package com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite

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
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import java.text.DateFormat
import java.util.*


class OnSiteOrderReadyFragment : Fragment() {
    lateinit var viewModel: OnSiteOrderingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(OnSiteOrderingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val orderReadyFragment = inflater.inflate(R.layout.fragment_order_ready, container, false)
        val buttonShowNumber = orderReadyFragment.findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.visibility = View.INVISIBLE
        val textViewOrderPackedMessage = orderReadyFragment.findViewById<TextView>(R.id.textViewOrderPackedMessage)
        textViewOrderPackedMessage.text = "Your order is ready. Please show this screen to Food Bank staff. Your order number is ${viewModel.accountNumberForDisplay}."


        return orderReadyFragment
    }
}