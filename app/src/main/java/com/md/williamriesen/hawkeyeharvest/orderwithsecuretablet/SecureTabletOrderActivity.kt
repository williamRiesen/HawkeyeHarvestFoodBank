package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R

class SecureTabletOrderActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureTabletOrderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secure_tablet_order)

        viewModel = ViewModelProviders.of(this).get(SecureTabletOrderViewModel::class.java)
        val startupAccountNumberString = intent.extras?.get("accountNumber").toString()
        Log.d("TAG", "startupAccountNumberString: $startupAccountNumberString")
//        viewModel.startupAccountNumber = startupAccountNumberString?.toIntOrNull()
    }

//    fun onCartButtonClick(view: View) {
//        Navigation.findNavController(view)
//            .navigate(R.id.action_secureTabletOrderSelectionFragment_to_secureTabletOrderCheckoutFragment)
//    }
}