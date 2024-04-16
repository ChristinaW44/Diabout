package com.example.diabout.helpers

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.diabout.R

const val id = 1
const val channel = "channel1"
const val titleExtra = "Title"
const val message = "Message"

class Broadcaster : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notification : Notification = NotificationCompat.Builder(context!!, channel)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(intent!!.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(message))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(id, notification)
    }
}
