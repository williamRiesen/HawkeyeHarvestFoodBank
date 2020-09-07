package com.md.williamriesen.hawkeyeharvestfoodbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsToPackAdapter(val nextOrder: Order) : RecyclerView.Adapter<ItemsToPackAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToPackName = view.findViewById<TextView>(R.id.textview_item_to_pack_name)
        var textViewItemToPackCount = view.findViewById<TextView>(R.id.textView_item_to_pack_count)
        var checkBoxPacked = view.findViewById<CheckBox>(R.id.checkBox_packed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsToPackAdapter.MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.checkout_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemsToPackAdapter.MyViewHolder, position: Int) {
        holder.textViewItemToPackName.text = nextOrder.itemMap.toList()[position].first
        holder.textViewItemToPackCount.text = nextOrder.itemMap.toList()[position].second.toString()
    }

    override fun getItemCount() = nextOrder.itemMap.size

}
