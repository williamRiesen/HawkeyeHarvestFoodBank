package com.md.williamriesen.hawkeyeharvest.orderonsite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.communication.DisplayNumberActivity


class OnSiteOrderConfirmedFragment : Fragment() {

    lateinit var viewModel: OnSiteOrderingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(OnSiteOrderingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_next_day_order_confirmed, container, false)
        val textViewPickUpInstructions = fragment.findViewById<TextView>(R.id.textViewPickUpInstructions)
        textViewPickUpInstructions.text = "Please show this screen to the staff so they know you have finished your order."
        val textViewPickUpInstructions2 = fragment.findViewById<TextView>(R.id.textViewPickUpInstructions2)
        textViewPickUpInstructions2.text = "Your order number is ${viewModel.accountID}"
        val buttonShowNumber = fragment.findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.setOnClickListener {
            val intent = Intent(context,DisplayNumberActivity::class.java)
            intent.putExtra("ACCOUNT_ID", viewModel.accountID)
            context?.startActivity(intent)
        }
        return fragment
    }


}