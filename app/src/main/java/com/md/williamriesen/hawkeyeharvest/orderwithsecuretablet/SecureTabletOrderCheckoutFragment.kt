package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.md.williamriesen.hawkeyeharvest.orderoffsite.CheckoutAdapter
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import com.md.williamriesen.hawkeyeharvest.R

class SecureTabletOrderCheckoutFragment : Fragment() {

    lateinit var viewModel: SecureTabletOrderViewModel
    private lateinit var adapter: CheckoutAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(SecureTabletOrderViewModel::class.java)
        viewModel.foodItemList.observe(this, Observer { adapter.notifyDataSetChanged() })
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
            Navigation.findNavController(view)
                .navigate(R.id.action_secureTabletOrderCheckoutFragment_to_secureTabletOrderConfirmAndReset)
        }
        val recyclerView = checkoutView.findViewById<RecyclerView>(R.id.recyclerviewChoices)
        setUpRecyclerView(viewModel.foodItemList, recyclerView)

        return checkoutView
    }

    private fun setUpRecyclerView(
        myList: MutableLiveData<MutableList<FoodItem>>,
        recyclerView: RecyclerView
    ) {
        FirestoreRecyclerOptions.Builder<FoodItem>()
        adapter = CheckoutAdapter(myList)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }
}