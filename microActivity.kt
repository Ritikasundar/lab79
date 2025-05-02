package com.example.terminal

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class microActivity : AppCompatActivity() {

    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var recordButton: Button
    private lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_micro)

        // Get references to the buttons
        recordButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set the file name using the correct directory
        fileName = getOutputFilePath()

        // Check for permission
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        }

        //set click listener for the buttons
        recordButton.setOnClickListener {
            startRecordingProcess()
        }

        stopButton.setOnClickListener {
            stopRecording()
        }

        findViewById<Button>(R.id.playButton).setOnClickListener {
            playRecording()
        }

    }

    //Set the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            Toast.makeText(this, "Permission is needed", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    //method to start the recording process
    private fun startRecordingProcess() {
        if (permissionToRecordAccepted) {
            startRecording()
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission is needed", Toast.LENGTH_SHORT).show()
        }
    }

    //start the recording
    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("microActivity", "prepare() failed")
            }

            start()
        }
    }

    //stop the recording
    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
    }

    //Play the recording
    private fun playRecording() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("microActivity", "play failed")
            }
        }
        player?.setOnCompletionListener {
            player?.release()
            player = null
        }
    }

    //get the right directory to save the file
    private fun getOutputFilePath(): String {
        val directory = getExternalFilesDir(null)
        val file = File(directory, "audiorecordtest.3gp")
        return file.absolutePath
    }
}