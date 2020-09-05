package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

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


    fun retrieveRecyclerView(): RecyclerView{
        return findViewById<RecyclerView>(R.id.recyclerviewChoices)
    }

    fun onAddItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.textview_item_name).text.toString()
        viewModel.addItem(itemName)
//        adapter.notifyDataSetChanged()
    }

    fun onRemoveItem(view: android.view.View) {
        val itemName = findViewById<TextView>(R.id.textview_item_name).text.toString()
        val updatedCount = viewModel.removeItem(itemName)
        val textViewCount = findViewById<TextView>(R.id.textView_count)
        textViewCount.text = updatedCount.toString()
    }

    fun onCartButtonClick(view: View) {
//        Navigation.findNavController(view).navigate(R.id.action_selectionFragment_to_titleFragment)
        Log.d("TAG", "onCartButtonClick triggered.")
        Navigation.findNavController(view)
            .navigate(R.id.action_selectionFragment_to_checkoutFragment)
        Log.d ("TAG", "order = ${viewModel.order}")
    }

    fun onShopButtonClick(view: View){

        val editTextAccountID = findViewById<EditText>(R.id.editTextAccountID)
         val accountID = editTextAccountID.text.toString()
        Log.d ("TAG", "accountID: $accountID")
        viewModel.signIn(accountID)
        Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_selectionFragment)
    }

    fun onSubmitButtonClick(view: View){
        viewModel.submitOrder()
    }


}

