package com.md.williamriesen.hawkeyeharvest.signin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.md.williamriesen.hawkeyeharvest.orderoffsite.MainActivity
import com.md.williamriesen.hawkeyeharvest.manager.ManagerActivity
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.orderwithsecuretablet.SecureTabletOrderActivity
import com.md.williamriesen.hawkeyeharvest.volunteer.VolunteerActivity

class SignStaffInActivity : AppCompatActivity() {

    val RC_SIGN_IN = 9999
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        createLogInUI()
    }

    fun createLogInUI() {
        // Choose authentication providers
        val providers = arrayListOf(

            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
//            AuthUI.IdpConfig.AnonymousBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build(),
//            AuthUI.IdpConfig.TwitterBuilder().build()
        )

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_hawkeye_harvest_food_bank_logo)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var isManager = false
        var isVolunteer = false
        Log.d("TAG", "requestCode: $requestCode, resultCode: $resultCode, data $data")
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                val token = user?.getIdToken(false)
                token?.addOnSuccessListener { token ->
                    if (token.claims.containsKey("manager")) {
                        isManager = token.claims.getValue("manager") as Boolean
                    }
                    if (token.claims.containsKey("volunteer")) {
                        isVolunteer = token.claims.getValue("volunteer") as Boolean
                    }
                    Log.d("TAG", "isManager: $isManager")
                    Log.d("TAG", "isVolunteer: $isVolunteer")
                    when {
                        (isManager) -> {
                            Log.d("TAG","going to Start Activity")
                            startActivity(Intent(this,SecureTabletOrderActivity::class.java))
                        }
                        (isVolunteer) -> {
                            startActivity(Intent(this, VolunteerActivity::class.java))
                        }
                        else -> {

                                startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
            } else {
                if (response == null) {
                    finish()
                }
                if (response?.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    return
                }
                if (response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(
                        this,
                        response.error?.errorCode.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
    }
}