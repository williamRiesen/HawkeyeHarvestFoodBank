package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SelectionFragment : Fragment() {

    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
//        viewModel.sendCatalogToFireStore()
//        viewModel.populateFoodCountMapFromFireStore()
        viewModel.itemList.observe(this, Observer { adapter.notifyDataSetChanged() })
//        viewModel.foodCountMap.observe(this, Observer { adapter.notifyDataSetChanged() })
//        Log.d("TAG", "Calling setUpRecyclerView from onCreate")
//        viewModel.foodCountMap.observe(this, Observer { adapter.notifyDataSetChanged() })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val selectionView = inflater.inflate(R.layout.fragment_selection, container, false)
        val recyclerView = selectionView.findViewById<RecyclerView>(R.id.recyclerviewChoices)
        setUpRecyclerView(viewModel.itemList, recyclerView)
        return selectionView
    }

    private fun setUpRecyclerView(itemList: MutableLiveData<MutableList<Item>>, recyclerView: RecyclerView ) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemListAdapter(itemList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}