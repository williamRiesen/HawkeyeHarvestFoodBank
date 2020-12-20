package com.md.williamriesen.hawkeyeharvest.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.navigation.Navigation
import com.md.williamriesen.hawkeyeharvest.R


class ManagerStartFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment =
         inflater.inflate(R.layout.fragment_manager_start, container, false)
        val buttonAccountAddEdit = fragment.findViewById<Button>(R.id.buttonAccountAddEdit)
        buttonAccountAddEdit.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_managerStartFragment_to_addEditAccountFragment)
        }
        val buttonInventory = fragment.findViewById<Button>(R.id.buttonInventory)
        buttonInventory.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_managerStartFragment_to_updateInventoryFragment)
        }
        return fragment
    }



}