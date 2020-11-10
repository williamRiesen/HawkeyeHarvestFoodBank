package com.md.williamriesen.hawkeyeharvestfoodbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
        val buttonBack = checkoutView.findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener { activity?.onBackPressed() }

        val buttonNext = checkoutView.findViewById<Button>(R.id.button_next)
        buttonNext.setOnClickListener { view ->
            if (acceptNextDayOrders){
                if (acceptSameDayOrders){
                    TODO()// what to do if both kinds of orders accepted
                } else {
                    viewModel.submitNextDayOrder(view)
                }
            } else {
                viewModel.saveOrder(view)
            }
        }
        val recyclerView = checkoutView.findViewById<RecyclerView>(R.id.recyclerviewChoices)
        setUpRecyclerView(viewModel.itemList, recyclerView)
        return checkoutView
    }

    private fun setUpRecyclerView(
        myList: MutableLiveData<MutableList<Item>>,
        recyclerView: RecyclerView
    ) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = CheckoutAdapter(myList)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}