package com.md.williamriesen.hawkeyeharvestfoodbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapter(var orders: List<Order>) : RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {

        inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            var textViewOrderID: TextView = view.findViewById(R.id.textViewOrderID)
            var textViewAccountID: TextView = view.findViewById(R.id.textViewAccountID)
            var checkBoxPacked: CheckBox = view.findViewById(R.id.checkBox_packed)
            var checkBoxDelivered: CheckBox = view.findViewById(R.id.checkBox_delivered)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v: View =
                LayoutInflater.from(parent.context).inflate(R.layout.checkout_item, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        }

        override fun getItemCount() = 1
    }
