package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddItemActivity : AppCompatActivity() {

    private lateinit var itemNameEditText: EditText
    private lateinit var itemDescriptionEditText: EditText
    private lateinit var itemPriceEditText: EditText
    private lateinit var itemImageEditText: EditText
    private lateinit var switchAvailability: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        itemNameEditText = findViewById(R.id.editTextName)
        itemDescriptionEditText = findViewById(R.id.editTextDescription)
        itemPriceEditText = findViewById(R.id.editTextPrice)
        itemImageEditText = findViewById(R.id.editTextImage)
        // Make sure this matches the id in your XML
        switchAvailability = findViewById(R.id.switchAvailability)

        val btnAddItem: Button = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            addItemToMenu()
        }
    }

    private fun addItemToMenu() {
        val itemName = itemNameEditText.text.toString()
        val itemDescription = itemDescriptionEditText.text.toString()
        val itemPriceStr = itemPriceEditText.text.toString()
        val itemImage = itemImageEditText.text.toString()
        val isAvailable = switchAvailability.isChecked

        if (itemName.isEmpty() || itemDescription.isEmpty() || itemPriceStr.isEmpty() || itemImage.isEmpty()) {
            showToast("Please fill in all fields")
            return
        }

        // Validate that the price is a valid double
        val itemPrice: Double = try {
            itemPriceStr.toDouble()
        } catch (e: NumberFormatException) {
            showToast("Invalid price format")
            return
        }

        // Create a new item object
        val newItem = MenuItem(
            available = isAvailable,
            name = itemName,
            description = itemDescription,
            price = itemPrice,
            image = itemImage
        )

        // Use Firebase SDK to add the item to the "Menu" node
        val menuRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Menu")
        val itemId = menuRef.push().key
        menuRef.child(itemId!!).setValue(newItem)

        showToast("Successfully added to menu")
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
