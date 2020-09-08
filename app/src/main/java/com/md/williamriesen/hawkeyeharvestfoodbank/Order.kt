package com.md.williamriesen.hawkeyeharvestfoodbank
class Order(){
    constructor(
        itemMap: MutableMap<String, Int> = mutableMapOf(),
        accountID: String
    ) : this()
    var itemMap: MutableMap<String, Int> = mutableMapOf()
    var accountID = ""
}