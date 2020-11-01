package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class LoginByAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_account)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.isOpen.value = true
        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        editTextAccountID.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                onShopButtonClick()
            }
            false
        }
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val label = findViewById<TextView>(R.id.textViewAccountIDLabel)
        val pleaseWaitObserver = Observer<Boolean> {
            if(it){
                progressBar.visibility = View.VISIBLE
                editTextAccountID.visibility = View.INVISIBLE
                label.visibility = View.INVISIBLE
            }
            else{
                progressBar.visibility = View.INVISIBLE
                editTextAccountID.visibility = View.VISIBLE
                label.visibility = View.VISIBLE
            }
        }
        viewModel.pleaseWait.observe(this,pleaseWaitObserver)
        val buttonNext = this.findViewById<Button>(R.id.buttonOK)
        buttonNext.setOnClickListener {
            onShopButtonClick()
        }
    }

    override fun onResume() {
        super.onResume()
        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        if (editTextAccountID.text != null && editTextAccountID.text.toString() != ""){
            val buttonNext = findViewById<Button>(R.id.buttonOK)
            buttonNext.visibility = View.VISIBLE
        }
    }

    private fun onShopButtonClick() {
        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        val accountID = editTextAccountID.text.toString()
        if (accountID != null && accountID != "") {
            viewModel.signIn(accountID, applicationContext)
        }
    }

    override fun onRestart() {
        super.onRestart()
        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        val label = findViewById<TextView>(R.id.textViewAccountIDLabel)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        editTextAccountID.visibility = View.VISIBLE
        label.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }
}