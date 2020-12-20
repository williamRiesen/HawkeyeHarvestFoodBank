package com.md.williamriesen.hawkeyeharvest.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R


class AddEditAccountFragment : Fragment() {

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
            inflater.inflate(R.layout.fragment_add_edit_account, container, false)
        val editTextAccountID = fragment.findViewById<EditText>(R.id.editTextAccountID)
        val editTextFamilySize = fragment.findViewById<EditText>(R.id.editTextFamilySize)
        val editTextCity = fragment.findViewById<EditText>(R.id.editTextCity)
        val editTextCounty = fragment.findViewById<EditText>(R.id.editTextCounty)
        val buttonSubmit = fragment.findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            viewModel.submitAccount(
                editTextAccountID.text.toString(),
                editTextFamilySize.text.toString(),
                editTextCity.text.toString(),
                editTextCounty.text.toString(),
                requireContext()
            )
        }
        return fragment
    }


}