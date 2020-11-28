package com.md.williamriesen.hawkeyeharvest.volunteer

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
import com.md.williamriesen.hawkeyeharvest.*
import com.md.williamriesen.hawkeyeharvest.foodbank.Order


class DisplayOrdersListFragment : Fragment() {


    private lateinit var adapter: OrdersToPackAdapter
    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
        viewModel.getTodaysSubmittedOrdersList()
        viewModel.todaysSubmittedOrdersList.value?.forEach {
            Log.d("TAG", "accountID: ${it.accountID}, orderSize: ${it.itemList.size}, pickUpHour24: ${it.pickUpHour24}")
        }
        viewModel.todaysPackedOrdersList.observe(this, Observer{ adapter.notifyDataSetChanged()})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment =  inflater.inflate(R.layout.fragment_display_orders_list, container, false)
        val recyclerView = fragment.findViewById<RecyclerView>(R.id.recyclerViewOrdersList)
        setUpRecyclerView(viewModel.todaysSubmittedOrdersList, recyclerView)
        return fragment
    }

    private fun setUpRecyclerView(todaysOrdersList: MutableLiveData<MutableList<Order>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Order>()
        adapter = OrdersToPackAdapter(todaysOrdersList, viewModel, this.activity as VolunteerActivity)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }

}