package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.core.View
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val catalog = db.collection("items")
    private lateinit var adapter: ItemAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
//        val cities = db.collection("cities")
//        val data1 = mapOf(
//            "name" to "San Francisco",
//            "state" to "CA",
//            "capital" to false,
//            "population" to 860000
//        )
//        cities.document("SF").set(data1)
//        val data3 = mapOf(
//            "name" to "Washington, D.C.",
//            "state" to null,
//            "capital" to true,
//            "population" to 680000
//        )
//        cities.document("DC").set(data3)

        val docRef = db.collection("cities").document("SF")
        docRef.get()
            .addOnSuccessListener { document->
                if (document != null){
                    Log.d("TAG","DocumentSnapshot data ${document.data}")
                } else{
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("TAG", "get failed with ", exception)
            }
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

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    fun onAddItem(view: android.view.View) {

        val itemName = findViewById<TextView>(R.id.text_view_item_name).text.toString()
        val updatedCount = viewModel.addItem(itemName)
        val textViewCount = findViewById<TextView>(R.id.textView_count)
//        textViewCount.text = updatedCount.toString()
    }

    fun onRemoveItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.text_view_item_name).text.toString()
        val updatedCount = viewModel.removeItem(itemName)
        val textViewCount = findViewById<TextView>(R.id.textView_count)
        textViewCount.text = updatedCount.toString()
    }

}

