package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.communication.DisplayNumberActivity
import com.md.williamriesen.hawkeyeharvest.pushPassword


class SecureTabletOrderConfirmAndReset : Fragment() {

    lateinit var viewModel: SecureTabletOrderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(SecureTabletOrderViewModel::class.java)
        requireActivity().onBackPressedDispatcher.addCallback(this){
            //empty body disables back button
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment =
            inflater.inflate(R.layout.fragment_secure_tablet_confirm_and_reset, container, false)
        val textViewPickUpInstructions =
            fragment.findViewById<TextView>(R.id.textViewPickUpInstructions)
        textViewPickUpInstructions.text =
            "Please return this tablet to food bank staff; they will place your order."
        val textViewPickUpInstructions2 =
            fragment.findViewById<TextView>(R.id.textViewPickUpInstructions2)
        textViewPickUpInstructions2.text = "Your order number is ${viewModel.account.accountID.takeLast(4)}"
        val buttonShowNumber = fragment.findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.setOnClickListener {
            val intent = Intent(context, DisplayNumberActivity::class.java)
            intent.putExtra("ACCOUNT_ID", viewModel.account.accountID)
            context?.startActivity(intent)
        }
        val editTextPushPassword = fragment.findViewById<EditText>(R.id.editTextPushPassword)
        editTextPushPassword.setOnEditorActionListener { textView, i, keyEvent ->
            Log.d("TAG","text entered: ${textView.text}")
            if (textView.text.toString() == pushPassword) {
                Log.d("TAG","Now submitting...")
                val navigationAction = R.id.action_secureTabletOrderConfirmAndReset_to_outOfStockAtConfirmFragment
                viewModel.processOrder(requireView(),navigationAction)
            }
            false

        }
        return fragment
    }


}