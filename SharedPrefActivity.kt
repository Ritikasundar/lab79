package com.example.terminal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SharedPrefActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var age: EditText
    private lateinit var save: Button
    private lateinit var delete: Button
    private lateinit var result: TextView
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shared_pref)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.editTextText)
        age = findViewById(R.id.editTextText2)
        save = findViewById(R.id.button5)
        delete = findViewById(R.id.buttonClear)
        result = findViewById(R.id.textView)

        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        var savedname=sharedPrefs.getString("name"," ")
        var savedage=sharedPrefs.getString("age"," ")
        result.text="name:$savedname age:$savedage"

        save.setOnClickListener{
            showalert()
        }

        delete.setOnClickListener{
            with(sharedPrefs.edit()){
                clear()
                apply()
            }
            name.text.clear()
            age.text.clear()
        }


    }
    private fun showalert(){
        var builer= AlertDialog.Builder(this)
        builer.setTitle("conform")
        builer.setMessage("want to save")
        builer.setPositiveButton("Yes") { _, _ ->
            savsData()
        }
        builer.setNegativeButton("No"){dialog,_->
            dialog.dismiss()
        }
        builer.create().show()
    }

    private fun savsData(){
        var currname=name.text.toString()
        var currage=age.text.toString()

        with(sharedPrefs.edit()){
            putString("name",currname)
            putString("age",currage)
            apply()

        }
        result.text="name:$currname age:$currage"
    }
    }
