package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat


class AskWhetherToSubmitSavedOrderFragment : Fragment() {
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
        val askWhetherToSubmitFragment = inflater.inflate(R.layout.fragment_ask_whether_to_submit_saved_order, container, false)
        val buttonNo = askWhetherToSubmitFragment.findViewById<Button>(R.id.buttonNo)
        buttonNo.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_askWhetherToSubmitSavedOrderFragment_to_orderSavedFragment)
        }
        val buttonYes = askWhetherToSubmitFragment.findViewById<Button>(R.id.buttonYes)
        buttonYes.setOnClickListener{
            viewModel.submitOrder(it)
        }
        return askWhetherToSubmitFragment
    }
}