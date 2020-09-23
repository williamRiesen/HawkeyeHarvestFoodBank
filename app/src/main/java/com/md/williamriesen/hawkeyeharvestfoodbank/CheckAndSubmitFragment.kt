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

class CheckAndSubmitFragment : Fragment() {

    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.itemList.observe(this, Observer { adapter.notifyDataSetChanged() })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewCheckAndConfirm =
            inflater.inflate(R.layout.fragment_check_and_submit, container, false)
        val recyclerView = viewCheckAndConfirm.findViewById<RecyclerView>(R.id.recyclerviewConfirm)
        setUpRecyclerView(viewModel.itemList, recyclerView)
        return viewCheckAndConfirm
    }

    private fun setUpRecyclerView(
        foodCountMap: MutableLiveData<MutableList<Item>>,
        recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemListAdapter(foodCountMap, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}
