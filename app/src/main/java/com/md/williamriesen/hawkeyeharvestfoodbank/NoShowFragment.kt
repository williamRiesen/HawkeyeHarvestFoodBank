package com.md.williamriesen.hawkeyeharvestfoodbank

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


class NoShowFragment : Fragment() {

    private lateinit var adapter: NoShowAdapter
    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
        viewModel.getTodaysOrdersList()
        viewModel.todaysOrdersList.observe(this, Observer{ adapter.notifyDataSetChanged()})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment =  inflater.inflate(R.layout.fragment_no_show, container, false)
        val recyclerView = fragment.findViewById<RecyclerView>(R.id.recyclerViewTodaysOrders)
        setUpRecyclerView(viewModel.todaysOrdersList, recyclerView)
        return fragment
    }

    private fun setUpRecyclerView(todaysOrdersList: MutableLiveData<MutableList<Order>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = NoShowAdapter(todaysOrdersList, viewModel, this.activity as VolunteerActivity)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }

}