package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel


class MainActivityViewModel : ViewModel() {

    var order: Order = Order(123456)


    fun addItem(itemName: String):Int {
        val count = order.add(itemName)
        Log.d("TAG", "count $count")
        return count
    }

    fun removeItem(itemName: String):Int {
        return order.remove(itemName)
    }


}