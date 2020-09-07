package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class PackOrderFragment : Fragment() {

    private lateinit var adapter: ItemsToPackAdapter
    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val packOrderView = inflater.inflate(R.layout.fragment_pack_order, container, false)
        val recyclerView = packOrderView.findViewById<RecyclerView>(R.id.recyclerviewItemsToPack)
        viewModel.nextOrder?.let { setUpRecyclerView(it, recyclerView) }
        viewModel.getNextOrderFromFireStore()
        return packOrderView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
    }

    private fun setUpRecyclerView(nextOrder: Order, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemsToPackAdapter(nextOrder)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}