package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
//        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
//        viewModel.sendCatalogToFireStore()
        viewModel.populateFoodCountMapFromFireStore()

        viewModel.foodCountMap.observe(this, Observer { adapter.notifyDataSetChanged() })

        setUpRecyclerView(viewModel.foodCountMap)
        viewModel.foodCountMap.observe(this, Observer { adapter.notifyDataSetChanged() })


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setUpRecyclerView(viewModel.foodCountMap)
        return inflater.inflate(R.layout.fragment_selection, container, false)
    }

    private fun setUpRecyclerView(foodCountMap: MutableLiveData<MutableMap<String, Int>>) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemListAdapter(foodCountMap)
        val recyclerview = view?.findViewById<RecyclerView>(R.id.view)
//        val recyclerview = findViewById<RecyclerView>(R.id.view)
        recyclerview?.layoutManager = LinearLayoutManager(this.activity)
        recyclerview?.adapter = adapter
    }

    fun onAddItem(view: android.view.View) {
        val itemName = view?.findViewById<TextView>(R.id.text_view_item_name).text.toString()
        viewModel.addItem(itemName)
        adapter.notifyDataSetChanged()
    }

    fun onRemoveItem(view: android.view.View) {
        val itemName = view?.findViewById<TextView>(R.id.text_view_item_name).text.toString()
        val updatedCount = viewModel.removeItem(itemName)
        val textViewCount = view?.findViewById<TextView>(R.id.textView_count)
        textViewCount.text = updatedCount.toString()
    }

}