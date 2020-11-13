package com.md.williamriesen.hawkeyeharvestfoodbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class AskIfOnSiteFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        Log.d("TAG", "accountId from AskIfOnSiteFragment: ${viewModel.accountID}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_ask_if_on_site, container, false)
        val buttonYes = fragment.findViewById<Button>(R.id.buttonYes)
        buttonYes.setOnClickListener {
            viewModel.clientIsOnSite = true
            Log.d("TAG", "viewModel.accountID:  ${viewModel.accountID}")
            viewModel.signIn(viewModel.accountID,viewModel.clientIsOnSite,it.context)
        }
        val buttonNo = fragment.findViewById<Button>(R.id.buttonNo)
        buttonNo.setOnClickListener {
            viewModel.clientIsOnSite = false
            Log.d("TAG", "viewModel.accountID:  ${viewModel.accountID}")
            viewModel.signIn(viewModel.accountID,viewModel.clientIsOnSite,it.context)
        }
        return fragment
    }
}