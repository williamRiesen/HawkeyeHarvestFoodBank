package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class UpdateInventoryFragment : Fragment() {

    private lateinit var adapter: ItemsToInventoryAdapter
    private lateinit var viewModel: ManagerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(ManagerActivityViewModel::class.java)
        viewModel.getInventoryFromFirestore()
        viewModel.itemsToInventoryList.observe(this, Observer { adapter.notifyDataSetChanged() })
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
        return updateInventoryView
    }

    private fun setUpRecyclerView(itemsToInventoryList: MutableLiveData<MutableList<Item>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemsToInventoryAdapter(itemsToInventoryList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}