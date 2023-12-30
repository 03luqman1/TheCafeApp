package com.example.myapplicationimport
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MenuItem
import com.example.myapplication.R

class MenuAdapter2(
    private var menuItems: List<MenuItem>,
    private var menuKeys: List<String>,
    private val onItemChecked: (String, Boolean) -> Unit
) : RecyclerView.Adapter<MenuAdapter2.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemCheckbox: CheckBox = itemView.findViewById(R.id.itemCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.itemName.text = menuItem.name
        holder.itemCheckbox.isChecked = false // Set the initial state based on your logic

        holder.itemView.setOnClickListener {
            // Toggle the checkbox state
            holder.itemCheckbox.isChecked = !holder.itemCheckbox.isChecked
            // Notify the listener of the change
            onItemChecked.invoke(menuKeys[position], holder.itemCheckbox.isChecked)
        }
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    fun updateData(newMenuItems: List<MenuItem>, newMenuKeys: List<String>) {
        menuItems = newMenuItems
        menuKeys = newMenuKeys
        notifyDataSetChanged()
    }
}
