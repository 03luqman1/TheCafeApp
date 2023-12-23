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
            val email = editTextEmailSignIn.text.toString()
            val password = editTextPassword.text.toString()

            if (validateInputs(email, password)) {
                // Inputs are valid, sign in with Firebase Authentication
                signIn(email, password)
            }
        }

        textViewSignUp.setOnClickListener {
            // Navigate to SignupActivity when textViewSignUp is clicked
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        // Validate each input field and show error messages if needed

        if (email.isEmpty()) {
            showToast("Please enter your email.")
            return false
        }

        if (password.isEmpty()) {
            showToast("Please enter your password.")
            return false
        }

        // All inputs are valid
        return true
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // User signed in successfully, get the user ID
                        val userId = user.uid

                        // Check the user type (Admin or Customer) based on the user ID
                        checkUserType(userId)
                    } else {
                        // Handle the case where the user is unexpectedly null
                        showToast("Authentication failed. User is null.")
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    // You can customize the error message based on the task.exception
                    // For simplicity, a generic error message is shown here.
                    showToast("Authentication failed. Check your credentials.")
                }
            }
    }

    private fun checkUserType(userId: String) {
        val adminRef = FirebaseDatabase.getInstance().getReference("Admin").child(userId)
        val customerRef = FirebaseDatabase.getInstance().getReference("Customers").child(userId)

        adminRef.get().addOnCompleteListener { adminTask ->
            if (adminTask.isSuccessful) {
                if (adminTask.result != null && adminTask.result.exists()) {
                    // User is an admin
                    showToast("ADMIN SIGN IN, PLEASE ADD INTENT TO NEXT SCREEN")
                } else {
                    // Check if the user is a customer
                    customerRef.get().addOnCompleteListener { customerTask ->
                        if (customerTask.isSuccessful) {
                            if (customerTask.result != null && customerTask.result.exists()) {
                                // User is a customer
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                showToast("User not found in database.")
                            }
                        } else {
                            showToast("Error checking customer user type.")
                        }
                    }
                }
            } else {
                showToast("Error checking admin user type.")
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

