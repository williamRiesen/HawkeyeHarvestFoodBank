package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val catalog = db.collection("items")
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()


//        val db = Firebase.firestore
//        val accounts = db.collection("accounts")
//        val items = db.collection("items")
//        val myItems = items.get().toString()
////        myItems.result?.documents
//        val docRef = accounts.document("00tTuTdG9uGGBYQFTfrm")
//        docRef.get().addOnSuccessListener { document ->
//            if (document != null) {
//                val accountTextView = findViewById<TextView>(R.id.textView2)
//                val familySizeTextView = findViewById<TextView>(R.id.familySize)
//
//                // test conversion JSON to data class here
//                val json = """{"itemID": "12345", "itemName" : "Pork chop", "limit": "2", "countInStock":"24"}"""
//                val gson = Gson()
//
//                val item1: Item = gson.fromJson(json, Item::class.java)
//                val account2: Account = gson.fromJson(document.data.toString(), Account::class.java)
//                Log.d("JSON conversion test",account2.accountID.toString())
//                // end of JSON experiment
//
//                val familySize = document.data?.get("familySize")
//                val accountID = document.data?.get("accountID")
////                 account2.accountID.toString()
//                accountTextView.text = myItems
//                familySizeTextView.text = "Family Size: $familySize"
//            } else {
//                Log.d("TAG", "No such document")
//            }
//        }
//            .addOnFailureListener { exception ->
//                Log.d("TAG", "get failed with ", exception)
//            }
    }

    private fun setUpRecyclerView() {
        val query = catalog.orderBy("itemName", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()
        adapter = ItemAdapter(options)
        val recyclerview = findViewById<RecyclerView>(R.id.view)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }
    //

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}

