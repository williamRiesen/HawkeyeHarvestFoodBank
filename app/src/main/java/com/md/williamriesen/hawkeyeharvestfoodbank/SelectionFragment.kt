package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_selection.*

class SelectionFragment : Fragment() {

    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
//        viewModel.sendCatalogToFireStore()
        viewModel.populateFoodCountMapFromFireStore()
        viewModel.foodCountMap.observe(this, Observer { adapter.notifyDataSetChanged() })
        Log.d("TAG", "Calling setUpRecyclerView from onCreate")
        viewModel.foodCountMap.observe(this, Observer { adapter.notifyDataSetChanged() })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val selectionView = inflater.inflate(R.layout.fragment_selection, container, false)
        val recyclerView = selectionView.findViewById<RecyclerView>(R.id.recyclerviewChoices)
        setUpRecyclerView(viewModel.foodCountMap, recyclerView)
        return selectionView
    }

    private fun setUpRecyclerView(foodCountMap: MutableLiveData<MutableMap<String, Int>>, recyclerView: RecyclerView ) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemListAdapter(foodCountMap)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}