package com.example.terminal

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AboutActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playAudioButton = findViewById<Button>(R.id.playAudioButton)
        val stopAudioButton = findViewById<Button>(R.id.stopAudioButton)
        val playVideoButton = findViewById<Button>(R.id.playVideoButton)
        val pauseVideoButton = findViewById<Button>(R.id.pauseVideoButton)
        videoView = findViewById(R.id.videoView)

        // Setup Audio
        playAudioButton.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.uyirey) // Put sample_audio.mp3 in res/raw
                mediaPlayer?.isLooping = false
            }
            mediaPlayer?.start()
        }

        stopAudioButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        // Setup Video
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.sample_video}") // Put sample_video.mp4 in res/raw
        videoView.setVideoURI(videoUri)

        playVideoButton.setOnClickListener {
            videoView.start()
        }

        pauseVideoButton.setOnClickListener {
            videoView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
