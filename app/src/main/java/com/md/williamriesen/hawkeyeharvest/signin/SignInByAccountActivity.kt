package com.md.williamriesen.hawkeyeharvest.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.HawkeyeHarvestFoodBankApplication
import com.md.williamriesen.hawkeyeharvest.R
import javax.inject.Inject

class SignInByAccountActivity : AppCompatActivity() {
    @Inject lateinit var accountService: AccountService;
    lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as HawkeyeHarvestFoodBankApplication).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        // Work around for dependency injection
        viewModel.accountService = accountService

        setContentView(R.layout.activity_login_by_account)
    }
}