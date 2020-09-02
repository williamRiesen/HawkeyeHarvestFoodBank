package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_selection.*

class MainActivity : AppCompatActivity() {

//    private lateinit var adapter: ItemListAdapter
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
//        viewModel.sendCatalogToFireStore()
        viewModel.populateFoodCountMapFromFireStore()





//        db.collection("catalogs").document("catalog").set(catalog)

    }

//    fun setUpRecyclerView(foodCountMap: MutableLiveData<MutableMap<String, Int>>) {
//        FirestoreRecyclerOptions.Builder<Item>()
//        adapter = ItemListAdapter(foodCountMap)
//        val recyclerview = findViewById<RecyclerView>(R.id.view)
//        recyclerview.layoutManager = LinearLayoutManager(this)
//        recyclerview.adapter = adapter
//    }

    fun retrieveRecyclerView(): RecyclerView{
        return findViewById<RecyclerView>(R.id.recyclerviewChoices)
    }

    fun onAddItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.text_view_item_name).text.toString()
        viewModel.addItem(itemName)
//        adapter.notifyDataSetChanged()
    }

    fun onRemoveItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.text_view_item_name).text.toString()
        val updatedCount = viewModel.removeItem(itemName)
        val textViewCount = findViewById<TextView>(R.id.textView_count)
        textViewCount.text = updatedCount.toString()
    }

    fun onCartButtonClick(view: View) {
//        Navigation.findNavController(view).navigate(R.id.action_selectionFragment_to_titleFragment)
        Log.d("TAG", "onCartButtonClick triggered.")
        Navigation.findNavController(view)
            .navigate(R.id.action_selectionFragment_to_checkAndSubmitFragment)
    }
}

