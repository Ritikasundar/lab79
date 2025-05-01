package com.example.terminal

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.graphics.Bitmap
import android.net.wifi.WifiManager
import android.provider.Settings

class hardwareActivity : AppCompatActivity() {

    private lateinit var bluetoothButton: Button
    private lateinit var wifiButton: Button
    private lateinit var captureButton: Button
    private lateinit var imageView: ImageView
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var wifiManager: WifiManager

    private val CAMERA_PERMISSION_CODE = 101
    private val CAMERA_REQUEST_CODE = 102
    private val BLUETOOTH_PERMISSION_CODE = 103
    private val BLUETOOTH_ENABLE_REQUEST_CODE = 104

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hardware)

        // Initialize Views
        captureButton = findViewById(R.id.captureButton)
        bluetoothButton = findViewById(R.id.bluetoothButton)
        wifiButton = findViewById(R.id.wifiButton)
        imageView = findViewById(R.id.imageView)

        // Setup Camera
        setupCamera()

        // Setup Bluetooth
        setupBluetooth()

        // Setup WiFi
        setupWifi()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
    }

    // Camera Permissions and Functionality
    private fun setupCamera() {
        captureButton.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    // Bluetooth Permissions and Functionality
    private fun setupBluetooth() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        bluetoothButton.text = if (bluetoothAdapter.isEnabled) "Turn OFF Bluetooth" else "Turn ON Bluetooth"

        bluetoothButton.setOnClickListener {
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), BLUETOOTH_PERMISSION_CODE)
                return@setOnClickListener
            }

            if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable()
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_SHORT).show()
                bluetoothButton.text = "Turn ON Bluetooth"
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, BLUETOOTH_ENABLE_REQUEST_CODE)
            }
        }
    }

    // WiFi Permissions and Functionality
    private fun setupWifi() {
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiButton = findViewById(R.id.wifiButton)

        wifiButton.text = if (wifiManager.isWifiEnabled) "Turn OFF WiFi" else "Turn ON WiFi"

        wifiButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
                Toast.makeText(this, "Please toggle WiFi in settings", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    wifiManager.isWifiEnabled = !wifiManager.isWifiEnabled
                    Toast.makeText(
                        this,
                        if (wifiManager.isWifiEnabled) "WiFi turned on" else "WiFi turned off",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: SecurityException) {
                    Toast.makeText(this, "Cannot toggle WiFi", Toast.LENGTH_SHORT).show()
                }
            }

            wifiButton.text = if (wifiManager.isWifiEnabled) "Turn OFF WiFi" else "Turn ON WiFi"
        }
    }

}