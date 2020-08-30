package com.md.williamriesen.hawkeyeharvestfoodbank

class Order {
    private val itemMap = mutableMapOf<String, Int>()

    fun add(item: String): Int {
        if (itemMap.containsKey(item)) {
            itemMap[item] = itemMap[item]!! + 1
        } else {
            itemMap[item] = 1
        }
        return itemMap[item]!!
    }

    fun remove(item: String): Int {
        if (itemMap.containsKey(item) && itemMap[item]!! > 0) {
            itemMap[item] = itemMap[item]!! - 1
        }
        return itemMap[item]!!
    }

    fun count(item: String): Int {
        return itemMap[item]!!
    }
}

