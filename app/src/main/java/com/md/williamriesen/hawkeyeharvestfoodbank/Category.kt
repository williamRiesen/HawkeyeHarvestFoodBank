package com.md.williamriesen.hawkeyeharvestfoodbank

class Category() {
//    var pointsAllocated: Int = 0
//    var pointsUsed: Int = 0
    var name: String = ""
    var pointsPerFamilyMember: Int =0
    var additionalPointsPerFamily: Int = 0

   constructor (name: String,pointsPerFamilyMember: Int, additionalPointsPerFamily: Int) : this() {
       this.name = name
       this.pointsPerFamilyMember = pointsPerFamilyMember
       this.additionalPointsPerFamily = additionalPointsPerFamily
 }

    fun calculatePoints(familySize: Int): Int {
        return pointsPerFamilyMember * familySize + additionalPointsPerFamily
    }
}



