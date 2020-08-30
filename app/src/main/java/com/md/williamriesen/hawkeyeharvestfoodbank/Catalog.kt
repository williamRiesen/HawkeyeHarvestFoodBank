package com.md.williamriesen.hawkeyeharvestfoodbank

data class Catalog(
    val itemList: MutableMap<String, Int> = mutableMapOf()
)