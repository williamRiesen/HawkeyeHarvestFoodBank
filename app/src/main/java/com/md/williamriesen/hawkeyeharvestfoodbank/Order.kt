package com.md.williamriesen.hawkeyeharvestfoodbank
class Order() {
    var itemMap =  mutableMapOf<String, Int>()

    constructor(itemMapArg: MutableMap<String, Int>) : this() {
        itemMap = itemMapArg
    }
}