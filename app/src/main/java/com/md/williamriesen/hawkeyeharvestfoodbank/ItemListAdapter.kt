package com.md.williamriesen.hawkeyeharvestfoodbank

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvestfoodbank.R.*

class ItemListAdapter(
    var itemList: MutableLiveData<MutableList<Item>>,
    var viewModel: MainActivityViewModel
) :
    RecyclerView.Adapter<ItemListAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemName: TextView = view.findViewById(id.textview_item_name)
        var textViewCount: TextView = view.findViewById(id.textView_count)
        var imageButtonAdd: ImageButton = view.findViewById(id.imageButtonAdd)
        var imageButtonRemove: ImageButton = view.findViewById(id.imageButtonRemove)
        var textViewSelectedOf: TextView = view.findViewById(id.textViewSelectedOf)
        var textViewPointsUsed: TextView = view.findViewById(id.textViewPointsUsed)
        var textViewPointsAllocated: TextView = view.findViewById(id.textViewPointsAllocated)

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
        myList!![position].qtyOrdered = myList[position].qtyOrdered + 1
        itemList.value = myList
        viewModel.points = viewModel.points?.minus(1)
        val selectedCategory = myList[position].category
        val viewModelCategory = viewModel.categoriesList.value!!.find {
            it.name == selectedCategory
        }
        viewModelCategory!!.pointsUsed =
            viewModelCategory.pointsUsed + myList[position].pointValue!!
        val categoryHeading = myList.find {
            it.name == selectedCategory
        }
        categoryHeading!!.categoryPointsUsed =
            categoryHeading.categoryPointsUsed + myList[position].pointValue!!
    }

    fun decrementCountOfItem(position: Int) {
        val myList = itemList.value
        if (myList?.get(position)?.qtyOrdered!! > 0) {
            myList[position].qtyOrdered = myList[position].qtyOrdered - 1
            itemList.value = myList
            viewModel.points = viewModel.points?.plus(1)
            val selectedCategory = myList[position].category
            val viewModelCategory = viewModel.categoriesList.value!!.find {
                it.name == selectedCategory
            }
            viewModelCategory!!.pointsUsed =
                viewModelCategory.pointsUsed + myList[position].pointValue!!
            val categoryHeading = myList.find {
                it.name == selectedCategory
            }
            categoryHeading!!.categoryPointsUsed =
                categoryHeading.categoryPointsUsed - myList[position].pointValue!!
        }
    }

    private fun checkIfOption(position: Int): Boolean {
        val limit = itemList.value!![position].limit
        val qtyOrderedSoFar = itemList.value!![position].qtyOrdered
        val atLimit = qtyOrderedSoFar == limit
        val pointsNeeded = itemList.value!![position].pointValue!!.toInt()
        val thisCategory = itemList.value!![position].category
        val pointsUsed = itemList.value!!.find { item ->
            Log.d("TAG", "item.name: ${item.name}")
            item.name == thisCategory
        }!!.categoryPointsUsed
        val pointsAllocated = itemList.value!!.find { item ->
            item.name == thisCategory
        }!!.categoryPointsAllocated
        val pointsAvailable = pointsAllocated - pointsUsed
        val enoughPoints = (pointsAvailable >= pointsNeeded)
        return enoughPoints && !atLimit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val isCategory =
            itemList.value!![position].name == itemList.value!![position].category
        val isBottomBar =
            itemList.value!![position].name == "Bottom Bar"

        when {
            (isBottomBar) -> {
                formatAsBottomBar(holder)
            }
            (isCategory) -> {
                formatAsCategory(holder)
                val thisCategory = itemList.value!![position].name
                holder.textViewItemName.text = thisCategory
                holder.textViewPointsAllocated.text =
                    itemList.value!![position].categoryPointsAllocated.toString()
                holder.textViewPointsUsed.text = itemList.value!!.find { item ->
                    item.name == thisCategory
                }!!.categoryPointsUsed.toString()
//                itemList.value!![position].categoryPointsUsed.toString()
            }
            else -> {
                formatAsItem(holder)
                if (itemList.value?.get(position)!!.qtyOrdered == 0) {
                    holder.imageButtonRemove.visibility = View.INVISIBLE
                } else {
                    holder.imageButtonRemove.visibility = View.VISIBLE
                }
                holder.textViewItemName.text = itemList.value!![position].name
                holder.textViewCount.text = itemList.value!![position].qtyOrdered.toString()

                if (checkIfOption(position)) {
                    holder.imageButtonAdd.visibility = View.VISIBLE
                } else {
                    holder.imageButtonAdd.visibility = View.INVISIBLE
                }
            }
        }
    }


    private fun formatAsCategory(holder: MyViewHolder) {
        holder.imageButtonRemove.visibility = View.GONE
        holder.imageButtonAdd.visibility = View.GONE
        holder.textViewCount.visibility = View.GONE
        holder.textViewPointsUsed.visibility = View.VISIBLE
        holder.textViewSelectedOf.visibility = View.VISIBLE
        holder.textViewSelectedOf.text = "Selected of"
        holder.textViewPointsAllocated.visibility = View.VISIBLE
        holder.textViewItemName.visibility =View.VISIBLE
        holder.textViewItemName.textSize = 32F
        holder.textViewItemName.setTextColor(Color.BLACK)
        holder.textViewItemName.setBackgroundColor(Color.parseColor("#008577"))
    }

    private fun formatAsItem(holder: MyViewHolder) {
        holder.textViewCount.visibility = View.VISIBLE
        holder.textViewItemName.textSize = 20F
        holder.textViewItemName.setTextColor(Color.parseColor("#630293"))
        holder.textViewItemName.setBackgroundColor(Color.WHITE)
        holder.textViewPointsUsed.visibility = View.GONE
        holder.textViewSelectedOf.visibility = View.GONE
        holder.textViewPointsAllocated.visibility = View.GONE
        holder.textViewItemName.visibility = View.VISIBLE
    }

    private fun formatAsBottomBar(holder: MyViewHolder) {
        holder.imageButtonRemove.visibility = View.GONE
        holder.imageButtonAdd.visibility = View.GONE
        holder.textViewCount.visibility = View.GONE
        holder.textViewItemName.visibility = View.GONE
        holder.textViewSelectedOf.visibility - View.VISIBLE
        holder.textViewPointsUsed.visibility = View.VISIBLE
        holder.textViewPointsUsed.text = "--"
        holder.textViewSelectedOf.visibility = View.VISIBLE
        holder.textViewSelectedOf.text = " End "
        holder.textViewPointsAllocated.visibility = View.VISIBLE
        holder.textViewPointsAllocated.text = "--"
        holder.textViewItemName.setBackgroundColor(Color.parseColor("#008577"))
        holder.textViewCount.setTextColor(Color.parseColor("#000000"))
    }

    override fun getItemCount(): Int {
        var size = 0
        if (itemList.value != null) {
            size = itemList.value?.size!!
        }
        return size
    }
}