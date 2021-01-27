package com.md.williamriesen.hawkeyeharvest.volunteer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R
import kotlinx.android.synthetic.main.fragment_confirm_out_of_stock.*

class ConfirmOutOfStockFragment : Fragment() {

    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_confirm_out_of_stock, container, false)
        val outOfStockItemNameObserver = Observer<String> {outOfStockItemName ->
            textViewOutOfStockItemName.text = outOfStockItemName
        }
        viewModel.itemToMarkOutOfStock.observe(viewLifecycleOwner,outOfStockItemNameObserver)
        val buttonNo = fragment.findViewById<Button>(R.id.buttonNo)
        buttonNo.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val buttonYes = fragment.findViewById<Button>(R.id.buttonYes)
        buttonYes.setOnClickListener {
            viewModel.markOutOfStock(requireContext())
            requireActivity().onBackPressed()
        }
        return fragment
    }

}