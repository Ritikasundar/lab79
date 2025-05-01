package com.example.terminal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat




class smsActivity : AppCompatActivity() {
    private lateinit var phoneNumber: EditText
    private lateinit var message: EditText
    private lateinit var sendSmsButton: Button
    private lateinit var statusText: TextView

    companion object {
        const val SMS_PERMISSION_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sms)

        phoneNumber = findViewById(R.id.phoneNumber)
        message = findViewById(R.id.message)
        sendSmsButton = findViewById(R.id.sendSmsButton)
        statusText = findViewById(R.id.statusText)

        // Request SMS permission at runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }

        sendSmsButton.setOnClickListener {
            val phone = phoneNumber.text.toString().trim()
            val msg = message.text.toString().trim()

            if (phone.isNotEmpty() && msg.isNotEmpty()) {
                sendSms(phone, msg)
            } else {
                Toast.makeText(this, "Please enter phone number and message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSms(phone: String, msg: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phone, null, msg, null, null)
            statusText.text = "SMS Sent Successfully!"
            Toast.makeText(this, "SMS Sent!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            statusText.text = "SMS Failed to Send!"
            Toast.makeText(this, "SMS Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    }
