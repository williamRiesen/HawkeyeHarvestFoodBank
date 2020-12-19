package com.md.williamriesen.hawkeyeharvest.manager

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem

class FoodItemsToInventoryAdapter(
    private val itemsToInventoryList: MutableLiveData<MutableList<FoodItem>>,
    var viewModel: ManagerActivityViewModel
) : RecyclerView.Adapter<FoodItemsToInventoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToInventoryName: TextView =
            view.findViewById(R.id.textview_item_to_inventory_name)
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
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_to_update_inventory, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.textViewItemToInventoryName.text = itemsToInventoryList.value!![position].name
        holder.checkBoxIsAvailable.isChecked = itemsToInventoryList.value!![position].isAvailable!!
        val isCategory =
            itemsToInventoryList.value!![position].name == itemsToInventoryList.value!![position].category
        if (isCategory) {
            formatAsCategory(holder)
        } else {
            formatAsItem(holder)
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        if (itemsToInventoryList.value != null) {
            size = itemsToInventoryList.value!!.size
        }
        return size
    }

    private fun formatAsCategory(holder: MyViewHolder) {
        holder.checkBoxIsAvailable.visibility = View.INVISIBLE
        holder.textViewItemToInventoryName.setTextColor(
            ContextCompat.getColor(holder.view.context, R.color.logoLimeGreen)
        )
        holder.textViewItemToInventoryName.setBackgroundColor(
            ContextCompat.getColor(
                holder.view.context,
                R.color.colorAccent
            )
        )
    }


    private fun formatAsItem(holder: MyViewHolder) {
        holder.checkBoxIsAvailable.visibility = View.VISIBLE
        holder.textViewItemToInventoryName.setTextColor(Color.parseColor("#630293"))
        holder.textViewItemToInventoryName.setBackgroundColor(Color.WHITE)

    }
}



