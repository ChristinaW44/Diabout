package com.example.diabout.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
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

        //moves to the user details activity
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserDetails::class.java)
            startActivity(intent)
            finish()
        }

        timePicker = findViewById(R.id.timePicker)
        datePicker = findViewById(R.id.datePicker)
        repeatNeverRadio = findViewById(R.id.repeatNever)
        repeatHourlyRadio= findViewById(R.id.repeatHourly)
        repeatDailyRadio= findViewById(R.id.repeatDaily)
        repeatWeeklyRadio= findViewById(R.id.repeatWeekly)
        reminderText = findViewById(R.id.reminderMessage)


//        //sets a notification channel to add notifications to
//        val currentChannel = NotificationChannel(channel,
//            "Reminder", NotificationManager.IMPORTANCE_DEFAULT)
//        currentChannel.description = "User Set Reminder"
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(currentChannel)

        val setReminderButton = findViewById<Button>(R.id.setReminder)
        setReminderButton.setOnClickListener {
            setReminder()
        }

        val cancelReminderButton = findViewById<Button>(R.id.cancelAll)
        cancelReminderButton.setOnClickListener {
            //gets the system alarm manager
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(applicationContext, Broadcaster::class.java)
            val pendingIntent = PendingIntent.getBroadcast( //sets an intent that will be executed later
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE
            )

            //deletes the current reminder set
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this, "Reminder Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setReminder() {
        //gets the Broadcast receiver
        val intent = Intent(applicationContext, Broadcaster::class.java)
        val thisTitle = "Reminder"
        //adds the user's message to the reminder
        val thisMessage = reminderText.text.toString()
        intent.putExtra(reminderTitle,thisTitle)
        intent.putExtra(message,thisMessage)

        //sets an intent that will be executed later
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent,
            PendingIntent.FLAG_MUTABLE)

        //gets the system alarm manager
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmMilliseconds = getMilliseconds()

        //check how often the user wants the alarm to repeat
        if(repeatNeverRadio.isChecked){
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmMilliseconds,
                pendingIntent
            )
        } else{
            if(repeatHourlyRadio.isChecked){
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmMilliseconds,
                    1000 * 60 * 60 , pendingIntent) //interval need to be in milliseconds
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

    //gets the usr entered data and time in milliseconds
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