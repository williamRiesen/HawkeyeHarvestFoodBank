package com.md.williamriesen.hawkeyeharvestfoodbank

class OrderBlank(catalog: Catalog) {
    val mapItemToCount = catalog.itemList.map { it to 0 }
}
