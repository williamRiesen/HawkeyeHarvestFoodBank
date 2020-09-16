package com.md.williamriesen.hawkeyeharvestfoodbank

class Item() {
    var itemID: Int? = null
    var name: String? = null
    var category: String? = null
    var pointValue: Int? = null
    var limit: Int? = null
    var numberAvailable: Int? = null
    var isOption: Boolean? = null
    var qtyOrdered: Int = 0
    var categoryPointsAllocated: Int = 0
    var categoryPointsUsed: Int = 0

    constructor(itemID: Int, name: String, category: String, pointValue: Int,
                limit: Int, numberAvailable: Int, isOption: Boolean,
                categoryPointsAllocated: Int =0 , categoryPointsUsed: Int = 0
    ) : this() {
        this.itemID = itemID
        this.name = name
        this.category = category
        this.pointValue = pointValue
        this.limit = limit
        this.numberAvailable = numberAvailable
        this.isOption = isOption
        this.categoryPointsAllocated = categoryPointsAllocated
        this.categoryPointsUsed = categoryPointsUsed
    }
}