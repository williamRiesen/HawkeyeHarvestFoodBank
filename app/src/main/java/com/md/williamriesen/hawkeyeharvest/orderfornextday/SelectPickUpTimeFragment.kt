package com.md.williamriesen.hawkeyeharvest.orderfornextday

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvest.R

class SelectPickUpTimeFragment : Fragment() {
    lateinit var viewModel: NextDayOrderingActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(NextDayOrderingActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_select_pick_up_time, container, false)
        val radioGroup = fragment.findViewById<RadioGroup>(R.id.radioGroup)
        val buttonNext = fragment.findViewById<Button>(R.id.buttonNext)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d("TAG", "radioButton CLicked.")
            buttonNext.visibility = View.VISIBLE
        }
        buttonNext.setOnClickListener {
            val pickUpHour24 = when (radioGroup.checkedRadioButtonId) {
                R.id.radioButton2to3PM -> 14
                R.id.radioButton3to4PM -> 15
                else -> 0
            }
            viewModel.goToNextFragment(
                pickUpHour24, requireView()
            )
        }

        return fragment
    }

}