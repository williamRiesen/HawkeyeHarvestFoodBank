package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.md.williamriesen.hawkeyeharvestfoodbank.R

class AskIfOnSiteFragment : Fragment() {

    lateinit var viewModel: SignInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)
        Log.d("TAG", "accountId from AskIfOnSiteFragment: ${viewModel.accountID}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_ask_if_on_site, container, false)
        val buttonYes = fragment.findViewById<Button>(R.id.buttonYes)
        buttonYes.setOnClickListener {
            activity?.onBackPressed()
            viewModel.clientIsOnSite = true
            Log.d("TAG", "viewModel.accountID:  ${viewModel.accountID}")
            viewModel.signIn(viewModel.accountID,viewModel.clientIsOnSite,it.context)
        }
        val buttonNo = fragment.findViewById<Button>(R.id.buttonNo)
        buttonNo.setOnClickListener {
            viewModel.clientIsOnSite = false
            Log.d("TAG", "viewModel.accountID:  ${viewModel.accountID}")

            viewModel.signIn(viewModel.accountID,viewModel.clientIsOnSite,it.context)
        }
        return fragment
    }
}