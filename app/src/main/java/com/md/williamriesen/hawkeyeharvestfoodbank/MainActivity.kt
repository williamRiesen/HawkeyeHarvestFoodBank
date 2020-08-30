package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.populateFoodCountMapFromCode()

        viewModel.foodCountMap.observe(this, Observer { adapter.notifyDataSetChanged() })

        setUpRecyclerView(viewModel.foodCountMap)

//        val catalog = Catalog(
//            mutableMapOf(
//                "Pork Chops 1lb" to 0,
//                "Pork & Bacon Sausage 1.5lb" to 0,
//                "Ground Beef 1lb" to 0,
//                "Sliced Cooked Ham 2lb" to 0,
//                "Sliced Cotto Salami 2lb" to 0,
//                "Whole Chicken 3lb" to 0,
//                "Chicken Legs 5lb" to 0,
//                "Whole Ham" to 0,
//                "Catfish Fillets 2lb" to 0,
//                "Pork Loin 4lb" to 0,
//                "Chicken Thighs 5lb" to 0,
//                "Pork Shoulder Roast 6lb" to 0,
//                "Cooked Chicken Fajita 5lb" to 0,
//                "Cooked Chicken Fillets 5lb" to 0
//            )
//        )
//        db.collection("catalogs").document("catalog").set(catalog)

    }

    private fun setUpRecyclerView(foodCountMap: MutableLiveData<MutableMap<String, Int>>) {
        FirestoreRecyclerOptions.Builder<Item>()
        adapter = ItemListAdapter(foodCountMap)
        val recyclerview = findViewById<RecyclerView>(R.id.view)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    fun onAddItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.text_view_item_name).text.toString()
        viewModel.addItem(itemName)
        adapter.notifyDataSetChanged()
    }

    fun onRemoveItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.text_view_item_name).text.toString()
        val updatedCount = viewModel.removeItem(itemName)
        val textViewCount = findViewById<TextView>(R.id.textView_count)
        textViewCount.text = updatedCount.toString()
    }


}

