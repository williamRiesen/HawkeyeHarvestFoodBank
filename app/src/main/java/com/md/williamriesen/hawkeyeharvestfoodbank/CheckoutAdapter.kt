package com.md.williamriesen.hawkeyeharvestfoodbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class CheckoutAdapter(var itemList: MutableLiveData<MutableList<Item>>) :
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
        val filteredList = itemList.value?.filter { it.qtyOrdered != 0 }
//        val filteredOrder = order.value?.filterValues { it != 0 }
        holder.textViewItemName.text = filteredList?.get(position)?.name
        holder.textViewCount.text = filteredList?.get(position)?.qtyOrdered.toString()

//
//        holder.textViewItemName.text = filteredOrder?.toList()?.get(position)?.first
//        holder.textViewCount.text = filteredOrder?.toList()?.get(position)?.second.toString()
    }

    override fun getItemCount(): Int {
        var size = 0
        val filteredList = itemList.value?.filter { it.qtyOrdered != 0 }
//        val filteredOrder = order.value?.filterValues { it != 0 }
        if (filteredList != null) {
            size = filteredList.size
        }
        return size
    }

}

