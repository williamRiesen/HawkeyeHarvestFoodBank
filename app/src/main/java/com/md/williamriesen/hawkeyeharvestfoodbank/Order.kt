package com.md.williamriesen.hawkeyeharvestfoodbank

import java.util.*

class Order(val accountID: Int) {
    private val itemMap = mutableMapOf<String, Int>()

    fun add(item: String): Int {
        if (itemMap.containsKey(item)) {
            itemMap[item]= itemMap[item]!! + 1
        } else {
            itemMap[item] = 1
        }
        return itemMap[item]!!
    }

    fun remove(item: String): Int {
        if (itemMap.containsKey(item) && itemMap[item]!! > 0) {
            itemMap[item]= itemMap[item]!! - 1
        }
        return itemMap[item]!!
    }
}
