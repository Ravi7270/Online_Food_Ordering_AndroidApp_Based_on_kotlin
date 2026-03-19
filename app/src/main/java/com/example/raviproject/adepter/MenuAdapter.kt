package com.example.raviproject.adepter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raviproject.detailsactivity
import com.example.raviproject.databinding.MenuItemBinding
import com.example.raviproject.model.menuitem
import java.io.ByteArrayInputStream
import android.util.Base64


class menuadapter(
    private val menuItems: List<menuitem>,
    private val requireContext: Context
): RecyclerView.Adapter<menuadapter.MenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding=MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int=menuItems.size
    inner class MenuViewHolder (private val binding: MenuItemBinding):
        RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{

                val position = adapterPosition
                if (position!=RecyclerView.NO_POSITION){


                    openDetailSActivity(position)
                }

            }
        }

        private fun openDetailSActivity(position: Int) {
            val menuItem=menuItems[position]

//            Log.e("TAG", "openDetailSActivity: "+ menuItem )
            // intent to open detail activity
            val intent = Intent(requireContext, detailsactivity::class.java).apply {
                putExtra("MenuItemsName",menuItem.foodName)
                putExtra("MenuItemsPrice",menuItem.foodPrice)
                    putExtra("MenuItemsImage",menuItem.foodImage)
            }
            //start the detail activity
            requireContext.startActivity(intent)
        }
//set data into recyclerview


        fun bind(position: Int) {
            val menuItem=menuItems[position]
            binding.apply {
                menuFoodName.text=menuItem.foodName
                    menuPrice.text=menuItem.foodPrice
                val bitmap = decodeBase64ToBitmap(menuItem.foodImage.toString())
                if (bitmap != null) {
                    Glide.with(requireContext).load(bitmap).into(menuImage)
                } else {
                    Log.e("ImageError", "Bitmap is null for item at position $position")
                }
            }

            }
        private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
            return try {
                val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
                BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        }

    }
//    interface OnClickListener{
//        fun onItemClick(position: Int)
//    }




