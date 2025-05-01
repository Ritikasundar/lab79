package com.example.terminal

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DateActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_date)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var calendar=findViewById<CalendarView>(R.id.calendarView)
        var timepicker=findViewById<TimePicker>(R.id.textClock)
        var button=findViewById<Button>(R.id.button6)

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M){
            timepicker.setIs24HourView(true)
        }

        button.setOnClickListener{
            var hour=timepicker.hour
            var min=timepicker.minute
            var timeselected=String.format("%02d:%02d",hour,min)

            var cal=calendar.date
            var dateselected=android.text.format.DateFormat.format("dd/MM/yy",cal)
            Toast.makeText(this,"time:$timeselected | date:$dateselected",Toast.LENGTH_SHORT).show()
        }

    }
}

