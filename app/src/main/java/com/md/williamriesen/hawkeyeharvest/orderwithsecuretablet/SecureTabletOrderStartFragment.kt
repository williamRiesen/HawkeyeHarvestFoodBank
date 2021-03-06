package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.reports.ReportCreator

class SecureTabletOrderStartFragment : Fragment() {

    lateinit var viewModel: SecureTabletOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TAG","onCreateStarted")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(SecureTabletOrderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "Starting onCreateView")
        val fragment =
            inflater.inflate(R.layout.fragment_secure_tablet_order_start, container, false)
        val buttonGo = fragment.findViewById<Button>(R.id.buttonGo)
        val editTextAccountNumber = fragment.findViewById<EditText>(R.id.editTextAccountNumber)
        editTextAccountNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
//                val accountNumber = editTextAccountNumber.text.toString().toIntOrNull()
//                if (accountNumber != null) {
//                    viewModel.go(
//                        accountNumber, requireView()
//                    )
//                } else {
//                    Toast.makeText(
//                        context, "Please provide account number.", Toast.LENGTH_LONG
//                    ).show()
//                    editTextAccountNumber.requestFocus()
//                }
            }
            false
        }

        buttonGo.setOnClickListener {
            hideKeyboard()
            val accountNumber = editTextAccountNumber.text.toString().toIntOrNull()
            if (accountNumber != null) {
                viewModel.go(
                    accountNumber, requireView()
                )
            } else {
                Toast.makeText(
                    context, "Please provide account number.", Toast.LENGTH_LONG
                ).show()
                editTextAccountNumber.requestFocus()
            }
        }
        val buttonAddEditAccount = fragment.findViewById<Button>(R.id.buttonNewAccount)
        buttonAddEditAccount.setOnClickListener {
            viewModel.accountNumber = null
            Navigation.findNavController(it)
                .navigate(R.id.action_secureTabletOrderStartFragment_to_addEditAccountFragment2)
        }

        val buttonInventory = fragment.findViewById<Button>(R.id.buttonInventory)
        buttonInventory.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_secureTabletOrderStartFragment_to_updateInventoryFragment2)
        }
        Log.d("TAG", "startupAccountNumber: ${viewModel.startupAccountNumber}")
        if (viewModel.startupAccountNumber != null) {
            editTextAccountNumber.setText(viewModel.startupAccountNumber.toString())
        }
        val buttonCreateMonthReport = fragment.findViewById<Button>(R.id.buttonCreateMonthReport)
        buttonCreateMonthReport.setOnClickListener {
            ReportCreator().createMonthReport()
        }
        val buttonEditAccount = fragment.findViewById<Button>(R.id.buttonEditAccount)
        buttonEditAccount.setOnClickListener {
            val accountNumber = editTextAccountNumber.text.toString().toIntOrNull()
            if (accountNumber != null) {
                viewModel.accountNumber = accountNumber
                Navigation.findNavController(it)
                    .navigate(R.id.action_secureTabletOrderStartFragment_to_addEditAccountFragment2)
            } else {
                Toast.makeText(
                    context, "Please provide account number.", Toast.LENGTH_LONG
                ).show()
                editTextAccountNumber.requestFocus()
            }
        }
        val buttonReset =
            fragment.findViewById<Button>(R.id.buttonReset)
        buttonReset.setOnClickListener {
            val accountNumber = editTextAccountNumber.text.toString().toIntOrNull()
            if (accountNumber != null) {
                viewModel.resetOrder(accountNumber, requireView())
            } else {
                Toast.makeText(
                    context, "Please provide account number.", Toast.LENGTH_LONG
                ).show()
                editTextAccountNumber.requestFocus()
            }
        }

        val buttonBreadAndSweets = fragment.findViewById<Button>(R.id.buttonBreadAndSweets)
        buttonBreadAndSweets.setOnClickListener{
            val accountNumber = editTextAccountNumber.text.toString().toIntOrNull()
            if (accountNumber != null) {
                viewModel.breadAndSweets(accountNumber, requireView())
            } else {
                Toast.makeText(
                    context, "Please provide account number.", Toast.LENGTH_LONG
                ).show()
                editTextAccountNumber.requestFocus()
            }
        }
        return fragment
    }
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}