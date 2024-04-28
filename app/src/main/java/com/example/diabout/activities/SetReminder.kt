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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diabout.R
import com.example.diabout.helpers.Broadcaster
import com.example.diabout.helpers.channel
import com.example.diabout.helpers.message
import com.example.diabout.helpers.reminderTitle
import java.util.Calendar


class SetReminder : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var datePicker: DatePicker
    private lateinit var repeatNeverRadio: RadioButton
    private lateinit var repeatHourlyRadio: RadioButton
    private lateinit var repeatDailyRadio: RadioButton
    private lateinit var repeatWeeklyRadio: RadioButton
    private lateinit var reminderText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_reminder)

//        val intent = intent
//        val userID = intent.getStringExtra("ID")
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("ID", "0")

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        timePicker = findViewById<TimePicker>(R.id.timePicker)
        datePicker = findViewById(R.id.datePicker)

        repeatNeverRadio = findViewById(R.id.repeatNever)
        repeatHourlyRadio= findViewById(R.id.repeatHourly)
        repeatDailyRadio= findViewById(R.id.repeatDaily)
        repeatWeeklyRadio= findViewById(R.id.repeatWeekly)

        reminderText = findViewById(R.id.reminderMessage)


        val currentChannel = NotificationChannel(channel,
            "Reminder", NotificationManager.IMPORTANCE_DEFAULT)
        currentChannel.description = "User Set Reminder"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(currentChannel)

        val setReminderButton = findViewById<Button>(R.id.setReminder)
        setReminderButton.setOnClickListener {
            setReminder()
        }

        val cancelRem = findViewById<Button>(R.id.cancelAll)
        cancelRem.setOnClickListener {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(applicationContext, Broadcaster::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this, "Reminder Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setReminder() {
        val intent = Intent(applicationContext, Broadcaster::class.java)
        val thisTitle = "Reminder"
        val thisMessage = reminderText.text.toString()
        intent.putExtra(reminderTitle,thisTitle)
        intent.putExtra(message,thisMessage)

        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent,
            PendingIntent.FLAG_MUTABLE)
        
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmMilliseconds = getMilliseconds()

        if(repeatNeverRadio.isChecked){
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmMilliseconds,
                pendingIntent
            )
        } else{
            if(repeatHourlyRadio.isChecked){
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmMilliseconds,
                    1000 * 60 * 60 , pendingIntent)
            } else if(repeatDailyRadio.isChecked){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmMilliseconds,
                    1000 * 60 * 60 * 24, pendingIntent)
            } else{
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP, alarmMilliseconds,
                    1000 * 60 * 60 * 24 * 7, pendingIntent)
            }
        }
        Toast.makeText(applicationContext, "Reminder Set", Toast.LENGTH_LONG).show()
    }

    private fun getMilliseconds(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth,
            timePicker.hour,
            timePicker.minute)
        return calendar.timeInMillis
    }
}