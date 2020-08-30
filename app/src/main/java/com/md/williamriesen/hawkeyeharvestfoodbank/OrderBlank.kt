package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log

class OrderBlank(catalog: Catalog) {
    var itemMappedToCount = catalog.itemList
    fun itemNameAt(index: Int) = itemMappedToCount.toList()[index].first
    fun itemCountAt(index: Int) = itemMappedToCount.toList()[index].second

    fun add(item: String) {
        if (itemMappedToCount.containsKey(item)) {
            val priorCount = itemMappedToCount[item]
            val newCount = priorCount!!.plus(1)
            itemMappedToCount[item] = newCount
        } else {
            Log.d("TAG", "Item not found in map: something is wrong.")
        }
    }

    fun remove(item: String) {
        if (itemMappedToCount.containsKey(item)) {
            if (itemMappedToCount[item]!! >0) {
                val priorCount = itemMappedToCount[item]
                val newCount = priorCount!!.plus(1)
                itemMappedToCount[item] = newCount
            }
        } else {
            Log.d("TAG", "Item not found in map: something is wrong.")
        }
    }
}

