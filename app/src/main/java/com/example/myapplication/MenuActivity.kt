package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var menuRef: DatabaseReference

    // Keep track of selected items
    private val selectedItems: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        menuRef = database.getReference("Menu")

        // Get references to the views
        val listViewMenu: ListView = findViewById(R.id.listViewMenu)
        val buttonGoToBasket: Button = findViewById(R.id.buttonGoToBasket)

        // Set up the ListView
        val menuItems: MutableList<MenuItem> = mutableListOf()
        val adapter = MenuAdapter(this, menuItems)
        listViewMenu.adapter = adapter

        // Set up value event listener to retrieve menu items from the database
        menuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing menu items
                menuItems.clear()

                // Iterate through the menu items in the database and add available items to the list
                for (itemSnapshot in snapshot.children) {
                    val menuItem = itemSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        if (it.available) {
                            menuItems.add(it)
                        }
                    }
                }

                // Update the ListView
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to retrieve menu items.")
            }
        })

        // Set up item click listener
        listViewMenu.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = menuItems[position]
            showToast("Added to basket: ${selectedItem.name}")

            // Add the selected item name and price to the list
            val itemInfo = "${selectedItem.name} - $${selectedItem.price}"
            selectedItems.add(itemInfo)
        }


        // Set up button click listener for "Go to Basket" button
        buttonGoToBasket.setOnClickListener {
            // Pass the selected items to the BasketActivity
            val intent = Intent(this, BasketActivity::class.java)
            intent.putStringArrayListExtra("selectedItems", ArrayList(selectedItems))
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        // Helper function to show Toast messages
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
