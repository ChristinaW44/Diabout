package com.example.diabout.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.diabout.R
import com.example.diabout.activities.LogIn

const val id = 1
const val channel = "notificationChanel"
const val reminderTitle= "Title"
const val message = "Message"

//registers and manages the reminders
class Broadcaster : BroadcastReceiver() {

    //will only run when the alarm calls it (when the notification needs to be sent)
    override fun onReceive(context: Context?, intent: Intent?) {

        //retrieves teh notification title and message
        val notificationTitle = intent!!.getStringExtra(reminderTitle)
        val notificationMessage = intent!!.getStringExtra(message)
        //creates a notification chanel to use
        val notificationChannel = NotificationChannel(id.toString(), channel,
            NotificationManager.IMPORTANCE_DEFAULT)
        //sets what happens when the notification is clicked
        val notificationIntent = Intent(context, LogIn::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 1, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)
        //creates the notification and styles it
        val notification = NotificationCompat.Builder(context!!, channel)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(notificationTitle)
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        //gets the notification manager, adds the channel and displays the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(id, notification)
    }
}
