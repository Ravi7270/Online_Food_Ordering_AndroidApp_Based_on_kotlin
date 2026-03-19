package com.example.raviproject.adepter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.location.LocationRequestCompat.Quality
import androidx.recyclerview.widget.RecyclerView
import com.example.raviproject.databinding.CaetItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class cartadapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private val cartImages: MutableList<String>, // Base64 strings
    private val quantities: MutableList<Int>
) : RecyclerView.Adapter<cartadapter.CartViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private val cartItemReference: DatabaseReference

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        cartItemReference = database.reference.child("userdata").child(userId).child("CartItem")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CaetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size
//get updated quantities
    fun getUpdatedItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
    itemQuantity.addAll(quantities)
        return itemQuantity
    }

    inner class CartViewHolder(private val binding: CaetItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                cartFoodName.text = cartItems[position]
                cartItemPrice.text = cartItemPrices[position]
                cartItemQuantity.text = quantities[position].toString()

                val base64Image = cartImages[position]
                if (base64Image.isNotBlank()) {
                    val bitmap = decodeBase64ToBitmap(base64Image)
                    if (bitmap != null) {
                        cartImage.setImageBitmap(bitmap)
                    } else {
                        cartImage.setImageResource(android.R.drawable.ic_menu_report_image)
                    }
                } else {
                    cartImage.setImageResource(android.R.drawable.ic_menu_report_image)
                }

                removeButton.setOnClickListener {
                    decreaseQuantity(position)
                }

                addButton.setOnClickListener {
                    increaseQuantity(position)
                }

                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION)
                        deleteItems(itemPosition)
                }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (quantities[position] < 10) {
                quantities[position]++
                
                binding.cartItemQuantity.text = quantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (quantities[position] > 1) {
                quantities[position]--

                binding.cartItemQuantity.text = quantities[position].toString()
            }
        }

        private fun deleteItems(position: Int) {
            getUniqueKeyPosition(position) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                cartItems.removeAt(position)
                cartImages.removeAt(position)
                cartItemPrices.removeAt(position)
                quantities.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItems.size)
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
            }
        }

        private fun getUniqueKeyPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
            return try {
                val cleanBase64 = base64Str.substringAfter(",") // Strip metadata if any
                val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}



//package com.example.raviproject.adepter
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.util.Base64
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//import com.example.raviproject.databinding.CaetItemBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//class CartAdapter(
//    private val context: Context,
//    private val CartItems: MutableList<String>,
//    private val CartItemPrice: MutableList<String>,
//    private val CartImages: MutableList<String>, // Base64 strings
//    private val Quantity: MutableList<Int>
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    private val auth = FirebaseAuth.getInstance()
//
//    companion object {
//        private var itemQuantity: IntArray = intArrayOf()
//        private lateinit var cartItemReference: DatabaseReference
//    }
//
//    init {
//        val database = FirebaseDatabase.getInstance()
//        val userId = auth.currentUser?.uid ?: ""
//        val cartItemNumber = CartItems.size
//
//        itemQuantity = IntArray(cartItemNumber) { 1 }
//        cartItemReference = database.reference.child("userdata").child(userId).child("CartItem")
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val binding = CaetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CartViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        holder.bind(position)
//    }
//
//    override fun getItemCount(): Int = CartItems.size
//    //get updated quantitities
//    fun getUpdatedItemsQuantities(): MutableList<Int> {
//        val itemQuantity = mutableListOf<Int>()
//        itemQuantity.addAll(Quantity)
//        return itemQuantity
//
//    }
//
//    inner class CartViewHolder(private val binding: CaetItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(position: Int) {
//            binding.apply {
//                val quantity = itemQuantity[position]
//                cartFoodName.text = CartItems[position]
//                cartItemPrice.text = CartItemPrice[position]
//                cartItemQuantity.text = quantity.toString()
//
//                // ✅ Decode Base64 image and set to ImageView
//                val base64Image = CartImages[position]
//                val bitmap = decodeBase64ToBitmap(base64Image)
//                if (bitmap != null) {
//                    cartImage.setImageBitmap(bitmap)
//                } else {
//                    cartImage.setImageResource(android.R.drawable.ic_menu_report_image)
//                }
//
//                removeButton.setOnClickListener {
//                    decreaseQuantity(position)
//                }
//
//                addButton.setOnClickListener {
//                    increaseQuantity(position)
//                }
//
//                deleteButton.setOnClickListener {
//                    val itemPosition = adapterPosition
//                    if (itemPosition != RecyclerView.NO_POSITION)
//                        deleteItems(itemPosition)
//                }
//            }
//        }
//
//        private fun increaseQuantity(position: Int) {
//            if (itemQuantity[position] < 10) {
//                itemQuantity[position]++
//                binding.cartItemQuantity.text = itemQuantity[position].toString()
//            }
//        }
//
//        private fun decreaseQuantity(position: Int) {
//            if (itemQuantity[position] > 1) {
//                itemQuantity[position]--
//                binding.cartItemQuantity.text = itemQuantity[position].toString()
//            }
//        }
//
//        private fun deleteItems(position: Int) {
//            getUniqueKeyPosition(position) { uniqueKey ->
//                if (uniqueKey != null) {
//                    removeItem(position, uniqueKey)
//                }
//            }
//        }
//
//        private fun removeItem(position: Int, uniqueKey: String) {
//            cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
//                CartItems.removeAt(position)
//                CartImages.removeAt(position)
//                CartItemPrice.removeAt(position)
//                Quantity.removeAt(position)
//                itemQuantity = itemQuantity.filterIndexed { index, _ -> index != position }.toIntArray()
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, CartItems.size)
//                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        private fun getUniqueKeyPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
//            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    var uniqueKey: String? = null
//                    snapshot.children.forEachIndexed { index, dataSnapshot ->
//                        if (index == positionRetrieve) {
//                            uniqueKey = dataSnapshot.key
//                            return@forEachIndexed
//                        }
//                    }
//                    onComplete(uniqueKey)
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
//        }
//
//        // ✅ Helper to decode Base64 image string
//        private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
//            return try {
//                val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
//                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            }
//        }
//    }
//}
//
//
////
////package com.example.raviproject.adepter
////
////import android.content.Context
////import android.graphics.Bitmap
////import android.graphics.BitmapFactory
////import android.util.Base64
////import android.view.LayoutInflater
////import android.view.ViewGroup
////import android.widget.Toast
////import androidx.recyclerview.widget.RecyclerView
////import com.example.raviproject.databinding.CaetItemBinding
////import com.google.firebase.auth.FirebaseAuth
////import com.google.firebase.database.*
////
////class CartAdapter(
////    private val context: Context,
////    private val cartItems: MutableList<String>,
////    private val cartItemPrices: MutableList<String>,
////    private val cartImages: MutableList<String>, // Base64 strings
////    private val quantities: MutableList<Int>
////) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
////
////    private val auth = FirebaseAuth.getInstance()
////    private val cartItemReference: DatabaseReference
////
////    init {
////        val database = FirebaseDatabase.getInstance()
////        val userId = auth.currentUser?.uid ?: ""
////        cartItemReference = database.reference.child("userdata").child(userId).child("CartItem")
////    }
////
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
////        val binding = CaetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
////        return CartViewHolder(binding)
////    }
////
////    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
////        holder.bind(position)
////    }
////
////    override fun getItemCount(): Int = cartItems.size
////
////    fun getUpdatedItemsQuantities(): MutableList<Int> {
////        return quantities.toMutableList()
////    }
////
////    inner class CartViewHolder(private val binding: CaetItemBinding) : RecyclerView.ViewHolder(binding.root) {
////        fun bind(position: Int) {
////            binding.apply {
////                cartFoodName.text = cartItems[position]
////                cartItemPrice.text = cartItemPrices[position]
////                cartItemQuantity.text = quantities[position].toString()
////
////                val bitmap = decodeBase64ToBitmap(cartImages[position])
////                if (bitmap != null) {
////                    cartImage.setImageBitmap(bitmap)
////                } else {
////                    cartImage.setImageResource(android.R.drawable.ic_menu_report_image)
////                }
////
////                removeButton.setOnClickListener {
////                    decreaseQuantity(position)
////                }
////
////                addButton.setOnClickListener {
////                    increaseQuantity(position)
////                }
////
////                deleteButton.setOnClickListener {
////                    val itemPosition = adapterPosition
////                    if (itemPosition != RecyclerView.NO_POSITION)
////                        deleteItems(itemPosition)
////                }
////            }
////        }
////
////        private fun increaseQuantity(position: Int) {
////            if (quantities[position] < 10) {
////                quantities[position]++
////                binding.cartItemQuantity.text = quantities[position].toString()
////            }
////        }
////
////        private fun decreaseQuantity(position: Int) {
////            if (quantities[position] > 1) {
////                quantities[position]--
////                binding.cartItemQuantity.text = quantities[position].toString()
////            }
////        }
////
////        private fun deleteItems(position: Int) {
////            getUniqueKeyPosition(position) { uniqueKey ->
////                if (uniqueKey != null) {
////                    removeItem(position, uniqueKey)
////                }
////            }
////        }
////
////        private fun removeItem(position: Int, uniqueKey: String) {
////            cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
////                cartItems.removeAt(position)
////                cartImages.removeAt(position)
////                cartItemPrices.removeAt(position)
////                quantities.removeAt(position)
////                notifyItemRemoved(position)
////                notifyItemRangeChanged(position, cartItems.size)
////                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
////            }.addOnFailureListener {
////                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
////            }
////        }
////
////        private fun getUniqueKeyPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
////            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
////                override fun onDataChange(snapshot: DataSnapshot) {
////                    var uniqueKey: String? = null
////                    snapshot.children.forEachIndexed { index, dataSnapshot ->
////                        if (index == positionRetrieve) {
////                            uniqueKey = dataSnapshot.key
////                            return@forEachIndexed
////                        }
////                    }
////                    onComplete(uniqueKey)
////                }
////
////                override fun onCancelled(error: DatabaseError) {}
////            })
////        }
////
////        private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
////            return try {
////                val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
////                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
////            } catch (e: Exception) {
////                e.printStackTrace()
////                null
////            }
////        }
////    }
////}
