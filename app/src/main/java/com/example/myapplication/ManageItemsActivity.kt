package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class ManageItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_items)
    }

    fun onAddButtonClicked(view: View) {
        val intent = Intent(this, AddItemActivity::class.java)
        startActivity(intent)
    }

    fun onDeleteButtonClicked(view: View) {
        val intent = Intent(this, DeleteItemActivity::class.java)
        startActivity(intent)
    }
}
