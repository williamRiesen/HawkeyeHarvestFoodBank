package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class ItemsToPackAdapter(val itemsToPackList: MutableLiveData<MutableList<Item>>) :
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
        Log.d("TAG", "itemsToPackMap.toString ${itemsToPackList.toString()}")
        Log.d("TAG", "itemsToPackMap.value.toString ${itemsToPackList.value.toString()}")
        Log.d("TAG", "itemsToPackMap.value.toList()toString ${itemsToPackList.value?.toList().toString()}")
        Log.d("TAG", "position $position")
        Log.d("TAG", "itemsToPackMap.value.toList()!![position].toString() " +
                itemsToPackList.value?.toList()!![position].toString()
        )
        Log.d("TAG", "itemsToPackMap.value.toList()!![position].first" +
                itemsToPackList.value!![position].name
        )
        Log.d("TAG", "textViewItemToPackName ${holder.textViewItemToPackName}")
        Log.d("TAG", "holder.view $holder.view")
        val filteredItemsToPack = itemsToPackList.value!!.filter { it.qtyOrdered !=0  }
        holder.textViewItemToPackName.text =
            filteredItemsToPack[position].name
        holder.textViewItemToPackCount.text =
            filteredItemsToPack[position].qtyOrdered.toString()
    }

    override fun getItemCount(): Int {
        var size = 0
        if (itemsToPackList.value != null) {
            size = itemsToPackList.value!!.filter { it. qtyOrdered !=0 }!!.size
        }
        return size
    }

}
