package com.md.williamriesen.hawkeyeharvest.manager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.Account
import com.md.williamriesen.hawkeyeharvest.foodbank.ZipCodeIndex
import com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet.SecureTabletOrderViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_account.*
import java.util.*


class AddEditAccountFragment : Fragment() {

    private lateinit var viewModel: SecureTabletOrderViewModel
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
            inflater.inflate(R.layout.fragment_add_edit_account, container, false)
        val textViewZipLabel = fragment.findViewById<TextView>(R.id.textViewZipCodeLabel)
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
                buttonSubmitNewAccount.visibility = View.INVISIBLE
                textViewNoteLabel.visibility = View.INVISIBLE

            } else {
                progressBar.visibility = View.INVISIBLE
                buttonSubmitNewAccount.visibility = View.VISIBLE
                textViewNoteLabel.visibility = View.VISIBLE
            }
        }
        viewModel.pleaseWait.observe(viewLifecycleOwner, pleaseWaitObserver)


        val buttonSubmitNewAccount = fragment.findViewById<Button>(R.id.buttonSubmitNewAccount)
        buttonSubmitNewAccount.setOnClickListener {
            viewModel.submitNewAccount(
                editTextAccountID.text.toString(),
                editTextFamilySize.text.toString(),
                editTextCity.text.toString(),
                editTextCounty.text.toString(),
                requireContext(),
                requireActivity()
            )
        }

        val buttonSubmitEditedAccount =
            fragment.findViewById<Button>(R.id.buttonSubmitEditedAccount)
        buttonSubmitEditedAccount.setOnClickListener {
            viewModel.pleaseWait.value = true

            viewModel.submitEditedAccount(
                editTextFamilySize.text.toString().toInt(),
                editTextCity.text.toString(),
                editTextCounty.text.toString(),
                requireContext(),
                requireActivity()
            )
        }

        return fragment
    }

    override fun onStart() {
        val db = FirebaseFirestore.getInstance()
        super.onStart()
        if (viewModel.currentAccountNumber != null) {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("accounts")
                .whereEqualTo("accountNumber", viewModel.currentAccountNumber)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    when (querySnapshot.size()) {
                        0 -> Toast.makeText(
                            context,
                            "No match found for this number.",
                            Toast.LENGTH_LONG
                        ).show()
                        1 -> {
                            val document = querySnapshot.documents[0]
                            Log.d("TAG", "document.   : ${document.data}")
                            val timestamp = document.get("lastOrderDate") as Timestamp
                            val date = Date(timestamp.seconds * 1000)
                            viewModel.account = Account(
                                document.id,
                                (document.get("familySize") as Long).toInt(),
                                document.get("city") as String,
                                document.get("county") as String,
                                (document.get("accountNumber") as Long).toInt(),
                                date,
                                document.get("lastOrderType") as String,
                                document.get("orderState") as String,
                                (document.get("pickUpHour24") as Long?)?.toInt() ?: 0
                            )
                            Log.d("TAG","account just created: ${viewModel.account!!.accountID}")
                            Log.d("TAG","account just created: ${viewModel.account!!.county}")
                            Log.d("TAG","account just created: ${viewModel.account!!.city}")
                            Log.d("TAG","account just created: ${viewModel.account!!.county}")
                            Log.d("TAG","account just created: ${viewModel.account!!.familySize}")
                            Log.d("TAG","account just created: ${viewModel.account!!.lastOrderType}")
                            Log.d("TAG","account just created: ${viewModel.account!!.pickUpHour24}")
                            Log.d("TAG","account just created: ${viewModel.account!!.lastOrderDate}")
                            Log.d("TAG","account just created: ${viewModel.account!!.accountNumber}")
                            Log.d("TAG","account just created: ${viewModel.account!!.orderState}")

                            buttonSubmitNewAccount.visibility = View.INVISIBLE
                            buttonSubmitEditedAccount.visibility = View.VISIBLE
                            editTextAccountID.setText(viewModel.account!!.accountID)
                            editTextAccountID.visibility = View.VISIBLE
                            editTextFamilySize.setText(viewModel.account!!.familySize.toString())
                            editTextCity.setText(viewModel.account!!.city)
                            editTextCounty.setText(viewModel.account!!.county)
                            editTextZipCode.visibility = View.GONE
                            textViewZipCodeLabel.visibility = View.GONE

                        }
                        else -> Toast.makeText(
                            context,
                            "Multiple matches: this should not be. Please contact Dr. Riesen.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


        } else {
            val randomLetters = getRandomString(3)
            buttonSubmitNewAccount.visibility = View.INVISIBLE
            buttonSubmitEditedAccount.visibility = View.VISIBLE

            db.collection("accounts")
                .orderBy("accountNumber", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val highestNumberSoFar = querySnapshot.documents[0].get("accountNumber") as Long
                    val newNumber = highestNumberSoFar + 1
                    val accountID = randomLetters + newNumber.toString()
                    editTextAccountID.setText(accountID)
                    editTextAccountID.visibility = View.VISIBLE
                }
                .addOnFailureListener { exception ->
                    Log.i("TAG", exception.toString())
                }
        }
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}