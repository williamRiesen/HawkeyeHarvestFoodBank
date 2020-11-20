package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.foodbank.FoodBank
import com.md.williamriesen.hawkeyeharvestfoodbank.orderoffsite.MainActivityViewModel
import java.text.SimpleDateFormat

class NotTakingOrdersNowMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_taking_orders_now_message)
        val foodBank = FoodBank()
        val simpleDateFormat = SimpleDateFormat("E, MMM d")
        val textViewNextDayOpen = findViewById<TextView>(R.id.textViewNextDayOpen)
        val nextDayOpen = simpleDateFormat.format(foodBank.nextDayOpen())
        textViewNextDayOpen.text = nextDayOpen
        val textViewNextDayTakingOrders = findViewById<TextView>(R.id.textViewNextDayTakingOrders)
        val nextDayTakingOrders= simpleDateFormat.format(foodBank.nextDayTakingOrders())
        textViewNextDayTakingOrders.text = nextDayTakingOrders
    }
}



