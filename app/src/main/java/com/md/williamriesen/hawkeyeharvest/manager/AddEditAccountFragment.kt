package com.md.williamriesen.hawkeyeharvest.manager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.ZipCodeIndex
import kotlinx.android.synthetic.main.activity_secure_tablet_order.*
import kotlinx.android.synthetic.main.fragment_add_edit_account.*


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
        val editTextZipCode = fragment.findViewById<EditText>(R.id.editTextZipCode)
        editTextZipCode.setOnEditorActionListener { textView, i, keyEvent ->
            val zip = textView.text.toString().toIntOrNull()
            editTextCity.setText(
                if (zip != null) {
                    ZipCodeIndex().lookUpCity(zip)
                } else {
                    ""
                }
            )
            editTextCounty.setText(
                if (zip != null) {
                    ZipCodeIndex().lookUpCounty(zip)
                } else {
                    ""
                }
            )
            false
        }
        val progressBar = fragment.findViewById<ProgressBar>(R.id.progressBarAccountAddEdit)
        val pleaseWaitObserver = Observer<Boolean> {
            if (it) {
                progressBar.visibility = View.VISIBLE
                buttonSubmit.visibility = View.INVISIBLE
                textViewNoteLabel.visibility = View.INVISIBLE

            } else {
                progressBar.visibility = View.INVISIBLE
                buttonSubmit.visibility = View.VISIBLE
                textViewNoteLabel.visibility = View.VISIBLE
            }
        }
        viewModel.pleaseWait.observe(viewLifecycleOwner, pleaseWaitObserver)


        val buttonSubmit = fragment.findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            viewModel.pleaseWait.value = true
            viewModel.submitAccount(
                editTextAccountID.text.toString(),
                editTextFamilySize.text.toString(),
                editTextCity.text.toString(),
                editTextCounty.text.toString(),
                requireContext(),
                requireActivity()
            )
        }
        return fragment
    }

    override fun onStart() {
        super.onStart()
        val randomLetters = getRandomString(3)
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts")
            .orderBy("accountNumber", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener {querySnapshot ->
                val highestNumberSoFar = querySnapshot.documents[0].get("accountNumber") as Long
                val newNumber = highestNumberSoFar + 1
                val accountID = randomLetters + newNumber.toString()
                editTextAccountID.setText(accountID)
                editTextAccountID.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                Log.i("TAG",exception.toString())
            }
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}