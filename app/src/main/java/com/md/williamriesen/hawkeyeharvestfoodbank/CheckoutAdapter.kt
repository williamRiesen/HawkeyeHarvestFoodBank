package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class CheckoutAdapter(var foodCountMap: MutableLiveData<MutableMap<String, Int>>) :
    RecyclerView.Adapter<CheckoutAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemName: TextView = view.findViewById(R.id.textview_item_name)
        var textViewCount: TextView = view.findViewById(R.id.textView_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.checkout_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val orderList = order.toList()
//        holder.textViewItemName.text = orderList[position].first

//        holder.textViewCount.text= orderList[position].second.toString()

        holder.textViewItemName.text = foodCountMap.value!!.toList()[position].first
        holder.textViewCount.text = foodCountMap.value!!.toList()[position].second.toString()
    }

    override fun getItemCount(): Int {
        var size = 0
        if (foodCountMap.value != null) {
            size = foodCountMap.value!!.size
        }
        return size
    }
}
