package com.md.williamriesen.hawkeyeharvest.orderonsite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvest.R


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
