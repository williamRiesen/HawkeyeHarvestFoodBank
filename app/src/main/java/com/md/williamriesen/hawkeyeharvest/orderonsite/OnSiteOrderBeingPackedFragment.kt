package com.md.williamriesen.hawkeyeharvest.orderonsite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R


class OnSiteOrderBeingPackedFragment : Fragment() {
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
        val orderReadyFragment = inflater.inflate(R.layout.fragment_order_being_packed, container, false)
        val buttonShowNumber = orderReadyFragment.findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.visibility = View.INVISIBLE
        val textViewMessage = orderReadyFragment.findViewById<TextView>(R.id.textViewOrderBeingPackedMessage)
        textViewMessage.text = "Your order has been saved and will be packed shortly. Someone will bring it out to you when it is ready. Health and best wishes from the Hawkeye Harvest Food Bank!"

        return orderReadyFragment
    }
}