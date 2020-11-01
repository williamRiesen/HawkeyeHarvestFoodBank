package com.md.williamriesen.hawkeyeharvestfoodbank

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import java.text.DateFormat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_client_start.*


class ClientStartFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
        val foodBank = FoodBank()
        val today = foodBank.getCurrentDateWithoutTime()
        viewModel.isOpen.value = foodBank.isOpen
        Log.d("TAG", "viewModel.isOpen.value ${viewModel.isOpen.value}")
//         viewModel.sendCategoriesListToFireStore()

        }


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            // Inflate the layout for this fragment
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
            val textViewOpenOrClosed =
                clientStartFragment.findViewById<TextView>(R.id.textViewOpenOrClosed)

            val eligibilityStatusObserver = Observer<String> { eligibilityStatus ->
                when (eligibilityStatus) {
                    "mayOrderNow" -> {
                        textViewOpenOrClosed.text =
                            "The food bank is OPEN and can take orders until 3:30 PM."
                        textViewOpenOrClosed.setTextColor(Color.parseColor("#199115"))
                        textViewNextDayOpen.visibility = View.GONE
                        textViewLastOrderDate.visibility = View.INVISIBLE
                        textViewLastOrderDateLabel.visibility = View.INVISIBLE
                    }
                    "mayOrderWhenOpen" -> {
                        textViewOpenOrClosed.text =
                            "You may choose your food now. Save your order by touching the grocery cart button. Please sign in again to activate your order when the food bank opens at 12 noon on"
                        textViewOpenOrClosed.setTextColor(Color.GRAY)
                        textViewLastOrderDateLabel.visibility = View.GONE
                        textViewLastOrderDate.visibility = View.GONE
                    }
                    else -> {
                        textViewOpenOrClosed.text =
                            "You may shop now for next month using this app. Save your order when you are done. Our records show you have ordered already this month. Please sign back in on the first weekday of next month to place your saved order. "
                        textViewOpenOrClosed.setTextColor(Color.BLUE)
                        textViewNextDayOpen.visibility = View.INVISIBLE
                    }

                }
            }
//        val openOrClosedObserver = Observer<Boolean> { isOpen  ->
//            Log.d("TAG", "observer fired; isOpen = ${viewModel.isOpen.value}")
//            if (isOpen){
//                textViewOpenOrClosed.text = "The food bank is OPEN and can take orders until 3:30 PM."
//                textViewOpenOrClosed.setTextColor(Color.parseColor("#199115"))
//            } else{
//                textViewOpenOrClosed.text = "You may shop NOW using this app. Save your order when you are done. The food bank is closed now. Please sign in again to place your saved order when the food bank reopens at 12 noon on"
//
//                textViewOpenOrClosed.setTextColor(Color.GRAY)
//            }
//        }
//            viewModel.eligibilityStatus.observe(viewLifecycleOwner, eligibilityStatusObserver)

            val buttonShop = clientStartFragment.findViewById<Button>(R.id.buttonShop)
            textViewAccountID.text = viewModel.accountID
            textViewFamilySize.text = viewModel.familySize.toString()

            val formattedDate = DateFormat.getDateInstance().format(viewModel.lastOrderDate)
            textViewLastOrderDate.text = formattedDate

            val formattedEarliestDate =
                DateFormat.getDateInstance().format(viewModel.earliestOrderDate)
//            textViewEarliestNextOrderDate.text = formattedEarliestDate

            if (viewModel.mayOrderNow) {
//                textViewEarliestNextOrderDate.visibility = View.GONE
//                textViewEarliestNextOrderLabel.visibility = View.GONE
            } else {
//                textViewEarliestNextOrderDate.visibility = View.VISIBLE
//                textViewEarliestNextOrderLabel.visibility = View.VISIBLE
            }

            val formattedSuggestedDate =
                DateFormat.getDateInstance().format(viewModel.suggestedNextOrderDate)
            textViewSuggestedNextOrderDate.text = formattedSuggestedDate

            buttonShop.setOnClickListener {
                if (viewModel.outOfStockNameList.value!!.isEmpty()) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_clientStartFragment_to_instructionsFragment)
                } else {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_clientStartFragment_to_outOfStockFragment)
                }
            }

//            val buttonTest = clientStartFragment.findViewById<Button>(R.id.buttonShowAccountID)
//            buttonTest.setOnClickListener {
//                Navigation.findNavController(it)
//                    .navigate(R.id.action_clientStartFragment_to_displayNumberFragment)
//            }

            val textViewNextDayOpen =
                clientStartFragment.findViewById<TextView>(R.id.textViewNextDayOpen)
            val formattedNextDayOpen =
                DateFormat.getDateInstance().format(viewModel.nextDayOpen)
            textViewNextDayOpen.text = formattedNextDayOpen

//            val orderStateObserver = Observer<String> {
//                if (it == "PACKED"){
//                    Log.d("TAG", "About to navigate to ORder ready.")
//                    Navigation.findNavController(requireView()).navigate(R.id.action_clientStartFragment_to_orderReadyFragment)
//                } else if (it =="SUBMITTED")
//                    Navigation.findNavController(requireView()).navigate(R.id.action_clientStartFragment_to_orderBeingPackedFragment)
//            }
//            viewModel.orderState.observe(viewLifecycleOwner,orderStateObserver)

            val textViewOrderState = clientStartFragment.findViewById<TextView>(R.id.textViewOrderState)
            textViewOrderState.text = viewModel.orderState.value
            val textViewIsOpen = clientStartFragment.findViewById<TextView>(R.id.textViewIsOpen)

            val isOpenObserver = Observer<Boolean> {isOpen ->
                if (isOpen) textViewIsOpen.text = "The food bank is currently open to pack and pick up online orders."
                else textViewIsOpen.text = "The food bank is currently closed, but you can work online with this app."
            }
            viewModel.isOpen.observe(viewLifecycleOwner, isOpenObserver)

            val buttonOK = clientStartFragment.findViewById<Button>(R.id.buttonOK)
            buttonOK.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(viewModel.nextFragment)
            }
            return clientStartFragment

        }
    }
