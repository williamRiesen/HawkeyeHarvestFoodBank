package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.md.williamriesen.hawkeyeharvestfoodbank.R

class PickUpTomorrowInstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pickUpHour24 = intent.extras["PICKUP_HOUR24"] as Int
        val startOfWindow = pickUpHour24 - 12
        val endOfWindow = startOfWindow + 1
        setContentView(R.layout.activity_pick_up_tomorow_instructions)
        val textViewMessage = findViewById<TextView>(R.id.textViewMessage)
        textViewMessage.text = "Your order has been received. Please go to the food bank tomorrow between $startOfWindow and $endOfWindow to pick it up. "
    }
}