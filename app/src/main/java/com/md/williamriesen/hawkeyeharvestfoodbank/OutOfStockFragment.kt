package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.DateFormat


class OutOfStockFragment : Fragment() {

    private lateinit var adapter: ItemsOutOfStockAdapter

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(MainActivityViewModel::class.java)
        viewModel.outOfStockNameList.observe(this, Observer{adapter.notifyDataSetChanged()})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val outOfStockFragment = inflater.inflate(R.layout.fragment_out_of_stock_notice, container, false)

        val recyclerView = outOfStockFragment.findViewById<RecyclerView>(R.id.recyclerView_out_of_stock_names)
        setUpRecyclerView(viewModel.outOfStockNameList,recyclerView)
        val buttonOK2 = outOfStockFragment.findViewById<Button>(R.id.buttonOK2)
        buttonOK2.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_outOfStockFragment_to_selectionFragment)
            activity?.onBackPressed()
        }
        return outOfStockFragment
    }

    private fun setUpRecyclerView(outOfStockItemList: MutableLiveData<MutableList<String>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<String>()
        adapter = ItemsOutOfStockAdapter(outOfStockItemList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}