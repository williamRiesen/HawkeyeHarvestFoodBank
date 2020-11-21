package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.md.williamriesen.hawkeyeharvestfoodbank.R


class SignInByAccountFragment : Fragment() {


    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)
//        viewModel.isOpen.value = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_login_by_account, container, false)

        val label = fragment.findViewById<TextView>(R.id.textViewAccountIDLabel)
        val editTextAccountID = fragment.findViewById<EditText>(R.id.editTextAccountID)
        editTextAccountID.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onShopButtonClick(requireView())
            }
            false
        }
        val progressBar = fragment.findViewById<ProgressBar>(R.id.progressBar)
        val pleaseWaitObserver = Observer<Boolean> {
            if (it) {
                progressBar.visibility = View.VISIBLE
                editTextAccountID.visibility = View.INVISIBLE
                label.visibility = View.INVISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
                editTextAccountID.visibility = View.VISIBLE
                label.visibility = View.VISIBLE
            }
        }
        viewModel.pleaseWait.observe(viewLifecycleOwner, pleaseWaitObserver)
        val buttonNext = fragment.findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener {
            onShopButtonClick(requireView())
        }
        return fragment
    }


    override fun onResume() {
        super.onResume()
        val editTextAccountID = requireView().findViewById<EditText>(R.id.editTextAccountID)
        if (editTextAccountID.text != null && editTextAccountID.text.toString() != "") {
            val buttonNext = requireView().findViewById<Button>(R.id.buttonNext)
            buttonNext.visibility = View.VISIBLE
        }
    }

    private fun onShopButtonClick(view: View) {
        val editTextAccountID = view.findViewById<EditText>(R.id.editTextAccountID)
        val accountID = editTextAccountID.text.toString()
        if (accountID != null && accountID != "") {
            viewModel.accountID = accountID
//            if (accountID == "STAFF"){
                            viewModel.retrieveClientInformation(accountID,view,requireContext())
//            } else {
//                viewModel.determineClientLocation(accountID, view, requireContext())
//            }
        }
    }

//    override fun onRestart() {
//        super.onRestart()
//        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
//        val label = findViewById<TextView>(R.id.textViewAccountIDLabel)
//        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
//        editTextAccountID.visibility = View.VISIBLE
//        label.visibility = View.VISIBLE
//        progressBar.visibility = View.INVISIBLE
//    }

}