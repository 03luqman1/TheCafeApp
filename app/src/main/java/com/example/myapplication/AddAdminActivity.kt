package com.example.myapplication

import android.widget.Button


import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddAdminActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextFullName: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextUserName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextUserName = findViewById(R.id.editTextUserName)

        findViewById<Button>(R.id.buttonAddAdmin).setOnClickListener {
            // Get admin details from the form
            val email = editTextEmail.text.toString().trim()
            val fullName = editTextFullName.text.toString().trim()
            val phoneNumber = editTextPhoneNumber.text.toString().trim()
            val userName = editTextUserName.text.toString().trim()

            // Validate input fields
            if (email.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty() || userName.isEmpty()) {
                Toast.makeText(this@AddAdminActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Add the admin to Firebase
            addAdminToFirebase(email, fullName, phoneNumber, userName)
        }
    }

    private fun addAdminToFirebase(
        email: String,
        fullName: String,
        phoneNumber: String,
        userName: String
    ) {
        val adminsRef = FirebaseDatabase.getInstance().getReference("Admin")

        // Generate a unique key for the new admin
        val adminId = adminsRef.push().key

        // Create a new Admin object
        val newAdmin = Admin(email, fullName, phoneNumber, userName)

        // Add the new admin to the "Admin" node using the generated key
        adminId?.let {
            adminsRef.child(it).setValue(newAdmin)

            // Display a success message or perform any other actions needed
            Toast.makeText(this, "Admin added successfully", Toast.LENGTH_SHORT).show()
        }

    }
}
