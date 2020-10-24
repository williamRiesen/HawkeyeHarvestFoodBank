package com.md.williamriesen.hawkeyeharvestfoodbank

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import java.text.DateFormat
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.google.protobuf.Value


class ClientStartFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)

        val foodBank = FoodBank()
        viewModel.isOpen.value= foodBank.isOpen
//         viewModel.sendObjectCatalogToFireStore()
//         viewModel.sendCategoriesListToFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val clientStartFragment = inflater.inflate(R.layout.fragment_client_start, container, false)
        val textViewAccountID = clientStartFragment.findViewById<TextView>(R.id.textViewAccountID)
        val textViewFamilySize = clientStartFragment.findViewById<TextView>(R.id.textViewFamilySize)
        val textViewLastOrderDate =
            clientStartFragment.findViewById<TextView>(R.id.textViewLastOrderDate)
        val textViewEarliestNextOrderDate =
            clientStartFragment.findViewById<TextView>(R.id.textViewEarliestNextOrderDate)
        val textViewSuggestedNextOrderDate =
            clientStartFragment.findViewById<TextView>(R.id.textViewSuggestedNextOrderDate)
        val textViewEarliestNextOrderLabel =
            clientStartFragment.findViewById<TextView>(R.id.textViewEarliestNextOrderDateLabel)
        val textViewOpenOrClosed = clientStartFragment.findViewById<TextView>(R.id.textViewOpenOrClosed)
        val openOrClosedObserver = Observer<Boolean> { isOpen  ->
            Log.d("TAG", "observer fired; isOpen = ${viewModel.isOpen.value}")
            if (isOpen){
                textViewOpenOrClosed.text = "The food bank is OPEN and can take orders until 3:30 PM."
                textViewOpenOrClosed.setTextColor(Color.parseColor("#199115"))
            } else{
                textViewOpenOrClosed.text = "The food bank is CLOSED. You may shop and save your order now. Return to this app to place the order after 12 Noon on:"
                textViewOpenOrClosed.setTextColor(Color.RED)
            }
        }
        viewModel.isOpen.observe(viewLifecycleOwner,openOrClosedObserver)
        val buttonShop = clientStartFragment.findViewById<Button>(R.id.buttonShop)
        textViewAccountID.text = viewModel.accountID
        textViewFamilySize.text = viewModel.familySize.toString()

        val formattedDate = DateFormat.getDateInstance().format(viewModel.lastOrderDate)
        textViewLastOrderDate.text = formattedDate

        val formattedEarliestDate =
            DateFormat.getDateInstance().format(viewModel.earliestOrderDate)
        textViewEarliestNextOrderDate.text = formattedEarliestDate

        if (viewModel.canOrderNow) {
            textViewEarliestNextOrderDate.visibility = View.GONE
            textViewEarliestNextOrderLabel.visibility = View.GONE
        } else {
            textViewEarliestNextOrderDate.visibility = View.VISIBLE
            textViewEarliestNextOrderLabel.visibility = View.VISIBLE
        }

        val formattedSuggestedDate =
            DateFormat.getDateInstance().format(viewModel.suggestedNextOrderDate)
        textViewSuggestedNextOrderDate.text = formattedSuggestedDate

        buttonShop.setOnClickListener {
            if (viewModel.outOfStockNameList.value!!.isEmpty()) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_clientStartFragment_to_selectionFragment)
            } else {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_clientStartFragment_to_outOfStockFragment)
            }
        }

        val buttonTest = clientStartFragment.findViewById<Button>(R.id.buttonTest)
        buttonTest.setOnClickListener {
            viewModel.isOpen.value = !viewModel.isOpen.value!!
        }

        return clientStartFragment
    }
}