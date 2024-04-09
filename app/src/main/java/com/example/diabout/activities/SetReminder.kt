package com.example.diabout.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diabout.R
import com.example.diabout.helpers.Broadcaster
import com.example.diabout.helpers.channel
import com.example.diabout.helpers.id
import com.example.diabout.helpers.message
import com.example.diabout.helpers.titleExtra
import java.text.DateFormat
import java.util.Calendar
import java.util.Date


class SetReminder : AppCompatActivity() {

    lateinit var timePicker: TimePicker
    lateinit var datePicker: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_reminder)

        timePicker = findViewById<TimePicker>(R.id.timePicker)
        datePicker = findViewById<DatePicker>(R.id.datePicker)

        createReminderChannel()
        val setRem = findViewById<Button>(R.id.setReminder)
        setRem.setOnClickListener { scheduleReminder() }

        val cancelRem = findViewById<Button>(R.id.cancelAll)
        cancelRem.setOnClickListener {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(applicationContext, Broadcaster::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.cancel(pendingIntent)
        }

    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleReminder() {
        val intent = Intent(applicationContext, Broadcaster::class.java)
        val thisTitle = "Test Title"
        val thisMessage = "Test message"
        val id = System.currentTimeMillis().toInt()
        intent.putExtra(titleExtra,thisTitle)
        intent.putExtra(message,thisMessage)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        Toast.makeText(applicationContext, "Reminder Set", Toast.LENGTH_LONG).show()
    }


    private fun getTime(): Long {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year,month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createReminderChannel() {
        val name = "Reminder channel"
        val desc = "A description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val thisChannel = NotificationChannel(channel, name, importance)
        thisChannel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(thisChannel)
    }
}