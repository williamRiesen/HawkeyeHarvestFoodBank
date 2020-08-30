package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class ItemListAdapter(var foodCountMap: MutableLiveData<MutableMap<String, Int>>) :
    RecyclerView.Adapter<ItemListAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemName: TextView = view.findViewById(R.id.text_view_item_name)
        var textViewCount: TextView = view.findViewById(R.id.textView_count)
        var imageButtonAdd: ImageButton = view.findViewById(R.id.imageButtonAdd)

        init {
            imageButtonAdd.setOnClickListener{
                incrementCountOfItem(adapterPosition)
            }
        }
    }

    fun incrementCountOfItem(position: Int){
        val myMap = foodCountMap.value
        val itemName = myMap!!.toList()[position].first
        Log.d("TAG", "Position $position, Item Name $itemName")
        myMap[itemName] = myMap[itemName]!! + 1
        foodCountMap.value = myMap
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedItemName = holder.textViewItemName.text
        holder.textViewItemName.text = foodCountMap.value!!.toList()[position].first
        holder.textViewCount.text = foodCountMap.value!!.toList()[position].second.toString()
    }

    override fun getItemCount(): Int {
        return foodCountMap.value!!.size
    }
}