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
    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var retrievedCatalog: Catalog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        val catalog = Catalog(
            mutableListOf(
                "Ground Beef 1lb",
                "Pork & Bacon Sausage 1.5lb",
                "Pork Chops 1lb"
            )
        )
        db.collection("catalogs").document("catalog").set(catalog)
        val docRefCatalog = db.collection("catalogs").document("catalog")
        Log.d("TAG", "docReference: $docRefCatalog")
        docRefCatalog.get().addOnSuccessListener { documentSnapshot ->
            retrievedCatalog = documentSnapshot.toObject<Catalog>()!!
            Log.d("TAG", "Catalog retrieved not null.")
            Log.d("TAG", "Retrieved Catalog ${retrievedCatalog.itemList.toString()}")
            val orderBlank = OrderBlank(catalog)
            setUpRecyclerView(orderBlank)
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Catalog get failed with ", exception)
            }

//        Log.d("TAG", "Retrieved Catalog second reference ${retrievedCatalog.itemList.toString()}")

    }

    private fun setUpRecyclerView(orderBlank: OrderBlank) {
//        val query = catalog.orderBy("itemName", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Item>()
//            .setQuery(query, Item::class.java)
//            .build()
//        adapter = ItemAdapter(options)
        adapter = ItemListAdapter(orderBlank.mapItemToCount)
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

