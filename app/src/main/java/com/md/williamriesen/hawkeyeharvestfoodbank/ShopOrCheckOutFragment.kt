package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class ShopOrCheckOutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            Navigation.findNavController(it).navigate(R.id.action_shopVsCheckOutFragment_to_checkoutFragment)
        }
        val buttonShop = fragment.findViewById<Button>(R.id.button_shop)
        buttonShop.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_shopVsCheckOutFragment_to_selectionFragment)
        }
        return fragment
    }
}