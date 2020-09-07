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


class PackOrderFragment : Fragment() {

    private lateinit var adapter: ItemsToPackAdapter
    private lateinit var viewModel: VolunteerActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
        viewModel.getNextOrderFromFireStore()
        viewModel.itemsToPackMap.observe(this, Observer { adapter.notifyDataSetChanged() })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "onCreateView called.")
        val packOrderView = inflater.inflate(R.layout.fragment_pack_order, container, false)
        val recyclerView = packOrderView.findViewById<RecyclerView>(R.id.recyclerviewItemsToPack)
        setUpRecyclerView(viewModel.itemsToPackMap, recyclerView)
        Log.d("TAG", "onCreateView Complete")
        return packOrderView
    }

    private fun setUpRecyclerView(itemsToPackMap: MutableLiveData<MutableMap<String, Int>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemsToPackAdapter(itemsToPackMap)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}