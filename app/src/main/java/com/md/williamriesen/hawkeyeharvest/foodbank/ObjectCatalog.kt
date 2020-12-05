package com.md.williamriesen.hawkeyeharvest.foodbank

class ObjectCatalog() {
  var catalogName: String? = null
    var foodItemList: List<FoodItem>? = null
  constructor(foodItemListArg: List<FoodItem>): this() {
    this.foodItemList = foodItemListArg
  }
}