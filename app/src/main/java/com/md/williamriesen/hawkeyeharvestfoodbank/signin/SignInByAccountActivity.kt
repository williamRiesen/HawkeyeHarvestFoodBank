package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.messaging.FirebaseMessaging
import com.md.williamriesen.hawkeyeharvestfoodbank.R

class SignInByAccountActivity : AppCompatActivity() {

    lateinit var viewModel: SignInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        setContentView(R.layout.activity_login_by_account)
    }
}