package com.md.williamriesen.hawkeyeharvestfoodbank.volunteer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.Order

class OrdersToPackAdapter(
    private val todaysSubmittedOrdersList: MutableLiveData<MutableList<Order>>,
    var viewModel: VolunteerActivityViewModel,
    var activity: VolunteerActivity
) : RecyclerView.Adapter<OrdersToPackAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewOrderSize: TextView = view.findViewById(R.id.textViewOrderSize)
        var textViewAccountID: TextView = view.findViewById(R.id.textViewAccountID)
        var textViewPickUpHour24: TextView = view.findViewById(R.id.textViewPickUpHour24)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.order_to_pack, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewAccountID.text = todaysSubmittedOrdersList.value!![position].accountID
        holder.textViewOrderSize.text =
            todaysSubmittedOrdersList.value!![position].itemList.size.toString()
        val pickUpHour24: Int = todaysSubmittedOrdersList.value!![position]?.pickUpHour24 ?: 0
        val pickUpHour12 = when {
            (pickUpHour24 > 12) -> (pickUpHour24 - 12).toString() + " PM"
            (pickUpHour24 == 12) -> "12 Noon"
            else -> "$pickUpHour24 AM"
        }
        holder.textViewPickUpHour24.text = pickUpHour12.toString()
    }

    override fun getItemCount(): Int {
        var size = 0
        Log.d("TAG", "todaysSubmittedOrderList.value ${todaysSubmittedOrdersList.value}")
        if (todaysSubmittedOrdersList.value != null) {
            size = todaysSubmittedOrdersList.value!!.size
        }
        Log.d("TAG", "size: $size")
        return size
    }

}

