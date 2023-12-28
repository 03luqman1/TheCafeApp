package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ManageItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_items)
    }

    fun onAddButtonClicked() {
        // Add your logic for the "Add Items" button here
        startActivity(Intent(this, AddItemActivity::class.java))
    }

    fun onDeleteButtonClicked() {
        // Add your logic for the "Delete Items" button here
        startActivity(Intent(this, DeleteItemActivity::class.java))
    }
}
