package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrdersActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var listViewActiveOrders: ListView
    private lateinit var ordersRef: DatabaseReference
    private lateinit var orderDetailsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        auth = FirebaseAuth.getInstance()
        listViewActiveOrders = findViewById(R.id.listViewActiveOrders)
        ordersRef = FirebaseDatabase.getInstance().reference.child("Orders")
        orderDetailsRef = FirebaseDatabase.getInstance().reference.child("OrderDetails")

        fetchAndDisplayOrders()
    }

    private fun fetchAndDisplayOrders() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            val query = ordersRef.orderByChild("customerId").equalTo(userId)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ordersList: MutableList<String> = mutableListOf()
                    for (orderSnapshot in snapshot.children) {
                        val orderId = orderSnapshot.key
                        val orderStatus = orderSnapshot.child("orderStatus").getValue(String::class.java)
                        if (orderId != null && orderStatus != null) {
                            // Fetch and display order details
                            fetchAndDisplayOrderDetails(orderId, orderStatus, ordersList)
                        }
                    }
                    val adapter = ArrayAdapter(this@OrdersActivity, android.R.layout.simple_list_item_1, ordersList)
                    listViewActiveOrders.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun fetchAndDisplayOrderDetails(orderId: String, orderStatus: String, ordersList: MutableList<String>) {
        val query = orderDetailsRef.orderByChild("orderId").equalTo(orderId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productsList: MutableList<String> = mutableListOf()
                var totalCost = 0.0

                for (orderDetailSnapshot in snapshot.children) {
                    val products = orderDetailSnapshot.child("products").getValue(object : GenericTypeIndicator<List<String>>() {})
                    if (products != null) {
                        for (productInfo in products) {
                            val itemName = productInfo.split(" - ")[0]
                            productsList.add(itemName)
                        }
                    }
                }
                totalCost = calculateTotalCost(snapshot)
                val orderDetails = if (productsList.isNotEmpty()) {
                    "${productsList.joinToString(", ")} - Total Cost: £${String.format("%.2f", totalCost)} - $orderStatus"
                } else {
                    "No items - $orderStatus"
                }

                ordersList.add(orderDetails)
                (listViewActiveOrders.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun calculateTotalCost(snapshot: DataSnapshot): Double {
        var totalCost = 0.0
        for (orderDetailSnapshot in snapshot.children) {
            val products = orderDetailSnapshot.child("products").getValue(object : GenericTypeIndicator<List<String>>() {})
            if (products != null) {
                for (productInfo in products) {
                    val priceString = productInfo.split(" - ")[1]
                    val cleanPriceString = priceString.replace("[^\\d.]".toRegex(), "")
                    val price = cleanPriceString.toDouble()
                    totalCost += price
                }
            }
        }

        return totalCost
    }



}
