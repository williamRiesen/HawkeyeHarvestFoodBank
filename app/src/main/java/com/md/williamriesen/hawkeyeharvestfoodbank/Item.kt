package com.md.williamriesen.hawkeyeharvestfoodbank

class Item (
    var itemID: String = "Item ID not set",
    var itemName: String = "Item Name not set.",
    var category: String = "Category not set.",
    var points: Int = 0,
    var countInStock: Int = 0,
    var limit: Int = 0 )