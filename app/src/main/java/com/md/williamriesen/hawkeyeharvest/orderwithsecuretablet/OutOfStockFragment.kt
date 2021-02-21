package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.OutOfStockItem


class OutOfStockFragment : Fragment() {

    private lateinit var adapterFood: FoodItemsOutOfStockAdapter

    lateinit var viewModel: SecureTabletOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this.requireActivity()).get(SecureTabletOrderViewModel::class.java)
        viewModel.outOfStockItems.observe(this, Observer{adapterFood.notifyDataSetChanged()})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val outOfStockFragment = inflater.inflate(R.layout.fragment_out_of_stock_notice, container, false)

        val recyclerView = outOfStockFragment.findViewById<RecyclerView>(R.id.recyclerView_out_of_stock_names)
        setUpRecyclerView(viewModel.outOfStockItems,recyclerView)
        val buttonOK2 = outOfStockFragment.findViewById<Button>(R.id.buttonOK2)
        buttonOK2.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_outOfStockFragment_to_selectionFragment)
            activity?.onBackPressed()
        }
        return outOfStockFragment
    }

    private fun setUpRecyclerView(outOfStockItems: MutableLiveData<MutableList<OutOfStockItem>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<String>()
        adapterFood = FoodItemsOutOfStockAdapter(outOfStockItems, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapterFood
    }
}