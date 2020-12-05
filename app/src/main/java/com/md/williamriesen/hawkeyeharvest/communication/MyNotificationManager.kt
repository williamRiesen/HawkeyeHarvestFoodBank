package com.md.williamriesen.hawkeyeharvest.communication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.md.williamriesen.hawkeyeharvest.R
import com.md.williamriesen.hawkeyeharvest.orderoffsite.MainActivity

class MyNotificationManager(context: Context) {
    var mCtx =  context
    var mInstance: MyNotificationManager? = null

    companion object

    fun getInstance(context: Context): MyNotificationManager {
        if (mInstance == null) {
            mInstance = MyNotificationManager(context)
        }
        return mInstance as MyNotificationManager
    }

    fun displayNotification(title: String, body: String){
        val myChannel = "myChannel"
        val vibrateArray = longArrayOf(0,500, 1000)
        val mBuilder = NotificationCompat.Builder(mCtx,myChannel)
            .setSmallIcon(R.drawable.ic_hawkeye_harvest_food_bank_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setVibrate(vibrateArray)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
    val intent= Intent(mCtx, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(mCtx,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    mBuilder.setContentIntent(pendingIntent)
    val mNotificationManager= mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1,mBuilder.build())
    }
}