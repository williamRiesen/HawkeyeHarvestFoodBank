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


class PackOrderFragment : Fragment() {

    private lateinit var adapter: ItemsToPackAdapter
    private lateinit var viewModel: VolunteerActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
//        viewModel.getNextOrderFromFireStore()  // commented out due to being called in volunteer sign in fragment
        viewModel.itemsToPackList.observe(this, Observer { adapter.notifyDataSetChanged() })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val packOrderView = inflater.inflate(R.layout.fragment_pack_order, container, false)
        val recyclerView = packOrderView.findViewById<RecyclerView>(R.id.recyclerviewInventoryForUpdate)
        setUpRecyclerView(viewModel.itemsToPackList, recyclerView)
        return packOrderView
    }

    private fun setUpRecyclerView(itemsToPackList: MutableLiveData<MutableList<Item>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemsToPackAdapter(itemsToPackList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }




}