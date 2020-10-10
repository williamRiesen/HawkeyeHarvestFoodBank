package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class LoginByAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_account)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        editTextAccountID.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                onShopButtonClick(View(this))
            }
            false
        }
    }

    fun onShopButtonClick(view: View) {
        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        val accountID = editTextAccountID.text.toString()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        viewModel.signIn(accountID, applicationContext)
    }

    override fun onRestart() {
        super.onRestart()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
    }
}