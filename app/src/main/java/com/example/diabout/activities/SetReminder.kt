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
import android.widget.RadioGroup
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
    lateinit var repeatNeverRadio: RadioButton
    lateinit var repeatHourlyRadio: RadioButton
    lateinit var repeatDailyRadio: RadioButton
    lateinit var repeatWeeklyRadio: RadioButton
    lateinit var reminderText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_reminder)

        val intent = intent
        val userID = intent.getStringExtra("ID")

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("ID", userID)
            startActivity(intent)
            finish()
        }

        timePicker = findViewById<TimePicker>(R.id.timePicker)
        datePicker = findViewById<DatePicker>(R.id.datePicker)

        repeatNeverRadio = findViewById<RadioButton>(R.id.repeatNever)
        repeatHourlyRadio= findViewById<RadioButton>(R.id.repeatHourly)
        repeatDailyRadio= findViewById<RadioButton>(R.id.repeatDaily)
        repeatWeeklyRadio= findViewById<RadioButton>(R.id.repeatWeekly)

        reminderText = findViewById<EditText>(R.id.reminderMessage)

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
                PendingIntent.FLAG_MUTABLE
            )
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this, "Reminder Deleted", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleReminder() {
        val intent = Intent(applicationContext, Broadcaster::class.java)
        val thisTitle = "Reminder"
        val thisMessage = reminderText.text.toString()
        intent.putExtra(titleExtra,thisTitle)
        intent.putExtra(message,thisMessage)

        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent,
            PendingIntent.FLAG_MUTABLE)
        
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        if(repeatNeverRadio.isChecked){
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        } else{
            if(repeatHourlyRadio.isChecked){
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    1000 * 60 * 60 ,
                    pendingIntent)
            } else if(repeatDailyRadio.isChecked){
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    1000 * 60 * 60 * 24,
                    pendingIntent)
            } else{
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    1000 * 60 * 60 * 24 * 7,
                    pendingIntent)
            }
        }


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