package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class InstructionsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment = inflater.inflate(R.layout.fragment_instructions, container, false)
        val buttonReady = fragment.findViewById<Button>(R.id.buttonReady)
        buttonReady.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_instructionsFragment_to_selectionFragment)
        }
        return fragment
    }

}