package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CheckoutFragment : Fragment() {

    lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: CheckoutAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
        viewModel.itemList.observe(this, Observer { adapter.notifyDataSetChanged() })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val checkoutView = inflater.inflate(R.layout.fragment_checkout, container, false)
        val recyclerView = checkoutView.findViewById<RecyclerView>(R.id.recyclerviewChoices)
        setUpRecyclerView(viewModel.itemList, recyclerView)
        return checkoutView
    }

    private fun setUpRecyclerView(myList: MutableLiveData<MutableList<Item>>, recyclerView:RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = CheckoutAdapter(myList)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}