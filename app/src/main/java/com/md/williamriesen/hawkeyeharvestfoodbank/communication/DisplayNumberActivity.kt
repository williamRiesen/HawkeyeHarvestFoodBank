package com.md.williamriesen.hawkeyeharvestfoodbank.communication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.md.williamriesen.hawkeyeharvestfoodbank.R

class DisplayNumberActivity : AppCompatActivity() {

    lateinit var accountID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountID = intent.extras?.get("ACCOUNT_ID").toString()
        Log.d("TAG","accountID: $accountID")
        setContentView(R.layout.activity_display_number)
        val textViewAccountIdBig = findViewById<TextView>(R.id.textViewAccountIdBig)
        textViewAccountIdBig.text = accountID.takeLast(4)
    }
}