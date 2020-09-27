package com.md.williamriesen.hawkeyeharvestfoodbank

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
import java.time.Duration

class LoginActivity : AppCompatActivity() {

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
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
//            ,
//            AuthUI.IdpConfig.FacebookBuilder().build(),
//            AuthUI.IdpConfig.TwitterBuilder().build()
        )

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "requestCode: $requestCode, resultCode: $resultCode, data $data")
        if(requestCode == RC_SIGN_IN){
            var response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                var user = FirebaseAuth.getInstance().currentUser
                startActivity(Intent(this, MainActivity::class.java))
                Log.d("TAG", "Display Name: ${user?.displayName}")
            }
            else{
                if (response == null){
                    finish()
                }
                if (response?.error?.errorCode  == ErrorCodes.NO_NETWORK){
                        return
                }
                if (response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(this, response?.error?.errorCode.toString(), Toast.LENGTH_LONG)
                }
            }
        }
    }

    fun signOut(){
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
    }
}