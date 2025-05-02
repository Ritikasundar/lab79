package com.example.terminal

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class graphicsActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graphics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get references for the TextViews
        val titleTextView: TextView = findViewById(R.id.tvTitle)
        val descriptionTextView: TextView = findViewById(R.id.tvDescription)

        // Load animations
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        // Start animations
        titleTextView.startAnimation(fadeInAnimation)
        descriptionTextView.startAnimation(slideUpAnimation)

        // Get the ViewPager2
        viewPager = findViewById(R.id.imageSliderViewPager)

        // Create the image list
        val imageList = listOf(
            R.drawable.background_image,
            R.drawable.icon_image,
            // Add more images here
        )

        // Create the adapter
        val adapter = ImageSliderAdapter(imageList)

        // Set the adapter to the ViewPager2
        viewPager.adapter = adapter

    }

}