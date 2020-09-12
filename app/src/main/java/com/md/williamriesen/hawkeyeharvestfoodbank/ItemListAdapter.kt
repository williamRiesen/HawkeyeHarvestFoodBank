package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class ItemListAdapter(
    var itemList: MutableLiveData<MutableList<Item>>,
    var viewModel: MainActivityViewModel
) :
    RecyclerView.Adapter<ItemListAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemName: TextView = view.findViewById(R.id.textview_item_name)
        var textViewCount: TextView = view.findViewById(R.id.textView_count)
        var imageButtonAdd: ImageButton = view.findViewById(R.id.imageButtonAdd)
        var imageButtonRemove: ImageButton = view.findViewById(R.id.imageButtonRemove)

        init {
            imageButtonAdd.setOnClickListener {
                incrementCountOfItem(adapterPosition)
            }
            imageButtonRemove.setOnClickListener {
                decrementCountOfItem(adapterPosition)
            }
        }
    }

    fun incrementCountOfItem(position: Int) {
        val myList = itemList.value
        Log.d("TAG", "myList[position] before ${myList?.get(position)?.qtyOrdered} ")
        myList!![position].qtyOrdered = myList[position].qtyOrdered + 1
        Log.d("TAG", "myList[position] after  ${myList.get(position).qtyOrdered} ")
        myList.forEach { item ->
            Log.d("TAG", "item.qtyOrdered: ${item.qtyOrdered}")
        }
        itemList.value = myList
        viewModel.points = viewModel.points?.minus(1)
    }

    fun decrementCountOfItem(position: Int) {
        val myList = itemList.value
        if (myList?.get(position)?.qtyOrdered!! > 0) {
            myList[position].qtyOrdered = myList[position].qtyOrdered - 1
            itemList.value = myList
            viewModel.points = viewModel.points?.plus(1)
        }
    }

    fun checkIfOption(position: Int): Boolean {
        val pointsNeeded = 1
        return if (viewModel.points != null) {
            viewModel.points!! >= pointsNeeded
        } else false
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (itemList.value?.get(position)!!.qtyOrdered == 0) {

            holder.imageButtonRemove.visibility = View.INVISIBLE
        } else {
            holder.imageButtonRemove.visibility = View.VISIBLE
        }
        holder.textViewItemName.text = itemList.value!![position].name

        holder.textViewCount.text = itemList.value!![position].qtyOrdered.toString()
        Log.d("TAG", "position: $position, name: ${itemList.value!![position].name}")

        if (checkIfOption(position)) {
            holder.imageButtonAdd.visibility = View.VISIBLE
        } else {
            holder.imageButtonAdd.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {

        var size = 0
        if (itemList.value != null) {
            size = itemList.value?.size!!
        }
        return size
    }
}