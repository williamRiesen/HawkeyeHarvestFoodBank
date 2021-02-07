package com.md.williamriesen.hawkeyeharvest.manager

import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import java.util.*
import kotlin.collections.ArrayList

class FoodItemsToInventoryAdapter(
    private val itemsToInventoryList: MutableLiveData<MutableList<FoodItem>>,
    var viewModel: ManagerActivityViewModel
) : RecyclerView.Adapter<FoodItemsToInventoryAdapter.MyViewHolder>(), Filterable {

    @RequiresApi(Build.VERSION_CODES.N)
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToInventoryName: TextView =
            view.findViewById(R.id.textview_item_to_inventory_name)
        var checkBoxIsAvailable: CheckBox = view.findViewById(R.id.checkBoxIsAvailable)
        var editTextNumberAvailable: EditText = view.findViewById(R.id.editTextNumberAvailable)

        init {
            checkBoxIsAvailable.setOnClickListener {
                val item = textViewItemToInventoryName.text
                viewModel.toggleIsAvailableStatus(item.toString(), it.context)
            }

            editTextNumberAvailable.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    val item = textViewItemToInventoryName.text
                    val numberAvailable = editTextNumberAvailable.text
                    viewModel.updateNumberAvailable(item.toString(), numberAvailable, view.context)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_to_update_inventory, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textViewItemToInventoryName.text =
            viewModel.filteredInventoryList.value!![position].name
        holder.checkBoxIsAvailable.isChecked =
            viewModel.filteredInventoryList.value!![position].isAvailable!!

        val isCategory =
            viewModel.filteredInventoryList.value!![position].name == viewModel.filteredInventoryList.value!![position].category
        if (isCategory) {
            formatAsCategory(holder)
        } else {
            formatAsItem(holder)
            holder.editTextNumberAvailable.setText(viewModel.filteredInventoryList.value!![position].numberAvailable!!.toString())
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        size = viewModel.filteredInventoryList.value!!.size
        Log.d("TAG", "size: $size")
        viewModel.showNewItemButton.value = size == 0
        return size


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                var resultList = MutableLiveData<MutableList<FoodItem>>(mutableListOf())
                if (charSearch.isEmpty()) {
                    resultList = itemsToInventoryList
//                    viewModel.filteredInventoryList = itemsToInventoryList.value as MutableLiveData<MutableList<FoodItem>>
                } else {
                    for (row in viewModel.itemsToInventoryList.value!!) {
                        if (row.name!!.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            Log.d("TAG", "row: ${row.name}")
                            Log.d("TAG", "resultList: ${resultList.value}")
                            resultList.value!!.add(row)
                        }
                    }
                    viewModel.searchString = charSearch
                }

//                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                viewModel.filteredInventoryList =
                    results?.values as MutableLiveData<MutableList<FoodItem>>
                notifyDataSetChanged()
            }
        }
    }

    private fun formatAsCategory(holder: MyViewHolder) {
        holder.checkBoxIsAvailable.visibility = View.GONE
        holder.editTextNumberAvailable.visibility = View.GONE
        holder.textViewItemToInventoryName.setTextColor(
            ContextCompat.getColor(holder.view.context, R.color.logoLimeGreen)
        )
        holder.textViewItemToInventoryName.setBackgroundColor(
            ContextCompat.getColor(
                holder.view.context,
                R.color.colorAccent
            )
        )
    }

    private fun formatAsItem(holder: MyViewHolder) {
        holder.checkBoxIsAvailable.visibility = View.VISIBLE
        holder.editTextNumberAvailable.visibility = View.VISIBLE
        holder.textViewItemToInventoryName.setTextColor(Color.parseColor("#630293"))
        holder.textViewItemToInventoryName.setBackgroundColor(Color.WHITE)
    }
}



