package com.example.terminal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class SharedPrefActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var age: EditText
    private lateinit var save: Button
    private lateinit var delete: Button
    private lateinit var tableLayout: TableLayout
    private lateinit var sharedPrefs: SharedPreferences

    private val PREF_KEY = "user_list"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_pref)

        name = findViewById(R.id.editTextName)
        age = findViewById(R.id.editTextAge)
        save = findViewById(R.id.buttonAdd)
        delete = findViewById(R.id.buttonDelete)
        tableLayout = findViewById(R.id.tableLayout)

        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        displayAllUsers()

        save.setOnClickListener {
            showAlertToSave()
        }

        delete.setOnClickListener {
            showAlertToDelete()
        }
    }

    private fun showAlertToSave() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm")
        builder.setMessage("Do you want to save or update this entry?")
        builder.setPositiveButton("Yes") { _, _ ->
            saveData()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showAlertToDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Entry")
        builder.setMessage("Enter name to delete:")
        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("Delete") { _, _ ->
            val toDelete = input.text.toString()
            deleteEntry(toDelete)
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun saveData() {
        val currName = name.text.toString()
        val currAge = age.text.toString()

        val data = JSONArray(sharedPrefs.getString(PREF_KEY, "[]"))

        var updated = false

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            if (obj.getString("name") == currName) {
                obj.put("age", currAge)
                updated = true
                break
            }
        }

        if (!updated) {
            val newUser = JSONObject()
            newUser.put("name", currName)
            newUser.put("age", currAge)
            data.put(newUser)
        }

        sharedPrefs.edit().putString(PREF_KEY, data.toString()).apply()
        displayAllUsers()
        name.text.clear()
        age.text.clear()
    }

    private fun deleteEntry(nameToDelete: String) {
        val data = JSONArray(sharedPrefs.getString(PREF_KEY, "[]"))
        val newArray = JSONArray()

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            if (obj.getString("name") != nameToDelete) {
                newArray.put(obj)
            }
        }

        sharedPrefs.edit().putString(PREF_KEY, newArray.toString()).apply()
        displayAllUsers()
    }

    private fun displayAllUsers() {
        tableLayout.removeAllViews() // Clear previous views

        val data = JSONArray(sharedPrefs.getString(PREF_KEY, "[]"))

        // Add headers for the table
        val headerRow = TableRow(this)
        val nameHeader = TextView(this)
        nameHeader.text = "Name"
        val ageHeader = TextView(this)
        ageHeader.text = "Age"
        headerRow.addView(nameHeader)
        headerRow.addView(ageHeader)
        tableLayout.addView(headerRow)

        // Add data rows for each user
        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val row = TableRow(this)

            val nameText = TextView(this)
            nameText.text = obj.getString("name")
            row.addView(nameText)

            val ageText = TextView(this)
            ageText.text = obj.getString("age")
            row.addView(ageText)

            tableLayout.addView(row)
        }
    }
}
