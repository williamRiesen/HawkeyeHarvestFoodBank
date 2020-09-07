package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class ItemsToPackAdapter(val itemsToPackMap: MutableLiveData<MutableMap<String, Int>>) :
    RecyclerView.Adapter<ItemsToPackAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToPackName: TextView = view.findViewById(R.id.textview_item_to_pack_name)
        var textViewItemToPackCount: TextView = view.findViewById<TextView>(R.id.textView_item_to_pack_count)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsToPackAdapter.MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_to_pack, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemsToPackAdapter.MyViewHolder, position: Int) {
        Log.d("TAG", "itemsToPackMap.toString ${itemsToPackMap.toString()}")
        Log.d("TAG", "itemsToPackMap.value.toString ${itemsToPackMap.value.toString()}")
        Log.d("TAG", "itemsToPackMap.value.toList()toString ${itemsToPackMap.value?.toList().toString()}")
        Log.d("TAG", "position $position")
        Log.d("TAG", "itemsToPackMap.value.toList()!![position].toString() " +
                itemsToPackMap.value?.toList()!![position].toString()
        )
        Log.d("TAG", "itemsToPackMap.value.toList()!![position].first" +
                itemsToPackMap.value?.toList()!![position].first
        )
        Log.d("TAG", "textViewItemToPackName ${holder.textViewItemToPackName}")
        Log.d("TAG", "holder.view $holder.view")
        holder.textViewItemToPackName.text =
            itemsToPackMap.value?.toList()!![position].first
        holder.textViewItemToPackCount.text =
            itemsToPackMap.value?.toList()?.get(position)?.second.toString()
    }

    override fun getItemCount(): Int {
        var size = 0
        if (itemsToPackMap.value != null) {
            size = itemsToPackMap.value!!.size
        }
        return size
    }

}
