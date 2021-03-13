package com.md.williamriesen.hawkeyeharvest.foodbank

class ObjectCatalog() {
  var catalogName: String? = null
    var foodItemList = mutableListOf<FoodItem>()
  constructor(foodItemListArg: MutableList<FoodItem>): this() {
    this.foodItemList = foodItemListArg
  }
}