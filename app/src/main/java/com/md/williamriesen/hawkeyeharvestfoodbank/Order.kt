package com.md.williamriesen.hawkeyeharvestfoodbank
class Order() {
    var itemList =  mutableListOf<Item>()

    constructor(itemListArg: MutableList<Item> ) : this() {
        itemList = itemListArg
    }
}