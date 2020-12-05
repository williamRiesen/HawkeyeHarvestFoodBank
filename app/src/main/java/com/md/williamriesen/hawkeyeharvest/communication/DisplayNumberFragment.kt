package com.md.williamriesen.hawkeyeharvest.communication

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.orderoffsite.MainActivityViewModel
import com.md.williamriesen.hawkeyeharvest.R


class DisplayNumberFragment : Fragment() {

lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Log.d("TAG", "Orientation request called")
        val displayNumberFragment =
            inflater.inflate(R.layout.fragment_display_number, container, false)
        val textViewAccountID= displayNumberFragment.findViewById<TextView>(R.id.textViewAccountIdBig)
        textViewAccountID.text = viewModel.accountNumberForDisplay
        return displayNumberFragment
    }


}