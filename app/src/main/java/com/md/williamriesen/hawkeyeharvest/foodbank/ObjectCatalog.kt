package com.md.williamriesen.hawkeyeharvest.foodbank

class ObjectCatalog() {
  var catalogName: String? = null
    var foodItemList: MutableList<FoodItem>? = null
  constructor(foodItemListArg: MutableList<FoodItem>): this() {
    this.foodItemList = foodItemListArg
  }
}