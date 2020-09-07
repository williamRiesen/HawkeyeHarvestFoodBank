package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.lifecycle.MutableLiveData

data class Order(
    val itemMap: MutableMap<String, Int> = mutableMapOf()
)