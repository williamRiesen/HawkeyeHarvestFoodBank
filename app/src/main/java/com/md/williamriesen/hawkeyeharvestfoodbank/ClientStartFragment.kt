package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
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


class ClientStartFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
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
        val textViewEarliestNextOrderLabel = clientStartFragment.findViewById<TextView>(R.id.textViewEarliestNextOrderDateLabel)

        val buttonShop = clientStartFragment.findViewById<Button>(R.id.buttonShop)
        textViewAccountID.text = viewModel.accountID
        textViewFamilySize.text = viewModel.familySize.toString()

        val formattedDate = DateFormat.getDateInstance().format(viewModel.lastOrderDate);
        textViewLastOrderDate.text = formattedDate

        val formattedEarliestDate =
            DateFormat.getDateInstance().format(viewModel.earliestOrderDate);
        textViewEarliestNextOrderDate.text = formattedEarliestDate

        if (viewModel.canOrderNow) {
            textViewEarliestNextOrderDate.visibility = View.VISIBLE
            textViewEarliestNextOrderLabel.visibility = View.VISIBLE
        } else {
            textViewEarliestNextOrderDate.visibility = View.GONE
            textViewEarliestNextOrderLabel.visibility =View.GONE
        }

        val formattedSuggestedDate =
            DateFormat.getDateInstance().format(viewModel.suggestedNextOrderDate);
        textViewSuggestedNextOrderDate.text = formattedSuggestedDate

        buttonShop.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_clientStartFragment_to_selectionFragment)
        }
        return clientStartFragment
    }


}