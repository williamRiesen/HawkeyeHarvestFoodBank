package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log

class Category() {
    var pointsUsed: Int = 0
    var name: String = ""
    var pointsPerFamilyMember: Int = 0
    var additionalPointsPerFamily: Int = 0
    var id: Int = 0

    constructor (
        id: Int,
        name: String,
        pointsPerFamilyMember: Int,
        additionalPointsPerFamily: Int
    ) : this() {
        this.id = id
        this.name = name
        this.pointsPerFamilyMember = pointsPerFamilyMember
        this.additionalPointsPerFamily = additionalPointsPerFamily
    }

    fun calculatePoints(familySize: Int): Int {
//        Log.d("TAG" ,"calculatePoints familySize: $familySize")
//        Log.d("TAG", "pointsPerFamilyMember: $pointsPerFamilyMember")
//        Log.d("TAG", "additionalPointsPerFamily: $additionalPointsPerFamily")
        return pointsPerFamilyMember * familySize + additionalPointsPerFamily
    }
}



