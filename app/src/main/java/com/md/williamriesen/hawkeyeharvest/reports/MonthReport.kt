package com.md.williamriesen.hawkeyeharvest.reports

class MonthReport() {
    val reportBody = mutableListOf<ReportRow>()

}

class ReportRow (val city: String, val county: String, var families: Int, var persons: Int){

    override fun toString(): String {
        return " \n $county, $city, $families, $persons"
    }

}