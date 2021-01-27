package com.md.williamriesen.hawkeyeharvest.volunteer

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem


class PackOrderFragment() : Fragment() {

    private lateinit var adapterFood: FoodItemsToPackAdapter
    private lateinit var viewModel: VolunteerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity())
            .get(VolunteerActivityViewModel::class.java)
//        viewModel.updateOrderAsBeingPacked(requireActivity())
        viewModel.itemsToPackList.observe(this, Observer { adapterFood.notifyDataSetChanged() })


    }
    private var simpleCallback = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                Log.d("TAG", "position ${viewHolder.adapterPosition}")
                viewModel.itemsToPackList.value?.get(viewHolder.adapterPosition)?.packed  = true
                viewModel.itemToMarkOutOfStock.value = viewModel.itemsToPackList.value?.get(viewHolder.adapterPosition)?.name
                Navigation.findNavController(requireView()).navigate(R.id.action_packOrderFragment_to_confirmOutOfStockFragment)
                // remove from adapter
            }
        })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val packOrderView = inflater.inflate(R.layout.fragment_pack_order, container, false)
        val recyclerView =
            packOrderView.findViewById<RecyclerView>(R.id.recyclerviewInventoryForUpdate)
        setUpRecyclerView(viewModel.itemsToPackList, recyclerView)
        val textViewAccount = packOrderView.findViewById<TextView>(R.id.textViewAccount)
        textViewAccount.text = viewModel.accountNumberForDisplay
        val accountListener = Observer<String> { accountID ->
            textViewAccount.text = accountID.takeLast(4)
        }
        viewModel.accountID.observe(viewLifecycleOwner, accountListener)
        val textViewFamilySize = packOrderView.findViewById<TextView>(R.id.textViewFamilySize)
        val familySizeObserver = Observer<Long> { familySize ->
            Log.d("TAG", "familySize within observer: $familySize")
            Log.d("TAG", "textViewFamilySize: $textViewFamilySize")
            textViewFamilySize.text = familySize.toString()
        }
        viewModel.familySize.observe(viewLifecycleOwner, familySizeObserver)
        simpleCallback.attachToRecyclerView(recyclerView)
        return packOrderView
    }

    private fun setUpRecyclerView(
        itemsToPackList: MutableLiveData<MutableList<FoodItem>>,
        recyclerView: RecyclerView
    ) {
        FirestoreRecyclerOptions.Builder<FoodItem>()
        adapterFood = FoodItemsToPackAdapter(itemsToPackList, viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapterFood
    }




}