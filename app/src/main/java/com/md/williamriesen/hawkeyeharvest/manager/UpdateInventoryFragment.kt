package com.md.williamriesen.hawkeyeharvest.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem


class UpdateInventoryFragment : Fragment() {

    private lateinit var adapterFood: FoodItemsToInventoryAdapter
    private lateinit var viewModel: ManagerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(ManagerActivityViewModel::class.java)
        viewModel.itemsToInventoryList.observe(this, Observer { adapterFood.notifyDataSetChanged() })
        viewModel.retrieveCategoriesFromFireStore(this)
//        viewModel.getInventoryFromFirestore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val updateInventoryView = inflater.inflate(R.layout.fragment_update_inventory, container, false)
        val recyclerView = updateInventoryView.findViewById<RecyclerView>(R.id.recyclerviewInventoryForUpdate)
        setUpRecyclerView(viewModel.itemsToInventoryList, recyclerView)
        val actionButton = updateInventoryView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        actionButton.setOnClickListener {
            viewModel.submitUpdatedInventory(requireContext())
        }
        val actionButtonAddItem = updateInventoryView.findViewById<FloatingActionButton>(R.id.floatingActionButtonAddItem)
        actionButtonAddItem.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_updateInventoryFragment_to_newFoodItemFragment)
        }
        return updateInventoryView
    }

    private fun setUpRecyclerView(itemsToInventoryList: MutableLiveData<MutableList<FoodItem>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<FoodItem>()
        adapterFood = FoodItemsToInventoryAdapter(itemsToInventoryList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapterFood
    }
}