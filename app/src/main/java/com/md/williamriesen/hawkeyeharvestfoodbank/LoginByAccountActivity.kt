package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders

class LoginByAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_account)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
//        viewModel.accountID  = intent.getStringExtra("EXTRA_ACCOUNT_INFO");
    }

    fun onShopButtonClick(view: View) {
        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
        val accountID = editTextAccountID.text.toString()
        viewModel.signIn(accountID, applicationContext, view)
    }
}