package com.md.williamriesen.hawkeyeharvestfoodbank.orderoffsite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvestfoodbank.R


class ShopOrCheckOutFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_shop_vs_check_out, container, false)
        val buttonExit = fragment.findViewById<Button>(R.id.button_exit)
        buttonExit.setOnClickListener {
            activity?.onBackPressed()
        }
        val buttonYes = fragment.findViewById<Button>(R.id.button_yes)
        buttonYes.setOnClickListener {
            if (viewModel.outOfStockNameList.value!!.isEmpty()) {
                Navigation.findNavController(it)
                    .navigate(R.id.action_shopVsCheckOutFragment_to_checkoutFragment)
            } else {
                Navigation.findNavController(it)
                    .navigate(R.id.action_shopVsCheckOutFragment_to_selectionFragment)
                Navigation.findNavController(it)
                    .navigate(R.id.action_selectionFragment_to_outOfStockFragment)
            }
        }
        val buttonShop = fragment.findViewById<Button>(R.id.button_shop)
        buttonShop.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_shopVsCheckOutFragment_to_selectionFragment)
            if (viewModel.outOfStockNameList.value!!.isNotEmpty()) {
                Navigation.findNavController(it)
                    .navigate(R.id.action_selectionFragment_to_outOfStockFragment)
            }
        }
        return fragment
    }
}