package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val buttonSignIn = findViewById<Button>(R.id.buttonSignIn)
        val editTextEmailSignIn = findViewById<EditText>(R.id.editTextEmailSignIn)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val textViewSignUp = findViewById<TextView>(R.id.textViewSignUp)

        buttonSignIn.setOnClickListener {
            val username = editTextEmailSignIn.text.toString()
            val password = editTextPassword.text.toString()

            if (validateInputs(username, password)) {
                // Inputs are valid, sign in with Firebase Authentication
                signIn(username, password)
            }
        }

        textViewSignUp.setOnClickListener {
            // Navigate to SignupActivity when textViewSignUp is clicked
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        // Validate each input field and show error messages if needed

        if (username.isEmpty()) {
            showToast("Please enter your username.")
            return false
        }

        if (password.isEmpty()) {
            showToast("Please enter your password.")
            return false
        }

        // All inputs are valid
        return true
    }

    private fun signIn(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    // You can add logic here to navigate to the main activity or do further processing
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    // You can customize the error message based on the task.exception
                    // For simplicity, a generic error message is shown here.
                    showToast("Authentication failed. Check your credentials.")
                }
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

