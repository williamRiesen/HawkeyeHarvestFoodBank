package com.md.williamriesen.hawkeyeharvest.foodbank

class FoodItem() {
    var itemID: Int? = null
    var name: String? = null
    var category: String? = null
    var categoryId: Int = 0
    var pointValue: Int? = null
    var limit: Int? = null
    var numberAvailable: Int? = null
    var isAvailable: Boolean? = null
    var qtyOrdered: Int = 0
    var categoryPointsAllocated: Int = 0
    var categoryPointsUsed: Int = 0
    var packed: Boolean = false

    constructor(
        itemID: Int,
        name: String,
        category: String,
        pointValue: Int,
        limit: Int,
        numberAvailable: Int,
        isAvailable: Boolean,
        categoryId: Int,
        categoryPointsAllocated: Int = 0,
        categoryPointsUsed: Int = 0
    ) : this() {
        this.itemID = itemID
        this.name = name
        this.category = category
        this.pointValue = pointValue
        this.limit = limit
        this.numberAvailable = numberAvailable
        this.isAvailable = isAvailable
        this.categoryId = categoryId
        this.categoryPointsAllocated = categoryPointsAllocated
        this.categoryPointsUsed = categoryPointsUsed
    }
}