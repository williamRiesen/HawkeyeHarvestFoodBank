package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = Firebase.firestore
        val accounts = db.collection("accounts")
        val docRef = accounts.document("00tTuTdG9uGGBYQFTfrm")
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
//                Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                val accountTextView = findViewById<TextView>(R.id.textView2)
                val familySizeTextView = findViewById<TextView>(R.id.familySize)
//                account.text = document.data.toString()
                val familySize = document.data?.get("familySize")
                val accountID = document.data?.get("accountID")
//                Log.d("TAG", "familySize $familySize")
                accountTextView.text = "Account ID: $accountID"
                familySizeTextView.text = "Family Size: $familySize"
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }
}

