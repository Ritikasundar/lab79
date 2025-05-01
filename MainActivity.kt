package com.example.terminal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val button=findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent=Intent(this,NotificationActivity::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener{
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=Google+HQ"))
            startActivity(intent)
        }

        val button3=findViewById<Button>(R.id.button3)
        registerForContextMenu(button3)

        val button4=findViewById<Button>(R.id.button4)
        button4.setOnClickListener{view->
            val popup = PopupMenu(this,view)
            popup.menuInflater.inflate(R.menu.popup_menu,popup.menu)

            popup.setOnMenuItemClickListener { item->
                when(item.itemId){
                    R.id.share->{
                        Toast.makeText(this,"share clicked",Toast.LENGTH_SHORT).show()
                        true
                    }
                    else->false
                }

            }
            popup.show()
        }





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.about ->{
                Toast.makeText(this,"clicked about page",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.notification->{
                val intent=Intent(this,NotificationActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.time->{
                val intent=Intent(this,DateActivity::class.java)
                startActivity(intent)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }


    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu,menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.edit->{
                Toast.makeText(this,"edit clicked",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.delete->{
                Toast.makeText(this,"delete clicked",Toast.LENGTH_SHORT).show()
                true
            }
            else -> return super.onContextItemSelected(item)
        }

    }
}