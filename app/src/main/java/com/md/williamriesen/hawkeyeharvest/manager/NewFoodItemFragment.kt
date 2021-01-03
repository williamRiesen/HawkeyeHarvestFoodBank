package com.md.williamriesen.hawkeyeharvest.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R


class NewFoodItemFragment : Fragment() {

    private lateinit var viewModel: ManagerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(ManagerActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment =
         inflater.inflate(R.layout.fragment_new_food_item, container, false)
        val spinner: Spinner = fragment.findViewById(R.id.spinnerCategory)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        val editTextItemName = fragment.findViewById<EditText>(R.id.editTextName)
        val spinnerCategory = fragment.findViewById<Spinner>(R.id.spinnerCategory)
        val editTextPointValue = fragment.findViewById<EditText>(R.id.editTextPointValue)
        val editTextLimit = fragment.findViewById<EditText>(R.id.editTextLimit)
        val buttonSubmit = fragment.findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            viewModel.submitNewFoodItem(
                editTextItemName.text.toString(),
                spinnerCategory.selectedItem.toString(),
                editTextPointValue.text.toString(),
                editTextLimit.text.toString(),
                requireContext()
            )
        }
        return fragment
    }



}