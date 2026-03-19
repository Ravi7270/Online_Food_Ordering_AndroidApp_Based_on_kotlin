package com.example.raviproject.adepter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raviproject.detailsactivity
import com.example.raviproject.databinding.PopulerItemBinding

class populeradapter(
    private val items: List<String>,
    private val price: List<String>,
    private val image: List<Int>,
    private val context: Context
) : RecyclerView.Adapter<populeradapter.PopulerViewHolder>() {

    // ✅ Correct ViewHolder creation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulerViewHolder {
        val binding = PopulerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopulerViewHolder(binding)
    }

    // ✅ Correct binding
    override fun onBindViewHolder(holder: PopulerViewHolder, position: Int) {
        val itemName = items[position]
        val itemPrice = price[position]
        val itemImage = image[position]

        holder.bind(itemName, itemPrice, itemImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, detailsactivity::class.java)
            intent.putExtra("MenuItemName", itemName)
            intent.putExtra("MenuItemsPrice", itemPrice)
            intent.putExtra("MenuItemImage", itemImage)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    // ✅ Proper ViewHolder class
    class PopulerViewHolder(private val binding: PopulerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, price: String, image: Int) {
            binding.foodNamePopuler.text = item
            binding.PricePopuler.text = price
            binding.imageView6.setImageResource(image)
        }
    }
}