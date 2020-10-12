package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat


class OrderSubmittedFragment : Fragment() {
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
        val orderSubmittedFragment = inflater.inflate(R.layout.fragment_order_submitted, container, false)
        val buttonOK = orderSubmittedFragment.findViewById<Button>(R.id.buttonOK2)
        buttonOK.setOnClickListener {
            Log.d("TAG", "OK button clicked.")
            activity?.onBackPressed()
        }
        return orderSubmittedFragment
    }



}