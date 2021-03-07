package com.md.williamriesen.hawkeyeharvest.manager

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
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
    private lateinit var search: androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(ManagerActivityViewModel::class.java)
        viewModel.itemsToInventory.observe(this, Observer { adapterFood.notifyDataSetChanged() })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val updateInventoryView = inflater.inflate(R.layout.fragment_update_inventory, container, false)
        val buttonNewItem = updateInventoryView.findViewById<FloatingActionButton>(R.id.floatingActionButtonAddItem)
        viewModel.showNewItemButton.observe(viewLifecycleOwner, Observer {show ->
            if (show) buttonNewItem.visibility = View.VISIBLE
            else buttonNewItem.visibility = View.INVISIBLE
        })

        val recyclerView = updateInventoryView.findViewById<RecyclerView>(R.id.recyclerviewInventoryForUpdate)
        setUpRecyclerView(viewModel.itemsToInventory, recyclerView)
        val actionButton = updateInventoryView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        actionButton.setOnClickListener {
            val foodItem = FoodItem (
                308,
                "Party Dip",
                "Specials",
                1,
                100,
                100,
                false,
                21
            )
            viewModel.updateFoodItem(foodItem, requireContext())
            viewModel.retrieveCategoriesFromFireStore(this)
//            viewModel.submitUpdatedInventory(requireContext())
        }
        val actionButtonAddItem = updateInventoryView.findViewById<FloatingActionButton>(R.id.floatingActionButtonAddItem)
        actionButtonAddItem.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_updateInventoryFragment2_to_createNewItemFragment2)
        }
        search = updateInventoryView.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterFood.filter.filter(newText)
                return false
            }
        })
        search.requestFocus()
        return updateInventoryView
    }

    private fun setUpRecyclerView(itemsToInventoryList: MutableLiveData<MutableList<FoodItem>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<FoodItem>()
        adapterFood = FoodItemsToInventoryAdapter(itemsToInventoryList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapterFood
    }

    override fun onResume() {
        super.onResume()
        viewModel.retrieveCategoriesFromFireStore(this)
    }
}