package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Get references to the buttons
        val buttonGoToMenu: Button = findViewById(R.id.buttonGoToMenu)
        val buttonLeaveReview: Button = findViewById(R.id.buttonLeaveReview)
        val buttonViewOrders: Button = findViewById(R.id.buttonViewOrders)
        val buttonSignOut: Button = findViewById(R.id.buttonSignOut)

        // Set up click listeners for the buttons
        buttonGoToMenu.setOnClickListener {
            // Handle the action for "Go to Menu"
            startActivity(Intent(this, MenuActivity::class.java))
        }

        buttonLeaveReview.setOnClickListener {
            // Handle the action for "Leave a Review"
            // Implement your logic here
            startActivity(Intent(this, ReviewActivity::class.java))

        }


        buttonViewOrders.setOnClickListener {
            // Handle the action for "View Orders"
            // Implement your logic here
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        buttonSignOut.setOnClickListener {
            // Handle the action for "Sign Out"
            // Implement your logic here
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }

    @Suppress("MissingSuperCall")
    override fun onBackPressed() {

        // Create an intent to start the MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Start the MainActivity
        startActivity(intent)

        // Finish the current activity
        finish()
    }




}
