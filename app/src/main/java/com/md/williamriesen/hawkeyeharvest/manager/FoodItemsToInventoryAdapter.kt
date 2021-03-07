package com.md.williamriesen.hawkeyeharvest.manager

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import kotlinx.android.synthetic.main.fragment_new_food_item.view.*
import kotlinx.android.synthetic.main.item_to_update_inventory.view.*
import java.util.*


class FoodItemsToInventoryAdapter(
    private val localInventory: MutableLiveData<MutableList<FoodItem>>,
    var viewModel: ManagerActivityViewModel
) : RecyclerView.Adapter<FoodItemsToInventoryAdapter.MyViewHolder>(), Filterable {


    lateinit var adapter: ArrayAdapter<CharSequence>

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToInventoryName: TextView =
            view.findViewById(R.id.textview_item_to_inventory_name)
        var checkBoxIsAvailable: CheckBox = view.findViewById(R.id.checkBoxIsAvailable)
        var checkBoxSpecial: CheckBox = view.findViewById(R.id.checkBoxSpecial)
        var editTextNumberAvailable: EditText = view.findViewById(R.id.editTextNumberAvailable)
        var editTextLimit: EditText = view.findViewById(R.id.editTextQtyLimit)
        var editTextPoints: EditText = view.findViewById(R.id.editTextPoints)
        var spinnerCategory: Spinner = view.findViewById(R.id.spinnerCategory2)

        var textViewItemID: TextView = view.findViewById(R.id.textViewItemID)
        var expansionSection: LinearLayout = view.findViewById(R.id.expansionSection)
        var editTextEditName: EditText = view.findViewById(R.id.editTextEditName)
        var itemViewLinearLayout: LinearLayout = view.findViewById(R.id.itemViewLinearLayout)


        init {
            spinnerCategory.adapter = ArrayAdapter.createFromResource(
                view.context,
                R.array.categories_array,
                android.R.layout.simple_spinner_item
            )
            val editTextInventoryUpdater = View.OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
//                    updateInventory(assembleEditedItem(), view.context)
                }
            }
            editTextEditName.onFocusChangeListener = editTextInventoryUpdater
            editTextNumberAvailable.onFocusChangeListener = editTextInventoryUpdater
            editTextLimit.onFocusChangeListener = editTextInventoryUpdater
            editTextPoints.onFocusChangeListener = editTextInventoryUpdater

            val checkBoxInventoryUpdater = CompoundButton.OnCheckedChangeListener { checkBoxView, _ ->
//                updateInventory(assembleEditedItem(), checkBoxView.context)
            }
            checkBoxIsAvailable.setOnCheckedChangeListener(checkBoxInventoryUpdater)
            checkBoxSpecial.setOnCheckedChangeListener(checkBoxInventoryUpdater)

            val spinListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    updateInventory(assembleEditedItem(), view!!.context)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spinnerCategory.onItemSelectedListener = spinListener

        }

        fun assembleEditedItem(): FoodItem {
            return FoodItem(
                textViewItemID.text.toString().toInt(),
                editTextEditName.text.toString(),
                spinnerCategory.selectedItem.toString(),
                editTextPoints.text.toString().toInt(),
                editTextLimit.text.toString().toInt(),
                editTextNumberAvailable.text.toString().toInt(),
                checkBoxIsAvailable.isChecked,
                spinnerCategory.selectedItemPosition + 1,
                0,
                0,
                checkBoxSpecial.isChecked
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_to_update_inventory, parent, false)


        return MyViewHolder(v)
    }


    private fun updateInventory(foodItem: FoodItem, context: Context) {
        viewModel.itemsToInventory.value?.removeIf {
            foodItem.itemID == it.itemID
        }
        viewModel.itemsToInventory.value!!.add(foodItem)
        viewModel.submitUpdatedInventory(context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textViewItemToInventoryName.setOnClickListener {
            Log.d("TAG","onClickListener fired. Position: $position")
            Log.d("TAG","initial expanded status: ${viewModel.itemsToInventory.value!![position].isExpanded}")
//        holder.itemView.setOnClickListener {
            viewModel.itemsToInventory.value!![position].isExpanded =
                !viewModel.itemsToInventory.value!![position].isExpanded
            Log.d("TAG","final expanded status: ${viewModel.itemsToInventory.value!![position].isExpanded}")
            notifyItemChanged(position)
        }
        if (viewModel.itemsToInventory.value!![position].isExpanded) {
            holder.expansionSection.visibility = View.VISIBLE
        } else {
            holder.expansionSection.visibility = View.GONE
        }

        holder.textViewItemID.text =
            viewModel.itemsToInventory.value!![position].itemID.toString()

        holder.textViewItemToInventoryName.text =
            viewModel.itemsToInventory.value!![position].name



        holder.editTextEditName.setText(viewModel.itemsToInventory.value!![position].name!!.toString())
        holder.editTextNumberAvailable.setText(viewModel.itemsToInventory.value!![position].numberAvailable!!.toString())
        holder.editTextLimit.setText(viewModel.itemsToInventory.value!![position].limit!!.toString())
        holder.editTextPoints.setText(viewModel.itemsToInventory.value!![position].pointValue!!.toString())
        holder.checkBoxIsAvailable.isChecked =
            viewModel.itemsToInventory.value!![position].isAvailable!!
        holder.checkBoxSpecial.isChecked =
            viewModel.itemsToInventory.value!![position].special
        holder.spinnerCategory.setSelection( viewModel.itemsToInventory.value!![position].categoryId - 1)

        val isCategory =
            viewModel.itemsToInventory.value!![position].name ==
                    viewModel.itemsToInventory.value!![position].category
        when {
            isCategory -> {
                formatAsCategory(holder)
            }
            viewModel.itemsToInventory.value!![position].special -> {
                formatAsSpecial(holder)
            }
            else -> {
                formatAsItem(holder)
            }
        }
    }


    override fun getItemCount(): Int {
        var size = 0
        size = viewModel.itemsToInventory.value!!.size
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
                    resultList = localInventory
//                    viewModel.filteredInventoryList = itemsToInventoryList.value as MutableLiveData<MutableList<FoodItem>>
                } else {
                    for (row in viewModel.itemsToInventory.value!!) {
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
                viewModel.itemsToInventory =
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

    private fun formatAsSpecial(holder: MyViewHolder) {
        holder.checkBoxIsAvailable.visibility = View.VISIBLE
        holder.editTextNumberAvailable.visibility = View.VISIBLE
        holder.textViewItemToInventoryName.setBackgroundColor(Color.YELLOW)
        holder.textViewItemToInventoryName.setTextColor(Color.parseColor("#630293"))
    }

    private fun formatAsItem(holder: MyViewHolder) {
        holder.checkBoxIsAvailable.visibility = View.VISIBLE
        holder.editTextNumberAvailable.visibility = View.VISIBLE
        holder.textViewItemToInventoryName.setBackgroundColor(Color.WHITE)
        holder.textViewItemToInventoryName.setTextColor(Color.parseColor("#630293"))
    }


}





