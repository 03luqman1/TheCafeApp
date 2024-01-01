package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationimport.MenuAdapter2
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*

class DeleteItemActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deleteButton: MaterialButton
    private lateinit var adapter: MenuAdapter2
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Menu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_item)

        recyclerView = findViewById(R.id.recyclerView)
        deleteButton = findViewById(R.id.deleteButton)

        // Set up RecyclerView
        adapter = MenuAdapter2(emptyList(), emptyList()) { menuItemKey, isChecked ->
            // Handle item checked/unchecked
            if (isChecked) {
                // Delete the item from Firebase
                databaseReference.child(menuItemKey).removeValue()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load menu items from Firebase
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuItems = mutableListOf<MenuItem>()
                for (childSnapshot in snapshot.children) {
                    val menuItem = childSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                val menuKeys = snapshot.children.map { it.key ?: "" }
                adapter.updateData(menuItems, menuKeys)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })

        // Handle deletion button click
        deleteButton.setOnClickListener {
            // The deletion logic is now handled within the adapter callback
            // You may choose to perform additional actions here if needed

            // Show a toast message
            Toast.makeText(this@DeleteItemActivity, "Successfully selected items", Toast.LENGTH_SHORT).show()

            // Navigate back to the previous page
            onBackPressed()
        }
    }
}
