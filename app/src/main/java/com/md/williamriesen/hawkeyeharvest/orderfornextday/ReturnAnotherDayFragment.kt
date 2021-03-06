package com.md.williamriesen.hawkeyeharvest.orderfornextday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.orderoffsite.MainActivityViewModel

class ReturnAnotherDayFragment : Fragment() {
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel()::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_return_another_day, container, false)
        val nextPreOrderDay = fragment.findViewById<TextView>(R.id.textViewNextPreOrderDay)
        nextPreOrderDay.text = viewModel.nextPreOrderDay
        val nextPickUpDay = fragment.findViewById<TextView>(R.id.textViewNextPickUpDay)
        nextPickUpDay.text = viewModel.nextPickUpDay
        return fragment
    }
}