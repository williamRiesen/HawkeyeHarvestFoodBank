package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation


class ReviseSavedOrderOptionFragment : Fragment() {

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
        val reviseSavedOrderOptionFragment = inflater.inflate(R.layout.fragment_revise_saved_order_option, container, false)
        val buttonExit = reviseSavedOrderOptionFragment.findViewById<Button>(R.id.button_exit)
        buttonExit.setOnClickListener {
            activity?.onBackPressed()
        }
        val buttonShop = reviseSavedOrderOptionFragment.findViewById<Button>(R.id.button_shop)
        buttonShop.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_reviseSavedOrderOptionFragment_to_selectionFragment)
            Log.d("TAG", "out of stock list is not empty: ${viewModel.outOfStockNameList.value!!.isNotEmpty()}")
            if (viewModel.outOfStockNameList.value!!.isNotEmpty()){
                Navigation.findNavController(it).navigate(R.id.action_selectionFragment_to_outOfStockFragment)
            }
        }

        return reviseSavedOrderOptionFragment
    }

}