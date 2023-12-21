package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class BasketActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var basketViewModel: BasketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        auth = FirebaseAuth.getInstance()
        basketViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)

        // Retrieve basket items from the intent only if the ViewModel is empty
        if (basketViewModel.basketItems.isEmpty()) {
            val intent = intent
            if (intent.hasExtra("selectedItems")) {
                basketViewModel.basketItems.addAll(intent.getStringArrayListExtra("selectedItems")!!)
            }
        }

        // Set up the ListView for basket items
        val listViewBasketItems: ListView = findViewById(R.id.listViewBasketItems)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, basketViewModel.basketItems)
        listViewBasketItems.adapter = adapter

        // Set up item click listener to remove the clicked item
        listViewBasketItems.setOnItemClickListener { _, _, position, _ ->
            removeItemFromBasket(position)
        }

        // Set up button click listener for "Proceed to Checkout"
        val buttonCheckout: Button = findViewById(R.id.buttonCheckout)
        buttonCheckout.setOnClickListener {
            if (basketViewModel.basketItems.isNotEmpty()) {
                // Add your logic to proceed with the checkout
                placeOrder()
            } else {
                showToast("Basket is empty. Add items before checking out.")
            }
        }

        // Disable the "Proceed to Checkout" button initially if the basket is empty
        //buttonCheckout.isEnabled = basketViewModel.basketItems.isNotEmpty()
    }

    private fun removeItemFromBasket(position: Int) {
        basketViewModel.basketItems.removeAt(position)
        updateBasketAdapter()
    }

    private fun updateBasketAdapter() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, basketViewModel.basketItems)
        val listViewBasketItems: ListView = findViewById(R.id.listViewBasketItems)
        listViewBasketItems.adapter = adapter

        // Disable the "Proceed to Checkout" button if the basket is empty
        val buttonCheckout: Button = findViewById(R.id.buttonCheckout)
        buttonCheckout.isEnabled = basketViewModel.basketItems.isNotEmpty()

    }



    private fun placeOrder() {
        // Generate order ID
        val orderId = generateOrderId()

        // Get customer ID (user ID)
        val customerId = auth.currentUser?.uid

        // Get current date and time
        val orderDateTime = getCurrentDateTime()

        // Set up order details
        val orderDetails = OrderDetails(orderId, basketViewModel.basketItems)

        // Set up order
        val order = Order(orderId, customerId, orderDateTime, "Pending")

        // Add order to "Orders" node
        val ordersRef = Firebase.database.reference.child("Orders")
        ordersRef.child(orderId).setValue(order)

        // Add order details to "OrderDetails" node
        val orderDetailsRef = Firebase.database.reference.child("OrderDetails")
        orderDetailsRef.child(orderId).setValue(orderDetails)



        // Navigate to the OrdersActivity or any other screen as needed
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        // Helper function to show Toast messages
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun generateOrderId(): String {
        // Generate a unique order ID (you can customize this based on your needs)
        return UUID.randomUUID().toString()
    }

    private fun getCurrentDateTime(): String {
        // Get the current date and time in a specified format
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}