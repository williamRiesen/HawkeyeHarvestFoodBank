package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemListAdapter(private val itemList: List<Pair<String,Int>>) :
    RecyclerView.Adapter<ItemListAdapter.MyViewHolder>() {
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        var textViewItemName: TextView = view.findViewById<TextView>(R.id.text_view_item_name)
        var textViewCount: TextView = view.findViewById(R.id.textView_count)
        var imageButtonAdd: ImageButton = view.findViewById(R.id.imageButtonAdd)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewItemName.text = itemList[position].first
        holder.textViewCount.text = itemList[position].second.toString()
        holder.imageButtonAdd.setOnClickListener {
            Log.d("TAG", "AddButton Pressed on position ${holder.adapterPosition}")
        }
    }

    override fun getItemCount() = itemList.size

}