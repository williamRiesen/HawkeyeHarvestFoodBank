package com.md.williamriesen.hawkeyeharvestfoodbank.volunteer

import android.os.Bundle
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
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.Order
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodItem


class NoShowFragment : Fragment() {

    private lateinit var adapter: NoShowAdapter
    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
        viewModel.getTodaysPackedOrdersList()
        viewModel.todaysPackedOrdersList.observe(this, Observer{ adapter.notifyDataSetChanged()})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment =  inflater.inflate(R.layout.fragment_no_show, container, false)
        val recyclerView = fragment.findViewById<RecyclerView>(R.id.recyclerViewTodaysOrders)
        setUpRecyclerView(viewModel.todaysPackedOrdersList, recyclerView)
        return fragment
    }

    private fun setUpRecyclerView(todaysOrdersList: MutableLiveData<MutableList<Order>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<FoodItem>()
        adapter = NoShowAdapter(todaysOrdersList, viewModel, this.activity as VolunteerActivity)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }

}