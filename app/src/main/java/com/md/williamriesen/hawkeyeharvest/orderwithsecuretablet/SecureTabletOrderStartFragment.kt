package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.orderonsite.OnSiteOrderingViewModel

class SecureTabletOrderStartFragment : Fragment() {

    lateinit var viewModel: SecureTabletOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(SecureTabletOrderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_secure_tablet_order_start, container, false)
        val buttonGo = fragment.findViewById<Button>(R.id.buttonGo)
        val editTextAccountNumber = fragment.findViewById<EditText>(R.id.editTextAccountNumber)
        buttonGo.setOnClickListener {
            viewModel.lookUpAccount(editTextAccountNumber.text.toString().toInt(), requireContext())
        }
        return fragment
    }


}