package com.md.williamriesen.hawkeyeharvest.volunteer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem


class PackOrderFragment : Fragment() {

    private lateinit var adapterFood: FoodItemsToPackAdapter
    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
//        viewModel.updateOrderAsBeingPacked(requireActivity())
        viewModel.itemsToPackList.observe(this, Observer { adapterFood.notifyDataSetChanged() })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val packOrderView = inflater.inflate(R.layout.fragment_pack_order, container, false)
        val recyclerView = packOrderView.findViewById<RecyclerView>(R.id.recyclerviewInventoryForUpdate)
        setUpRecyclerView(viewModel.itemsToPackList, recyclerView)
        val textViewAccount = packOrderView.findViewById<TextView>(R.id.textViewAccount)
        textViewAccount.text = viewModel.accountNumberForDisplay
        val accountListener = Observer<String> { accountID ->
            textViewAccount.text = accountID.takeLast(4)
        }
        viewModel.accountID.observe(viewLifecycleOwner, accountListener)
        val textViewFamilySize = packOrderView.findViewById<TextView>(R.id.textViewFamilySize)
        val familySizeObserver = Observer<Long> {familySize ->
            Log.d("TAG", "familySize within observer: $familySize")
            Log.d("TAG", "textViewFamilySize: $textViewFamilySize")
            textViewFamilySize.text = familySize.toString()
        }
        viewModel.familySize.observe(viewLifecycleOwner, familySizeObserver)
        return packOrderView
    }

    private fun setUpRecyclerView(itemsToPackList: MutableLiveData<MutableList<FoodItem>>, recyclerView: RecyclerView) {
        FirestoreRecyclerOptions.Builder<FoodItem>()
        adapterFood = FoodItemsToPackAdapter(itemsToPackList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapterFood
    }
}