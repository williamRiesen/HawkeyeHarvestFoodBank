package com.md.williamriesen.hawkeyeharvestfoodbank.foodbank

import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodItem

class ObjectCatalog() {
  var catalogName: String? = null
    var foodItemList: List<FoodItem>? = null
  constructor(foodItemListArg: List<FoodItem>): this() {
    this.foodItemList = foodItemListArg
  }
}