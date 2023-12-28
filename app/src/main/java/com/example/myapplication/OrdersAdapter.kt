package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// OrdersAdapter.kt
class OrdersAdapter(private val orderStatusChangeListener: (String, String) -> Unit) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    private var ordersList: List<Order> = emptyList()

    fun setOrders(orders: List<Order>) {
        ordersList = orders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = ordersList[position]
        holder.textViewOrderStatus.text = "Status: ${currentItem.orderStatus}"
        holder.textViewOrderDateTime.text = "Date/Time: ${currentItem.orderDateTime}"

        // Set other details as needed

        // Button click listener to change order status
        holder.buttonChangeStatus.setOnClickListener {
            // You can implement a dialog or some UI to get the new status
            val newStatus = "Delivered" // Change this as needed
            orderStatusChangeListener.invoke(currentItem.orderId, newStatus)
        }
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewOrderStatus: TextView = itemView.findViewById(R.id.textViewOrderStatus)
        val textViewOrderDateTime: TextView = itemView.findViewById(R.id.textViewOrderDateTime)
        val buttonChangeStatus: Button = itemView.findViewById(R.id.buttonChangeStatus)

        // Add other views as needed
    }
}
