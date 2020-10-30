package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class ReviseSavedOrderOptionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        }

        return reviseSavedOrderOptionFragment
    }

}