package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val editTextFullName = findViewById<EditText>(R.id.editTextFullName)
        val editTextUsername = findViewById<EditText>(R.id.editTextEmailSignIn)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val editTextPassword = findViewById<EditText>(R.id.editTextNewPassword)
        val textViewMessage = findViewById<TextView>(R.id.textViewMessage)
        val textViewLogin = findViewById<TextView>(R.id.textViewSignIn)

        buttonRegister.setOnClickListener {
            val fullName = editTextFullName.text.toString().trim()
            val userName = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val phoneNumber = editTextPhoneNumber.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (validateInputs(fullName, userName, email, phoneNumber, password)) {
                // Inputs are valid, register the user in Firebase Authentication
                registerUser(email, password)
            }
        }

        textViewLogin.setOnClickListener {
            // Navigate to SignupActivity when textViewSignUp is clicked
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(
        fullName: String,
        userName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean {
        // Validate each input field and show error messages if needed

        if (fullName.isEmpty()) {
            showToast("Please enter your full name.")
            return false
        }

        if (userName.isEmpty()) {
            showToast("Please enter a username.")
            return false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address.")
            return false
        }

        if (phoneNumber.isEmpty() || !android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
            showToast("Please enter a valid phone number.")
            return false
        }

        if (password.isEmpty() || password.length < 6) {
            showToast("Please enter a password with at least 6 characters.")
            return false
        }

        // All inputs are valid
        return true
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    showToast("Registration successful.")

                    // Add user details to the Realtime Database
                    addUserDetailsToDatabase(user?.uid)

                    // You can add logic here to navigate to the main activity or do further processing
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If registration fails, display a message to the user.
                    // You can customize the error message based on the task.exception
                    // For simplicity, a generic error message is shown here.
                    showToast("Registration failed. ${task.exception?.message}")
                }
            }
    }

    private fun addUserDetailsToDatabase(userId: String?) {
        userId?.let {
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("Customers")

            val editTextFullName = findViewById<EditText>(R.id.editTextFullName)
            val editTextUsername = findViewById<EditText>(R.id.editTextEmailSignIn)
            val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
            val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)

            val fullName = editTextFullName.text.toString().trim()
            val userName = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val phoneNumber = editTextPhoneNumber.text.toString().trim()

            val userDetails = UserDetails(fullName, userName, email, phoneNumber)

            // Push user details to the "users" node using the user's UID as the key
            usersRef.child(userId).setValue(userDetails)
        }
    }

    private fun showToast(message: String) {
        // Helper function to show Toast messages
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    @Suppress("MissingSuperCall")
    override fun onBackPressed() {

        // Create an intent to start the MainActivity
        val intent = Intent(this, StartActivity::class.java)

        // Start the MainActivity
        startActivity(intent)

        // Finish the current activity
        finish()
    }
}
