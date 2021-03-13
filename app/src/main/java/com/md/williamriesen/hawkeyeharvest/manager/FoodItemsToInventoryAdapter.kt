package com.md.williamriesen.hawkeyeharvest.manager

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodItem
import com.md.williamriesen.hawkeyeharvest.foodbank.Order
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
        var buttonUpdate: Button = view.findViewById(R.id.buttonUpdate)
        var buttonCancel: Button = view.findViewById(R.id.buttonCancel)
        var spinListenerHasFiredPreviously = false


        init {
            spinnerCategory.adapter = ArrayAdapter.createFromResource(
                view.context,
                R.array.categories_array,
                android.R.layout.simple_spinner_item
            )


            val editTextInventoryUpdater = View.OnFocusChangeListener { view, hasFocus ->
                Log.d("TAG", "$view hasFocus $hasFocus isDirty ${view.isDirty}")
                if (!hasFocus) {
//                    updateInventory(assembleEditedItem(), view.context)
                }
            }


            editTextEditName.onFocusChangeListener = editTextInventoryUpdater
            editTextNumberAvailable.onFocusChangeListener = editTextInventoryUpdater
            editTextLimit.onFocusChangeListener = editTextInventoryUpdater
            editTextPoints.onFocusChangeListener = editTextInventoryUpdater







            itemView.setOnFocusChangeListener { view, hasFocus ->
                Log.d("TAG", "itemView onFocusChange fired.  hasFocus: $hasFocus")
            }
        }

        val spinListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (spinListenerHasFiredPreviously) {
                    buttonCancel.visibility = View.VISIBLE
                    buttonUpdate.visibility = View.VISIBLE

                } else {
                    spinListenerHasFiredPreviously = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

//        fun showUpdateCancelButtons(position: Int) {
//            viewModel.itemsToInventory.value!![position].edited = true
//            buttonUpdate.setOnClickListener {
//                updateInventory(assembleEditedItem(), it.context)
//            }
//            buttonCancel.setOnClickListener {
//                collapseWithoutSaving(position)
//            }
//            notifyItemChanged(position)
//        }

//        fun collapseWithoutSaving(position: Int) {
////            viewModel.itemsToInventory.value!![position].edited = false
//            viewModel.itemsToInventory.value!![position].isExpanded = false
//            spinListenerHasFiredPreviously = false
//            notifyItemChanged(position)
//        }

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




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.textViewItemToInventoryName.setOnClickListener {
            viewModel.itemsToInventory.value!![position].isExpanded =
                !viewModel.itemsToInventory.value!![position].isExpanded
            notifyItemChanged(position)
        }

        holder.textViewItemToInventoryName.text =
            viewModel.itemsToInventory.value!![position].name

        holder.checkBoxIsAvailable.isChecked =
            viewModel.itemsToInventory.value!![position].isAvailable!!

        holder.checkBoxIsAvailable.setOnClickListener {
            viewModel.toggleIsAvailableStatus(
                holder.textViewItemToInventoryName.text.toString(),
                it.context
            )
        }


        if (viewModel.itemsToInventory.value!![position].isExpanded) {
            holder.expansionSection.visibility = View.VISIBLE


            holder.editTextEditName.setText(viewModel.itemsToInventory.value!![position].name!!.toString())
            holder.editTextEditName.addTextChangedListener {
                holder.buttonCancel.visibility = View.VISIBLE
                holder.buttonUpdate.visibility = View.VISIBLE
            }

            holder.editTextNumberAvailable.setText(viewModel.itemsToInventory.value!![position].numberAvailable!!.toString())
            holder.editTextNumberAvailable.addTextChangedListener {
                holder.buttonCancel.visibility = View.VISIBLE
                holder.buttonUpdate.visibility = View.VISIBLE
            }

            holder.editTextLimit.setText(viewModel.itemsToInventory.value!![position].limit!!.toString())
            holder.editTextLimit.addTextChangedListener {
                holder.buttonCancel.visibility = View.VISIBLE
                holder.buttonUpdate.visibility = View.VISIBLE
            }

            holder.editTextPoints.setText(viewModel.itemsToInventory.value!![position].pointValue!!.toString())
            holder.editTextPoints.addTextChangedListener {
                holder.buttonCancel.visibility = View.VISIBLE
                holder.buttonUpdate.visibility = View.VISIBLE
            }

            holder.textViewItemID.text =
                viewModel.itemsToInventory.value!![position].itemID.toString()

            holder.checkBoxSpecial.isChecked =
                viewModel.itemsToInventory.value!![position].special


            holder.checkBoxSpecial.setOnCheckedChangeListener { _, _ ->
                holder.buttonCancel.visibility = View.VISIBLE
                holder.buttonUpdate.visibility = View.VISIBLE
            }

            holder.buttonCancel.setOnClickListener {
                holder.buttonUpdate.visibility = View.INVISIBLE
                holder.buttonCancel.visibility = View.INVISIBLE
                viewModel.itemsToInventory.value!![position].isExpanded = false
                holder.spinListenerHasFiredPreviously = false
                notifyItemChanged(position)
            }

            holder.buttonUpdate.setOnClickListener {
                viewModel.updateFoodItem(holder.assembleEditedItem(), it.context)
                viewModel.itemsToInventory.value!![position].isExpanded = false
                holder.spinListenerHasFiredPreviously = false
                notifyDataSetChanged()
//                notifyItemChanged(position)
            }

            holder.spinnerCategory.setSelection(viewModel.itemsToInventory.value!![position].categoryId - 1)
            holder.spinnerCategory.onItemSelectedListener = holder.spinListener

            holder.buttonUpdate.visibility = View.INVISIBLE
            holder.buttonCancel.visibility = View.INVISIBLE


        } else {
            holder.expansionSection.visibility = View.GONE
        }


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
//                            Log.d("TAG", "row: ${row.name}")
//                            Log.d("TAG", "resultList: ${resultList.value}")
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





