package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AdminPanelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }

    fun onViewOrdersClicked(view: View) {
        val intent = Intent(this, ViewOrdersActivity::class.java)
        startActivity(intent)
    }

    fun onViewRatingsClicked(view: View) {
        val intent = Intent(this, ViewRatingsActivity::class.java)
        startActivity(intent)
    }

    fun onManageItemsClicked(view: View) {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

    fun onSendNotificationsClicked(view: View) {
        val intent = Intent(this, SendNotificationsActivity::class.java)
        startActivity(intent)
    }
    fun onSignOutClicked(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
    fun onAddAdminClicked(view: View) {
        val intent = Intent(this, AddAdminActivity::class.java)
        startActivity(intent)

    }


}
