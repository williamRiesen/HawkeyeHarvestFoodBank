package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ItemAdapter(options: FirestoreRecyclerOptions<Item>) :
    FirestoreRecyclerAdapter<Item, ItemAdapter.ItemHolder>(options) {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemName: TextView = view.findViewById<TextView>(R.id.text_view_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int, model: Item) {
        holder.textViewItemName.text = model.itemName
    }


}