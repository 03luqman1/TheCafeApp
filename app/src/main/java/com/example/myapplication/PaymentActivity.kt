package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Retrieve total cost and order ID from the intent
        val totalCost = intent.getDoubleExtra("totalCost", 0.0)
        val orderId = intent.getStringExtra("orderId")

        // Set the total cost in the TextView
        val textViewTotalCost: TextView = findViewById(R.id.textViewTotalCost)
        val formattedTotal = "$${DecimalFormat("#.##").format(totalCost)}"
        textViewTotalCost.text = "Total Cost: $formattedTotal"

        val buttonPayPal: Button = findViewById(R.id.buttonPayPal)
        val buttonCreditDebitCard: Button = findViewById(R.id.buttonCreditDebitCard)

        buttonPayPal.setOnClickListener {
            handlePaymentButtonClick("PayPal")
        }

        buttonCreditDebitCard.setOnClickListener {
            handlePaymentButtonClick("Credit/Debit Card")
        }
    }

    private fun handlePaymentButtonClick(paymentType: String) {
        // Retrieve total cost and order ID from the intent
        val totalCost = intent.getDoubleExtra("totalCost", 0.0)
        val orderId = intent.getStringExtra("orderId")

        // Gather payment details
        val paymentId = generatePaymentId()

        // Use the actual total cost
        val amount = totalCost

        val paymentDate = getCurrentDateTime()

        // Set up payment details
        val paymentDetails = PaymentDetails(paymentId, orderId, paymentType, amount, paymentDate)

        // Add payment details to the "Payment" node
        val paymentsRef = database.reference.child("Payments")
        paymentsRef.child(paymentId).setValue(paymentDetails)

        // Handle further actions, e.g., navigate to a confirmation screen
        // ...
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

        // Display a toast or message indicating successful payment
        showToast("Payment successful")
    }

    private fun showToast(message: String) {
        // Helper function to show Toast messages
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        // You can implement this method based on your preferred way of displaying messages
    }

    private fun generatePaymentId(): String {
        // Generate a unique payment ID (you can customize this based on your needs)
        return UUID.randomUUID().toString()
    }

    private fun getCurrentDateTime(): String {
        // Get the current date and time in a specified format
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
