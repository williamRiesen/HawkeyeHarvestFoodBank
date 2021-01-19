package com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.md.williamriesen.hawkeyeharvest.foodbank.Account
import com.md.williamriesen.hawkeyeharvest.foodbank.Order

class SecureTabletOrderViewModel : ViewModel() {


    fun lookUpAccount(accountNumber: Int, context: Context) {
        var accountID = ""
        val db = FirebaseFirestore.getInstance()
        val accountsRef = db.collection("accounts")
        Log.d("TAG", "ready to look up account using number $accountNumber")
        val query = accountsRef
            .whereEqualTo("accountNumber", accountNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                when (querySnapshot.size()) {
                    0 -> Toast.makeText(
                        context,
                        "No match found for this number.",
                        Toast.LENGTH_LONG
                    ).show()
                    1 ->
                        Toast.makeText(
                            context,
                            "AccountID is: ${querySnapshot.documents[0].id}",
                            Toast.LENGTH_LONG
                        ).show()
                    else -> Toast.makeText(
                        context,
                        "Multiple matches, should not be: please contact Dr. Riesen.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}