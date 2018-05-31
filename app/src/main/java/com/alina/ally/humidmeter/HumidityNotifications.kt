package com.alina.ally.humidmeter

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log

class HumidityNotifications  {
    private lateinit var context : Context
    private val ILOGTNOTIFICATIONS = "NOTIFICATIONS"
    @SuppressLint("RestrictedApi")
    fun createNotification(context: Context){
        this.context = context.applicationContext

        val notificationManager : NotificationManager = context.getSystemService(IntentService.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var notificationChannel : NotificationChannel =  NotificationChannel(MainActivity.NOTIFICATION_CHANNEL_ID, "Notification channel", importance);
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = context.resources.getColor(R.color.text_color)
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 200, 100 )
            notificationManager.createNotificationChannel(notificationChannel)
            Log.i(ILOGTNOTIFICATIONS, "Notification for Oreo")
        }

        val builder =  NotificationCompat.Builder(this.context, MainActivity.NOTIFICATION_CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_launcher_icon)
        builder.setContentTitle(context.resources.getString(R.string.notification_title))
        builder.setContentText(context.resources.getString(R.string.notification_text))
        builder.setAutoCancel(true)

        val intent: Intent = Intent(context, javaClass)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(context, MainActivity.PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        notificationManager.notify(MainActivity.NOTIFICATION_ID, builder.build())
        Log.i(ILOGTNOTIFICATIONS, "Notification sent")
    }
}