package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.orderonsite.OnSiteOrderingViewModel

class SecureTabletOrderStartFragment : Fragment() {

    lateinit var viewModel: SecureTabletOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(SecureTabletOrderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment =
            inflater.inflate(R.layout.fragment_secure_tablet_order_start, container, false)
        val buttonGo = fragment.findViewById<Button>(R.id.buttonGo)
        val editTextAccountNumber = fragment.findViewById<EditText>(R.id.editTextAccountNumber)
        editTextAccountNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.lookUpAccount(
                    editTextAccountNumber.text.toString().toInt(), requireContext(),
                    requireView()
                )
            }
            false
        }

        buttonGo.setOnClickListener {
            viewModel.lookUpAccount(
                editTextAccountNumber.text.toString().toInt(), requireContext(),
                requireView()
            )
        }
        val buttonAddEditAccount = fragment.findViewById<Button>(R.id.buttonNewAccount)
        buttonAddEditAccount.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_secureTabletOrderStartFragment_to_addEditAccountFragment2)
        }
        val buttonUpdateNumbers = fragment.findViewById<Button>(R.id.buttonRunNumberUpdate)
        buttonUpdateNumbers.setOnClickListener {
            viewModel.updateNumbers()
        }
        Log.d("TAG", "startupAccountNumber: ${viewModel.startupAccountNumber}")
        if (viewModel.startupAccountNumber != null) {
            editTextAccountNumber.setText(viewModel.startupAccountNumber.toString())
        }
        return fragment
    }
}