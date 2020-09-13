package com.md.williamriesen.hawkeyeharvestfoodbank

class Category {
    var name = ""
    var pointsAllocated = 0
    var pointsUsed = 0
    fun calculatePoints(familySize: Int) = familySize * 2
    }
