package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class ItemsToPackAdapter(
    private val itemsToPackList: MutableLiveData<MutableList<Item>>,
    var viewModel: VolunteerActivityViewModel
) : RecyclerView.Adapter<ItemsToPackAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewItemToPackName: TextView = view.findViewById(R.id.textview_item_to_pack_name)
        var textViewItemToPackCount: TextView =
            view.findViewById<TextView>(R.id.textView_item_to_pack_count)
        var checkBoxPacked: CheckBox = view.findViewById(R.id.checkBoxPacked)


        init {
            checkBoxPacked.setOnClickListener { view ->
                Log.d("TAG", "onClickListener Fired.")
                val item = textViewItemToPackName.text
                viewModel.togglePackedState(item.toString())
                Log.d ("TAG", "all packed?: ${viewModel.checkIfAllItemsPacked()}")
                if (viewModel.checkIfAllItemsPacked()) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_packOrderFragment_to_confirmPacked)
                }
//                var allItemsPacked = onClickCheckBoxPacked(adapterPosition)
//                if (allItemsPacked) {
//                    navigateToConfirmPacked(view)
//                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsToPackAdapter.MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_to_pack, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemsToPackAdapter.MyViewHolder, position: Int) {

        val filteredItemsToPack = itemsToPackList.value!!.filter { it.qtyOrdered != 0 }
        holder.textViewItemToPackName.text =
            filteredItemsToPack[position].name
        holder.textViewItemToPackCount.text =
            filteredItemsToPack[position].qtyOrdered.toString()
        holder.checkBoxPacked.isChecked = filteredItemsToPack[position].packed
    }

    override fun getItemCount(): Int {
        var size = 0
        if (itemsToPackList.value != null) {
            size = itemsToPackList.value!!.filter { it.qtyOrdered != 0 }!!.size
        }
        return size
    }

    fun onClickCheckBoxPacked(position: Int): Boolean {

        return viewModel.upDatePackedStatus(position)
    }

    fun navigateToConfirmPacked(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_packOrderFragment_to_confirmPacked)
    }
}

