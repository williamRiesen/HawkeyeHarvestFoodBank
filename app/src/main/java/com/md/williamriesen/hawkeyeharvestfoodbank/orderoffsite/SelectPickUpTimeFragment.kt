package com.md.williamriesen.hawkeyeharvestfoodbank.orderoffsite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvestfoodbank.R

class SelectPickUpTimeFragment : Fragment() {
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_select_pick_up_time, container, false)
        val radioGroup = fragment.findViewById<RadioGroup>(R.id.radioGroup)
        val buttonNext = fragment.findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener {
            val selection = radioGroup.checkedRadioButtonId
            val pickUpHour24 = when (selection){
                R.id.radioButton2to3PM ->  14
                R.id.radioButton3to4PM ->  15
                else ->  0
            }
            viewModel.goToNextFragment(
                pickUpHour24, requireView()
            )
        }

        return fragment
    }

}