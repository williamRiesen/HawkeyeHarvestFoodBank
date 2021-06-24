package com.md.williamriesen.hawkeyeharvest.reports

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvest.foodbank.Order
import com.md.williamriesen.hawkeyeharvest.foodbank.ZipCodeIndex
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class ReportCreator {

    private val monthReport = mutableListOf<ReportRow>()
    private var totalFamilies = 0
    private var totalPersons = 0
    private val zipCodeIndex = ZipCodeIndex()
    val db = FirebaseFirestore.getInstance()

    init {
        zipCodeIndex.zipCodeList.forEach { zipCode ->
            if (zipCode.zip != 50402) { //50402 is omitted because Mason City is the only locale with TWO zip codes
                val row = ReportRow(zipCode.city, zipCode.county, 0, 0)
                monthReport.add(row)
            }
            monthReport.sortWith(
                compareBy({ it.county }, { it.city })
            )
        }
    }

    fun createMonthReport2() {
        val startOfMonth = FoodBank().makeDate(Calendar.JANUARY, 1, 2021)
        val startOfNextMonth = FoodBank().makeDate(Calendar.FEBRUARY, 1, 2021)
        val timeStampStart: Timestamp = Timestamp(startOfMonth)
        val timeStampEnd: Timestamp = Timestamp(startOfNextMonth)
        val ordersRef = db.collection("orders")
        val accountsRef = db.collection("accounts")
        ordersRef.whereGreaterThanOrEqualTo("date", timeStampStart)
            .whereLessThan("date", timeStampEnd)
//            .limit(3)
            .get()


    }

        fun createMonthReport() {
            val startOfMonth = FoodBank().makeDate(Calendar.APRIL, 1, 2021)
            val startOfNextMonth = FoodBank().makeDate(Calendar.MAY, 1, 2021)
            val timeStampStart: Timestamp = Timestamp(startOfMonth)
            val timeStampEnd: Timestamp = Timestamp(startOfNextMonth)
            val ordersRef = db.collection("orders")
            val accountsRef = db.collection("accounts")
            ordersRef.whereGreaterThanOrEqualTo("date", timeStampStart)
                .whereLessThan("date", timeStampEnd)
//            .limit(3)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var documentCount = querySnapshot.size()
                    Log.d("TAG", "querySnapshot.size: ${querySnapshot.size()}")
                    val orders = querySnapshot.toObjects(Order().javaClass)
                    orders.forEach { order ->
                        if (order.orderState != "SAVED" && order.accountID != null && order.accountID != "") {
                            accountsRef.document(order.accountID!!).get()
                                .addOnSuccessListener { accountDocSnapshot ->
                                    Log.d(
                                        "TAG",
                                        "AccountID ${order.accountID}, date: ${order.date}, orderState: ${order.orderState}"
                                    )
                                    val thisCityRow = monthReport.find { reportRow ->
                                        reportRow.city == accountDocSnapshot["city"].toString()
                                    }
                                    if (thisCityRow != null) {
//                                    Log.d("TAG", "Found row with city: ${thisCityRow.city}")
                                        thisCityRow.families += 1
                                        val persons = accountDocSnapshot["familySize"].toString()
                                            .toInt()
                                        thisCityRow.persons += persons
                                        totalFamilies += 1
                                        totalPersons += persons

                                    }
                                Log.d("TAG", "documentCount: $documentCount")
                                    documentCount -= 1
                                    if (documentCount == 0) {
                                        Log.d("TAG", "Final report: $monthReport")
                                        Log.d(
                                            "TAG",
                                            "Total families: $totalFamilies, Total persons: $totalPersons"
                                        )
                                    }
                                }
                        } else documentCount -= 1
                    }

                }
        }
    }





