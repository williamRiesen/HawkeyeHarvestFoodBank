package com.md.williamriesen.hawkeyeharvestfoodbank

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class MyNotificationManager(context: Context) {
    var mCtx =  context
    var mInstance: MyNotificationManager? = null

    companion object


    fun getInstance(context: Context): MyNotificationManager{
        if (mInstance == null) {
            mInstance = MyNotificationManager(context)
        }
        return mInstance as MyNotificationManager
    }

    fun displayNotification(title: String, body: String){
        val mBuilder = NotificationCompat.Builder(mCtx)
            .setSmallIcon(R.drawable.ic_hawkeye_harvest_food_bank_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setDefaults(Notification.DEFAULT_LIGHTS)
    val intent= Intent(mCtx, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(mCtx,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    mBuilder.setContentIntent(pendingIntent)
    val mNotificationManager= mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1,mBuilder.build())
    }
}