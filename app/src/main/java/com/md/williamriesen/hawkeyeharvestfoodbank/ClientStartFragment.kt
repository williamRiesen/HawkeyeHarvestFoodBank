package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation


class ClientStartFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val clientStartFragment = inflater.inflate(R.layout.fragment_client_start, container, false)
        val textViewAccountID = clientStartFragment.findViewById<TextView>(R.id.textViewAccountID)
        val textViewFamilySize = clientStartFragment.findViewById<TextView>(R.id.textViewFamilySize)

        val buttonShop = clientStartFragment.findViewById<Button>(R.id.buttonShop)
        textViewAccountID.text = viewModel.accountID
        textViewFamilySize.text = viewModel.familySize.toString()

        buttonShop.setOnClickListener(){
            Navigation.findNavController(requireView()).navigate(R.id.action_clientStartFragment_to_selectionFragment)
        }
        return clientStartFragment
    }


}