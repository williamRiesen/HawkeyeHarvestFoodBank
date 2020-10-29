package com.md.williamriesen.hawkeyeharvestfoodbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class NoShowAdapter(
    private val todaysOrdersList: MutableLiveData<MutableList<Order>>,
    var viewModel: VolunteerActivityViewModel,
    val activity: VolunteerActivity
) : RecyclerView.Adapter<NoShowAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewOrderNumber: TextView = view.findViewById(R.id.textViewOrderNumber)
        var textViewOrderID: TextView = view.findViewById(R.id.textViewOrderID)
        var imageButtonNoShow: ImageButton = view.findViewById(R.id.imageButtonNoShow)
        init {
            imageButtonNoShow.setOnClickListener {
                val orderID = textViewOrderID.text
                    viewModel.recordNoShow(orderID.toString(), it.context)
                activity.onBackPressed()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoShowAdapter.MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.order, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoShowAdapter.MyViewHolder, position: Int) {
        val thisOrder = todaysOrdersList.value?.get(position)
        holder.textViewOrderID.text = thisOrder?.orderID
        holder.textViewOrderNumber.text = thisOrder?.accountID!!.takeLast(4)
    }

    override fun getItemCount(): Int {
        var size = 0
        if (todaysOrdersList.value != null) {
            size = todaysOrdersList.value!!.size
        }
        return size
    }

}

