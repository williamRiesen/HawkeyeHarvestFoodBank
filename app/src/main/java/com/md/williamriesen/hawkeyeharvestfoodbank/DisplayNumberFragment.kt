package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders


class DisplayNumberFragment : Fragment() {

lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val displayNumberFragment =
            inflater.inflate(R.layout.fragment_display_number, container, false)
        val textViewAccountID= displayNumberFragment.findViewById<TextView>(R.id.textViewAccountIdBig)
        textViewAccountID.text = viewModel.accountNumberForDisplay
        return displayNumberFragment
    }
}