package com.example.myapplication


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
// ViewOrdersActivity.kt
class ViewOrdersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        recyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pass a lambda to handle order status changes
        adapter = OrdersAdapter { orderId, newStatus ->
            updateOrderStatus(orderId, newStatus)
        }

        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().reference.child("Orders")

        loadOrders()
    }

    private fun loadOrders() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val ordersList = mutableListOf<Order>()
                for (orderSnapshot in dataSnapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.let {
                        ordersList.add(it)
                    }
                }
                adapter.setOrders(ordersList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateOrderStatus(orderId: String, newStatus: String) {
        // Update the order status in the Firebase database
        val orderRef = databaseReference.child(orderId)
        orderRef.child("orderStatus").setValue(newStatus)
    }
}
