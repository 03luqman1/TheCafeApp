package com.example.myapplication

import android.content.Intent
import android.os.Bundle
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
    private var currentToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Retrieve total cost and order ID from the intent
        val totalCost = intent.getDoubleExtra("totalCost", 0.0)
        //val orderId = intent.getStringExtra("orderId")

        // Set the total cost in the TextView
        val textViewTotalCost: TextView = findViewById(R.id.textViewTotalCost)
        val formattedTotal = "£${DecimalFormat("#.##").format(totalCost)}"
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

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

        showToast("Payment successful - Order placed")
    }

    private fun showToast(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    private fun generatePaymentId(): String {
        return UUID.randomUUID().toString()
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
