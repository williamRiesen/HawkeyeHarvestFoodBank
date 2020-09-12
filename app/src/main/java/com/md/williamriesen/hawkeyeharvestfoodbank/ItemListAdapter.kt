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
    var foodCountMap: MutableLiveData<MutableMap<String, Int>>,
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
        val myMap = foodCountMap.value
        val itemName = myMap!!.toList()[position].first
        Log.d("TAG", "Position $position, Item Name $itemName")
        myMap[itemName] = myMap[itemName]!! + 1
        foodCountMap.value = myMap
        viewModel.points = viewModel.points?.minus(1)
    }

    fun decrementCountOfItem(position: Int) {
        val myMap = foodCountMap.value
        val itemName = myMap!!.toList()[position].first
        Log.d("TAG", "Position $position, Item Name $itemName")
        if (myMap[itemName]!! > 0) {
            myMap[itemName] = myMap[itemName]!! - 1
            foodCountMap.value = myMap
            viewModel.points = viewModel.points?.plus(1)
        }
    }

    fun checkIfOption(position: Int): Boolean {
        val myMap = foodCountMap.value
        val itemName = myMap!!.toList()[position].first
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
        val selectedItemName = holder.textViewItemName.text
        if (foodCountMap.value!!.toList()[position].second == 0) {
            holder.imageButtonRemove.visibility = View.INVISIBLE
        } else {
            holder.imageButtonRemove.visibility = View.VISIBLE
        }
        holder.textViewItemName.text = foodCountMap.value!!.toList()[position].first
        holder.textViewCount.text = foodCountMap.value!!.toList()[position].second.toString()
        if (checkIfOption(position)) {
            holder.imageButtonAdd.visibility = View.VISIBLE
        } else {
            holder.imageButtonAdd.visibility = View.INVISIBLE
        }


    }

    override fun getItemCount(): Int {
        var size = 0
        if (foodCountMap.value != null) {
            size = foodCountMap.value!!.size
        }
        return size
    }
}