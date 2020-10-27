package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class ReviseSavedOrderOptionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reviseSavedOrderOptionFragment = inflater.inflate(R.layout.fragment_revise_saved_order_option, container, false)
        val buttonExit = reviseSavedOrderOptionFragment.findViewById<Button>(R.id.button_exit)
        buttonExit.setOnClickListener {
            activity?.onBackPressed()
        }

        return reviseSavedOrderOptionFragment
    }

}