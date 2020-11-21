package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.communication.DisplayNumberActivity
import org.w3c.dom.Text

class PickUpLaterTodayInstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accountID = intent.extras?.get("ACCOUNT_ID") as String
        var pickUpHour24 = intent.extras["PICKUP_HOUR24"] as Int
        val startOfWindow = pickUpHour24 - 12
        val endOfWindow = startOfWindow + 1
        setContentView(R.layout.activity_pick_up_later_today_instructions)
        val textViewMessage = findViewById<TextView>(R.id.textViewMessage)
        textViewMessage.text = "Your order will be packed from pickup between $startOfWindow and $endOfWindow today. Please go to the food bank at that time. Show the order number on the next screen to food bank staff."

        val buttonShowNumber = findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.setOnClickListener {
            val intent = Intent(this, DisplayNumberActivity::class.java)
            intent.putExtra("ACCOUNT_ID", accountID)
            startActivity(intent)
        }
    }
}