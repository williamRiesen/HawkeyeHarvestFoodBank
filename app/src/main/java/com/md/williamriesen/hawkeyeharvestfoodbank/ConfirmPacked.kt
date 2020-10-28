package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_confirm_packed.*


class ConfirmPacked : Fragment() {

    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val conFirmPackedFragment = inflater.inflate(R.layout.fragment_confirm_packed, container, false)
        val buttonConfirmPacked = conFirmPackedFragment.findViewById<Button>(R.id.buttonConfirmPacked)
        buttonConfirmPacked.setOnClickListener{
            viewModel.upDateOrderAsPacked(requireActivity())
        }
        val textViewOrderNumber = conFirmPackedFragment.findViewById<TextView>(R.id.textViewOrderNumber)
        textViewOrderNumber.text = viewModel.accountNumberForDisplay
        return conFirmPackedFragment
    }

}