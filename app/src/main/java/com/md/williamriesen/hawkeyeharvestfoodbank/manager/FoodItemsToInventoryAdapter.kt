package com.md.williamriesen.hawkeyeharvestfoodbank.manager

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodItem

class FoodItemsToInventoryAdapter(
    private val itemsToInventoryList: MutableLiveData<MutableList<FoodItem>>,
    var viewModel: ManagerActivityViewModel
) : RecyclerView.Adapter<FoodItemsToInventoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToInventoryName: TextView = view.findViewById(R.id.textview_item_to_inventory_name)
        var checkBoxIsAvailable: CheckBox = view.findViewById(R.id.checkBoxIsAvailable)


        init {
            checkBoxIsAvailable.setOnClickListener {
                val item = textViewItemToInventoryName.text
                viewModel.toggleIsAvailableStatus(item.toString())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_to_update_inventory, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textViewItemToInventoryName.text = itemsToInventoryList.value!![position].name
        Log.d("TAG", "${itemsToInventoryList.value!![position].name}, position $position, isAvailable " +
                "${itemsToInventoryList.value!![position].isAvailable}")
        holder.checkBoxIsAvailable.isChecked = itemsToInventoryList.value!![position].isAvailable!!
    }

    override fun getItemCount(): Int {
        var size = 0
        if (itemsToInventoryList.value != null) {
            size = itemsToInventoryList.value!!.size
        }
        return size
    }

}

