package com.md.williamriesen.hawkeyeharvestfoodbank

class Item() {
    var itemID: Int? = null
    var name: String? = null
    var category: String? = null
    var pointValue: Int? = null
    var limit: Int? = null
    var numberAvailable: Int? = null
    var isOption: Boolean? = null

    constructor(a: Int, b: String, c: String, d: Int, e: Int, f: Int, g: Boolean) : this() {
        this.itemID = a
        this.name = b
        this.category = c
        this.pointValue = d
        this.limit = e
        this.numberAvailable = f
        this.isOption = g
    }
}