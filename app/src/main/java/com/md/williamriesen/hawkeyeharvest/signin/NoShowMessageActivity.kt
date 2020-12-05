package com.md.williamriesen.hawkeyeharvest.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.md.williamriesen.hawkeyeharvest.R

class NoShowMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_show_message)
        val exitButton = findViewById<Button>(R.id.button_exit)
        exitButton.setOnClickListener {
            onBackPressed()
        }
    }
}