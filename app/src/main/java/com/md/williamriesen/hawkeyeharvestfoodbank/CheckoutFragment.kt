package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Transformation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CheckoutFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: CheckoutAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
//        viewModel.populateFoodCountMapFromFireStore()
        viewModel.foodCountMap.observe(this, Observer {adapter.notifyDataSetChanged() })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val checkoutView = inflater.inflate(R.layout.fragment_checkout, container, false)
        val recyclerView = checkoutView.findViewById<RecyclerView>(R.id.recyclerviewChoices)
        setUpRecyclerView(viewModel.foodCountMap, recyclerView)
        // Inflate the layout for this fragment
        return checkoutView
    }

    private fun setUpRecyclerView(foodCountMap:MutableLiveData<MutableMap<String, Int>> , recyclerView:RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
//        val order = viewModel.foodCountMap.value!!.toMutableMap()
        adapter = CheckoutAdapter(foodCountMap)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}