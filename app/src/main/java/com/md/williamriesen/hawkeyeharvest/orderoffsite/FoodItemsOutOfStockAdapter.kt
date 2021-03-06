package com.md.williamriesen.hawkeyeharvest.orderoffsite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvest.R

class FoodItemsOutOfStockAdapter(
    private val itemsOutOfStockList: MutableLiveData<MutableList<String>>,
    var viewModel: MainActivityViewModel
) : RecyclerView.Adapter<FoodItemsOutOfStockAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemOutOfStockName: TextView = view.findViewById(R.id.textview_item_out_of_stock_name)


        init {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_out_of_stock, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.textViewItemOutOfStockName.text = itemsOutOfStockList.value!![position]
    }

    override fun getItemCount(): Int {
        var size = 0
        if (itemsOutOfStockList.value != null) {
            size = itemsOutOfStockList.value!!.size
        }
        return size
    }

}

