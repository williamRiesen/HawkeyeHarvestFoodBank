package com.md.williamriesen.hawkeyeharvest.orderoffsite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvest.R


class ClientStartFragment() : Fragment() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val foodBank = FoodBank()
        foodBank.getCurrentDateWithoutTime()
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
//        viewModel.isOpen.value = foodBank.isOpen
//         foodBank.sendCategoriesListToFireStore()
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val clientStartFragment =
                inflater.inflate(R.layout.fragment_client_start, container, false)
            val textViewAccountID =
                clientStartFragment.findViewById<TextView>(R.id.textViewAccountID)
            val textViewFamilySize =
                clientStartFragment.findViewById<TextView>(R.id.textViewFamilySize)
            val textViewLastOrderDate =
                clientStartFragment.findViewById<TextView>(R.id.textViewLastOrderDate)
            val textViewSuggestedNextOrderDate =
                clientStartFragment.findViewById<TextView>(R.id.textViewSuggestedNextOrderDate)

            val buttonShop = clientStartFragment.findViewById<Button>(R.id.buttonShop)
            textViewAccountID.text = viewModel.accountID
            textViewFamilySize.text = viewModel.familySize.toString()

//            val formattedDate = DateFormat.getDateInstance().format(viewModel.lastOrderDate)
//            textViewLastOrderDate.text = formattedDate

//            val formattedSuggestedDate =
//                DateFormat.getDateInstance().format(viewModel.suggestedNextOrderDate)
//            textViewSuggestedNextOrderDate.text = formattedSuggestedDate

            buttonShop.setOnClickListener {
                viewModel.shop(it)

            }

            val textViewNextDayOpen =
                clientStartFragment.findViewById<TextView>(R.id.textViewNextDayOpen)
//            val formattedNextDayOpen =
//                DateFormat.getDateInstance().format(viewModel.nextDayOpen)
//            textViewNextDayOpen.text = formattedNextDayOpen

            val textViewOrderState = clientStartFragment.findViewById<TextView>(R.id.textViewOrderState)
            textViewOrderState.text = viewModel.orderState.value
            val textViewIsOpen = clientStartFragment.findViewById<TextView>(R.id.textViewIsOpen)

            val isOpenObserver = Observer<Boolean> {isOpen ->
                if (isOpen) textViewIsOpen.text = "The food bank is currently open to pack and pick up online orders."
                else textViewIsOpen.text = "The food bank is currently closed, but you can work online with this app."
            }
            viewModel.isOpen.observe(viewLifecycleOwner, isOpenObserver)

            val buttonNext = clientStartFragment.findViewById<Button>(R.id.buttonNext)
            buttonNext.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(viewModel.nextFragment)
            }
            return clientStartFragment
        }
    }
