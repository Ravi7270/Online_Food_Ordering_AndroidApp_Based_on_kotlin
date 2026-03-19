package com.example.raviproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.raviproject.databinding.ActivityPayOutBinding
import com.example.raviproject.model.orderdetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class payoutactivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private var name: String = ""
    private var address: String = ""
    private var phone: String = ""
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemQuantity: ArrayList<Int>
    private lateinit var userId: String
    private var totalAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        // Get data from Intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") ?: arrayListOf()
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") ?: arrayListOf()
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") ?: arrayListOf()
        foodItemQuantity = intent.getIntegerArrayListExtra("FoodItemQuantitiy") ?: arrayListOf()

        // Set total amount
        totalAmount = calculateTotalAmount()
        binding.totalamount.setText("${totalAmount}rs.")

        // Fetch user data from Firebase
        setUserData()

        // Button Clicks
        binding.ProceedBackButton.setOnClickListener {
            finish() // Just close this activity to go back
        }

        binding.PlaceMyOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun setUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userReference = databaseReference.child("userdata").child(currentUser.uid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("name").getValue(String::class.java) ?: ""
                        address = snapshot.child("address").getValue(String::class.java) ?: ""
                        phone = snapshot.child("phone").getValue(String::class.java) ?: ""

                        binding.name.setText(name)
                        binding.address.setText(address)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun calculateTotalAmount(): Int {
        var total = 0
        for (i in foodItemPrice.indices) {
            val rawPrice = foodItemPrice[i].filter { it.isDigit() } // Remove any symbol like '$' or 'rs'
            val price = rawPrice.toIntOrNull() ?: 0
            val quantity = foodItemQuantity.getOrNull(i) ?: 1
            total += price * quantity
        }
        return total
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: return

        val itemPushKey = databaseReference.child("OrderDetails").push().key ?: return

        val orderDetails = orderdetails(
            userId,
            name,
            foodItemName,
            foodItemPrice,
            foodItemImage,
            foodItemQuantity,
            address,
            phone,
            itemPushKey,
            false,
            false
        )

        databaseReference.child("OrderDetails").child(itemPushKey).setValue(orderDetails)
            .addOnSuccessListener {
                val bottomSheetDialog = congratsbottomsheet()
                bottomSheetDialog.show(supportFragmentManager, "OrderPlaced")
            }
    }
}
