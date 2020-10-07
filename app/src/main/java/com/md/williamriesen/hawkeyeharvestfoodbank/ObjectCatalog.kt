package com.md.williamriesen.hawkeyeharvestfoodbank

class ObjectCatalog() {
  var catalogName: String? = null
    var itemList: List<Item>? = null
  constructor(itemListArg: List<Item>): this() {
    this.itemList = itemListArg
  }
}