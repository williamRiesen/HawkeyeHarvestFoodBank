package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.Order
import java.util.*

class SecureTabletOrderSelectionFragment : Fragment() {

    private lateinit var adapter: SecureTabletOrderItemListAdapter
    private lateinit var viewModel: SecureTabletOrderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate called for SecureTabletSelectionFragment.")
        activity?.title = "FoodItem Selection"
        activity?.actionBar?.setHomeButtonEnabled(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(SecureTabletOrderViewModel::class.java)
        viewModel.foodItemList.observe(this, Observer { adapter.notifyDataSetChanged() })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val selectionView = inflater.inflate(R.layout.fragment_selection, container, false)
        val recyclerView = selectionView.findViewById<RecyclerView>(R.id.recyclerviewChoices)
        setUpRecyclerView(viewModel.foodItemList, recyclerView)
        return selectionView
    }

    private fun setUpRecyclerView(
        foodItemList: MutableLiveData<MutableList<FoodItem>>,
        recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<FoodItem>()
        adapter = SecureTabletOrderItemListAdapter(foodItemList,viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveOrder(requireView())
    }
}