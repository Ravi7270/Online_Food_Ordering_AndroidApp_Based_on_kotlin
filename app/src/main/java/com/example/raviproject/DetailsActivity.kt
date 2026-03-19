package com.example.raviproject

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import com.example.raviproject.Fragment.CartFragment
import com.example.raviproject.databinding.ActivityDetailsBinding
import com.example.raviproject.model.cartitems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class detailsactivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private  var foodName: String? = null
    private var foodPrice: String? = null
    private var foodImage: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // intialize auth
        auth = FirebaseAuth.getInstance()
        foodName =intent.getStringExtra("MenuItemsName")
        foodPrice=intent.getStringExtra("MenuItemsPrice")
        foodImage=intent.getStringExtra("MenuItemsImage")
        with(binding){
            detailFoodName.text=foodName
            detailfoodprice.text=foodPrice
            foodImage?.let {
                val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                DetailFoodimage.setImageBitmap(bitmap)
            }
        }


        // Back button click
        binding.backDecription.setOnClickListener {
            val intent = Intent(this, mainactivity::class.java)
            startActivity(intent)

        }
binding.addItemButton.setOnClickListener {
    addItemToCart()
}

        // Add to cart button click

    }

    private fun addItemToCart() {
        val database=FirebaseDatabase.getInstance().reference
        val userId=auth.currentUser?.uid?:""

        //Create a cart items object
        val catItem = cartitems(foodName.toString(),foodPrice.toString(),foodImage.toString(), foodQuantity = 1)

    //save data to cart item to firebase data base
        database.child("userdata").child(userId).child("CartItem").push().setValue(catItem).addOnSuccessListener {
            Toast.makeText(this, "item added into the cart successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show()
        }
    }


}

