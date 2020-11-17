package com.md.williamriesen.hawkeyeharvestfoodbank.orderoffsite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodItem
import com.md.williamriesen.hawkeyeharvestfoodbank.R

class CheckoutAdapter(var foodItemList: MutableLiveData<MutableList<FoodItem>>) :
    RecyclerView.Adapter<CheckoutAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemName: TextView = view.findViewById(R.id.textview_item_name)
        var textViewCount: TextView = view.findViewById(R.id.textView_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.checkout_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val filteredList = foodItemList.value?.filter { it.qtyOrdered != 0 }
        holder.textViewItemName.text = filteredList?.get(position)?.name
        holder.textViewCount.text = filteredList?.get(position)?.qtyOrdered.toString()
    }

    override fun getItemCount(): Int {
        var size = 0
        val filteredList = foodItemList.value?.filter { it.qtyOrdered != 0 }
        if (filteredList != null) {
            size = filteredList.size
        }
        return size
    }

}

