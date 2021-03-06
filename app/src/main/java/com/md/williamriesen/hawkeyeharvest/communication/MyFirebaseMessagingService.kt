package com.md.williamriesen.hawkeyeharvest.communication

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val myNotificationManager = MyNotificationManager(applicationContext)
        myNotificationManager.getInstance(applicationContext).displayNotification(title!!, body!!)
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("onMessageReceived", "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d("onMessageReceived", "Message data payload: " + remoteMessage.data)
            if ( /* Check if data needs to be processed by long running job */true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob()
            } else {
                // Handle message within 10 seconds
//                handleNow()
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(
                "onMessageReceived",
                "Message Notification Body: " + remoteMessage.notification!!.body
            )
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

//    override fun onNewToken(p0: String?) {
//        super.onNewToken(p0)
//        val token = p0
//        Log.d("TAG", "Token: $token")
//    }


    fun getToken(context: Context): String? {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty")
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("TAG", "Token: $p0")
    }


}