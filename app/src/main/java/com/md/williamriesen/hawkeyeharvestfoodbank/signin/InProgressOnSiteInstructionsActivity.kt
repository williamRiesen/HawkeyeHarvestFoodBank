package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import com.md.williamriesen.hawkeyeharvestfoodbank.communication.DisplayNumberActivity

class InProgressOnSiteInstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var accountID = intent.extras?.get("ACCOUNT_ID") as String
        setContentView(R.layout.activity_in_progress_on_site_instructions)
        val buttonShowNumber = findViewById<Button>(R.id.buttonShowNumber)
        buttonShowNumber.setOnClickListener {
            val intent = Intent(this, DisplayNumberActivity::class.java)
            intent.putExtra("ACCOUNT_ID", accountID)
            startActivity(intent)
        }
    }
}