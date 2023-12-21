package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

// ...
class MenuAdapter(context: Context, items: List<MenuItem>) :
    ArrayAdapter<MenuItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.menu_item, parent, false)

        val currentItem = getItem(position)!!

        val imageViewItem: ImageView = itemView.findViewById(R.id.imageViewItem)
        val textViewItemName: TextView = itemView.findViewById(R.id.textViewItemName)
        val textViewItemDescription: TextView = itemView.findViewById(R.id.textViewItemDescription)
        val textViewItemPrice: TextView = itemView.findViewById(R.id.textViewItemPrice)

        // Load image using Picasso
        Picasso.get().load(currentItem.image).into(imageViewItem)

        // Set text values
        textViewItemName.text = currentItem.name
        textViewItemDescription.text = currentItem.description
        textViewItemPrice.text = "$${currentItem.price}"

        return itemView
    }
}
// ...

