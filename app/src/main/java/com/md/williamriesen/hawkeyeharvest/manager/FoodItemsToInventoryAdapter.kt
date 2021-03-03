package com.md.williamriesen.hawkeyeharvest.manager

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Movie
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import kotlinx.android.synthetic.main.activity_secure_tablet_order.*
import java.util.*


class FoodItemsToInventoryAdapter(
    private val itemsToInventoryList: MutableLiveData<MutableList<FoodItem>>,
    var viewModel: ManagerActivityViewModel
) : RecyclerView.Adapter<FoodItemsToInventoryAdapter.MyViewHolder>(), Filterable {


    lateinit var adapter: ArrayAdapter<CharSequence>

    @RequiresApi(Build.VERSION_CODES.N)

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToInventoryName: TextView =
            view.findViewById(R.id.textview_item_to_inventory_name)
        var checkBoxIsAvailable: CheckBox = view.findViewById(R.id.checkBoxIsAvailable)
        var editTextNumberAvailable: EditText = view.findViewById(R.id.editTextNumberAvailable)
        var editTextLimit: EditText = view.findViewById(R.id.editTextLimit)
        var editTextPoints: EditText = view.findViewById(R.id.editTextPoints)
        var spinnerCategory: Spinner = view.findViewById(R.id.spinnerCategory2)
        var textViewItemID: TextView = view.findViewById(R.id.textViewItemID)

        var expansionSection: LinearLayout = view.findViewById(R.id.expansionSection)
        var editTextEditName: EditText = view.findViewById(R.id.editTextEditName)
        var wasEdited = false

        init {
            spinnerCategory.adapter = adapter

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
            editTextLimit.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {

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
        adapter = ArrayAdapter.createFromResource(
            parent.context,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        )

        return MyViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textViewItemToInventoryName.text =
            viewModel.filteredInventoryList.value!![position].name
        holder.checkBoxIsAvailable.isChecked =
            viewModel.filteredInventoryList.value!![position].isAvailable!!

        if (viewModel.filteredInventoryList.value!![position].isExpanded) {
            holder.expansionSection.visibility = View.VISIBLE
        } else {
            holder.expansionSection.visibility = View.GONE
        }

        val isCategory =
            viewModel.filteredInventoryList.value!![position].name == viewModel.filteredInventoryList.value!![position].category
        if (isCategory) {
            formatAsCategory(holder)
        } else {
            formatAsItem(holder)
            holder.spinnerCategory.setSelection(viewModel.filteredInventoryList.value!![position].categoryId - 1)
            holder.editTextNumberAvailable.setText(viewModel.filteredInventoryList.value!![position].numberAvailable!!.toString())
            holder.editTextLimit.setText(viewModel.filteredInventoryList.value!![position].limit!!.toString())
            holder.editTextPoints.setText(viewModel.itemsToInventoryList.value!![position].pointValue!!.toString())
            holder.editTextEditName.setText(viewModel.itemsToInventoryList.value!![position].name!!.toString())
            holder.textViewItemID.text =
                viewModel.itemsToInventoryList.value!![position].itemID.toString()

        }
        holder.itemView.setOnClickListener {
            if (viewModel.filteredInventoryList.value!![position].isExpanded) {

                val editedItem = FoodItem(
                    holder.textViewItemID.text.toString().toInt(),
                    holder.editTextEditName.text.toString(),
                    holder.spinnerCategory.selectedItem.toString(),
                    holder.editTextPoints.text.toString().toInt(),
                    holder.editTextLimit.text.toString().toInt(),
                    holder.editTextNumberAvailable.text.toString().toInt(),
                    holder.checkBoxIsAvailable.isChecked,
                    holder.spinnerCategory.selectedItemPosition + 1
                )

                viewModel.updateFoodItem(editedItem, it.context)
                viewModel.itemsToInventoryList.value!![position].name =
                    holder.editTextEditName.text.toString()
            }
            viewModel.filteredInventoryList.value!![position].isExpanded =
                !viewModel.filteredInventoryList.value!![position].isExpanded
            notifyItemChanged(position)
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



