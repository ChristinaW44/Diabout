package com.example.diabout.helpers

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.diabout.R

const val id = 1
const val channel = "notificationChanel"
const val reminderTitle= "Title"
const val message = "Message"

//registers and manages the reminders
class Broadcaster : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notification : Notification = NotificationCompat.Builder(context!!, channel)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(intent!!.getStringExtra(reminderTitle))
            .setContentText(intent.getStringExtra(message))
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }
}
