package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.orderonsite.OnSiteOrderingViewModel

class SecureTabletOrderActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureTabletOrderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secure_tablet_order)
    }

    fun onCartButtonClick(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_secureTabletOrderSelectionFragment_to_secureTabletOrderCheckoutFragment)
    }
}