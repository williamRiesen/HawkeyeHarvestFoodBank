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
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val catalog = db.collection("items")
    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel
    private var retrievedCatalog: Catalog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val city = City("Des Moines", "Iowa", true, 250000)
        db.collection("cities").document("DM").set(city)
        val docRefDM = db.collection("cities").document("DM")
        docRefDM.get().addOnSuccessListener { documentSnapshot ->
            val cityDM = documentSnapshot.toObject<City>()
            Log.d("TAG", "City name  ${cityDM?.name}")
        }

        val catalog = Catalog(
            mutableListOf(
                "Ground Beef 1lb",
                "Port & Bacon Sausage 1.5lb",
                "Pork Chops 1lb"
            )
        )
        db.collection("catalogs").document("catalog").set(catalog)
        val docRefCatalog = db.collection("catalogs").document("catalog")
        docRefCatalog.get().addOnSuccessListener { documentSnapshot ->
            retrievedCatalog = documentSnapshot.toObject<Catalog>()
            if (retrievedCatalog != null) {
                Log.d("TAG", "Catalog retrieved not null.")
                Log.d("TAG", "Retrieved Catalog ${retrievedCatalog!!.itemList.toString()}")
            } else
                Log.d("TAG", "Retrieved Catalog is null.")
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Catalog get failed with ", exception)
            }

        val docRef = db.collection("items").document("Pork & Bacon Sausage 1.5lb")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data ${document.data}")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
        setUpRecyclerView(catalog)
    }

    private fun setUpRecyclerView(retrievedCatalog: Catalog?) {
//        val query = catalog.orderBy("itemName", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Item>()
//            .setQuery(query, Item::class.java)
//            .build()
//        adapter = ItemAdapter(options)
        if (retrievedCatalog != null) {
            adapter = ItemListAdapter(retrievedCatalog.itemList)
        } else {
            Log.d("TAG", "catalog was null: adapter not initialize")
        }
        val recyclerview = findViewById<RecyclerView>(R.id.view)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

//    override fun onStart() {
//        super.onStart()
//        adapter.startListening()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        adapter.stopListening()
//    }

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

