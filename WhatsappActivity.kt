package com.example.terminal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WhatsappActivity : AppCompatActivity() {
    private lateinit var statusText: TextView
    private val airplaneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                // Check the status of the Airplane Mode
                val isAirplaneModeOn = intent.getBooleanExtra("state", false)

                // Run on the UI thread to update the TextView
                runOnUiThread {
                    statusText.text = if (isAirplaneModeOn) {
                        "Airplane Mode: ON"
                    } else {
                        "Airplane Mode: OFF"
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_whatsapp)
        statusText = findViewById(R.id.statusText)

        // Handle edge-to-edge support for window insets (system bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        val messageEditText = findViewById<EditText>(R.id.messageEditText)
        val sendButton = findViewById<Button>(R.id.sendWhatsappButton)

        sendButton.setOnClickListener {
            val phone = phoneEditText.text.toString().trim()
            val message = messageEditText.text.toString().trim()

            if (phone.isNotEmpty() && message.isNotEmpty()) {
                sendWhatsappMessage(phone, message)
            } else {
                Toast.makeText(this, "Please enter phone and message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to send WhatsApp message using the wa.me URL
    private fun sendWhatsappMessage(phone: String, message: String) {
        // Format phone number by adding country code if missing
        val formattedNumber = if (phone.startsWith("+")) phone.substring(1) else "91$phone" // Assumes Indian numbers

        // Construct the URL to launch WhatsApp
        val url = "https://wa.me/$formattedNumber?text=${Uri.encode(message)}"

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Register the receiver to listen for Airplane Mode changes
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        try {
            // Unregister the receiver to prevent memory leaks
            unregisterReceiver(airplaneModeReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace() // Handle the case where the receiver wasn't registered
        }
    }
}
