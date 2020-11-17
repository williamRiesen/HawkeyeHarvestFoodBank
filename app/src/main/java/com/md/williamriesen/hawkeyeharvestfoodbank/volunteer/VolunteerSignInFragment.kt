package com.md.williamriesen.hawkeyeharvestfoodbank.volunteer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.Order

class VolunteerSignInFragment : Fragment() {

    lateinit var viewModel: VolunteerActivityViewModel
    lateinit var adapter: OrdersToPackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val volunteerSignInFragment =
            inflater.inflate(R.layout.fragment_volunteer_sign_in, container, false)
        val textViewOrderCount =
            volunteerSignInFragment.findViewById<TextView>(R.id.textViewOrderCount)
        val countObserver = Observer<Int> { newCount: Int ->
            textViewOrderCount.text = newCount.toString()
        }
        val imageButtonNoShow = volunteerSignInFragment.findViewById<ImageButton>(R.id.imageButtonNoShow)
        imageButtonNoShow.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_volunteerSignInFragment_to_noShowFragment)
        }
        val buttonPreviewOrders = volunteerSignInFragment.findViewById<Button>(R.id.buttonPreviewOrders)
        buttonPreviewOrders.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_volunteerSignInFragment_to_displayOrdersListFragment)
        }
        val recyclerView = volunteerSignInFragment.findViewById<RecyclerView>(R.id.recyclerViewOrdersList)
//        setUpRecyclerView(viewModel.todaysSubmittedOrdersList, recyclerView)
//        viewModel.todaysSubmittedOrdersList.observe(viewLifecycleOwner, Observer { adapter.notifyDataSetChanged() })
        viewModel.ordersToPackCount.observe(viewLifecycleOwner, countObserver)
        viewModel.getNextOrderFromFireStore()
        viewModel.getTodaysSubmittedOrdersList()
        return volunteerSignInFragment
    }

    private fun setUpRecyclerView(todaysOrdersList: MutableLiveData<MutableList<Order>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Order>()
        adapter = OrdersToPackAdapter(todaysOrdersList, viewModel, this.activity as VolunteerActivity)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }

}