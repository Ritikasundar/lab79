package com.example.terminal

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var resultText: TextView
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)

        nameInput = findViewById(R.id.editName)
        ageInput = findViewById(R.id.editAge)
        emailInput = findViewById(R.id.editEmail)
        resultText = findViewById(R.id.textResult)

        dbRef = FirebaseDatabase.getInstance().getReference("users")

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString()
            val email = emailInput.text.toString()
            val id = dbRef.push().key!!

            val user = User(id, name, age, email)
            dbRef.child(id).setValue(user)
                .addOnSuccessListener {
                    resultText.text = "User added!"
                }
                .addOnFailureListener {
                    resultText.text = "Failed to add user."
                }
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString()
            val email = emailInput.text.toString()
            val id = email.replace(".", "_")  // Using email as key

            val user = User(id, name, age, email)
            dbRef.child(id).setValue(user)
                .addOnSuccessListener {
                    resultText.text = "User updated!"
                }
                .addOnFailureListener {
                    resultText.text = "Failed to update user."
                }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val email = emailInput.text.toString()
            val id = email.replace(".", "_")

            dbRef.child(id).removeValue()
                .addOnSuccessListener {
                    resultText.text = "User deleted!"
                }
                .addOnFailureListener {
                    resultText.text = "Failed to delete user."
                }
        }

        findViewById<Button>(R.id.btnView).setOnClickListener {
            dbRef.get().addOnSuccessListener {
                val data = it.children.joinToString("\n") { snap ->
                    val user = snap.getValue(User::class.java)
                    "${user?.name}, ${user?.age}, ${user?.email}"
                }
                resultText.text = data
            }.addOnFailureListener {
                resultText.text = "Failed to fetch data."
            }
        }
    }
}
