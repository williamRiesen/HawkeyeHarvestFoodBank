package com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvestfoodbank.FoodBank
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import java.text.DateFormat


class OnSiteOrderStartFragment : Fragment() {

    lateinit var viewModel: OnSiteOrderingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(OnSiteOrderingViewModel::class.java)
        val foodBank = FoodBank()
        foodBank.getCurrentDateWithoutTime()
        viewModel.isOpen.value = foodBank.isOpen
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val fragment =
                inflater.inflate(R.layout.fragment_client_start, container, false)

            val buttonNext = fragment.findViewById<Button>(R.id.buttonNext)
            buttonNext.setOnClickListener {
                viewModel.navigateToNextFragment(it)
            }
            return fragment
        }
    }
