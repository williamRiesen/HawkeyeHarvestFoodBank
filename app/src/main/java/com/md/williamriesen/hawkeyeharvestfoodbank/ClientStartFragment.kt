package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders


class ClientStartFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
//        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        Log.d("TAG", " ClientStartActivity viewModel.accountID: ${viewModel.accountID}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val clientStartFragment = inflater.inflate(R.layout.fragment_client_start, container, false)
        val textViewAccountID = clientStartFragment.findViewById<TextView>(R.id.textViewAccountID)
        textViewAccountID.text = viewModel.accountID
        return clientStartFragment
    }

}