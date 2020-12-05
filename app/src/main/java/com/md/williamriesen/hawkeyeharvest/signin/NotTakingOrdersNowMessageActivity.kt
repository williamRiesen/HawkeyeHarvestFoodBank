package com.md.williamriesen.hawkeyeharvest.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.foodbank.FoodBank
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
        val buttonSeeYouThen = findViewById<Button>(R.id.buttonSeeYouThen)
        buttonSeeYouThen.setOnClickListener {
            onBackPressed()
        }
    }
}



