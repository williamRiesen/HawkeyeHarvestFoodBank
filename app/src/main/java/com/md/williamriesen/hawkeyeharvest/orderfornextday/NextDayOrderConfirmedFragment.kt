package com.md.williamriesen.hawkeyeharvest.orderfornextday

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.communication.DisplayNumberActivity
import com.md.williamriesen.hawkeyeharvest.R


class NextDayOrderConfirmedFragment : Fragment() {

    lateinit var viewModel: NextDayOrderingActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(NextDayOrderingActivityViewModel::class.java)
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
        textViewPickUpInstructions.text = "Please go to the food bank between $startOfWindow PM and $endOfWindow PM tomorrow to pick up your order."
        val buttonShowNumber = fragment.findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.setOnClickListener {
//            Navigation.findNavController(it)
//                .navigate(R.id.action_nextDayOrderConfirmedFragment_to_displayNumberFragment2)
            val intent = Intent(context, DisplayNumberActivity::class.java)
            intent.putExtra("ACCOUNT_ID", viewModel.accountID)
            startActivity(intent)

        }
        return fragment
    }


}