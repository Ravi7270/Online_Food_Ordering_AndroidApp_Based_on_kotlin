package com.example.raviproject.Fragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raviproject.payoutactivity
import com.example.raviproject.adepter.cartadapter
import com.example.raviproject.databinding.FragmentCartBinding
import com.example.raviproject.model.cartitems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class cartfragment : Fragment() {

    private lateinit var userId: String
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var cartAdapter: cartadapter

    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodImageBase64: MutableList<String>
    private lateinit var quantities: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.processButton.setOnClickListener {
            getOrderItemsDetail()
        }
        retrieveCartItems()
        return binding.root


    }
    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodImage: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), payoutactivity::class.java)
            intent.putExtra("FoodItemName", ArrayList(foodName))
            intent.putExtra("FoodItemPrice", ArrayList(foodPrice))
            intent.putExtra("FoodItemImage", ArrayList(foodImage))
            intent.putExtra("FoodItemQuantitiy", ArrayList(foodQuantities))
            startActivity(intent)
        }
    }
    private fun retrieveCartItems() {

        val cartRef: DatabaseReference = database.reference.child("userdata").child(userId).child("CartItem")

        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodImageBase64 = mutableListOf()
        quantities = mutableListOf()

        cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val cartItem = itemSnapshot.getValue(cartitems::class.java)
                    cartItem?.foodName?.let { foodNames.add(it) }
                    cartItem?.foodPrice?.let { foodPrices.add(it) }
                    cartItem?.foodImage?.let { foodImageBase64.add(it) }
                    cartItem?.foodQuantity?.let { quantities.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setAdapter() {
        cartAdapter = cartadapter(requireContext(), foodNames, foodPrices, foodImageBase64, quantities)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = cartAdapter
    }

    private fun getOrderItemsDetail() {
        val orderIdReference = database.reference.child("userdata").child(userId).child("CartItem")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodQuantities = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val orderItems = foodSnapshot.getValue(cartitems::class.java)
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                }
                orderNow(foodName, foodPrice, foodImage, foodQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Order failed, please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }


}



//package com.example.raviproject.Fragment
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.raviproject.PayOutActivity
//import com.example.raviproject.adepter.CartAdapter
//import com.example.raviproject.databinding.FragmentCartBinding
//import com.example.raviproject.model.CartItems
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//class CartFragment : Fragment() {
//
//    private lateinit var userId: String
//    private lateinit var binding: FragmentCartBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var cartAdapter: CartAdapter
//    private lateinit var foodNames: MutableList<String>
//    private lateinit var foodPrices: MutableList<String>
//    private lateinit var foodImageBase64: MutableList<String>
//    private lateinit var quantities: MutableList<Int>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        auth = FirebaseAuth.getInstance()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentCartBinding.inflate(inflater, container, false)
//
//        retrieveCartItems()
//
//        binding.ProceedButton.setOnClickListener {
//            //get items details
//            getOrderItemsDetail()
//
//        }
//
//        return binding.root
//    }
//
//    private fun getOrderItemsDetail() {
//        val orderIdReferance :DatabaseReference=database.reference.child("userdata").child(userId).child("CartItem")
//
//        val foodName= mutableListOf<String>()
//        val foodPrice= mutableListOf<String>()
//        val foodImage= mutableListOf<String>()
//        //get items Quantities
//        val foodQuantities= cartAdapter.getUpdatedItemsQuantities()
//
//        orderIdReferance.addListenerForSingleValueEvent(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (foodSnapshot in snapshot.children){
//                    val orderItems =foodSnapshot.getValue(CartItems::class.java)
//                    orderItems?.foodName?.let { foodName.add(it) }
//                    orderItems?.foodPrice?.let { foodPrice.add(it) }
//                    orderItems?.foodImage?.let { foodImage.add(it) }
//                }
//                orderNow(foodName,foodPrice,foodImage,foodQuantities)
//            }
//
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(), "Oredr Making failed please failed", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
//
//    private fun orderNow(
//        foodName: MutableList<String>,
//        foodPrice: MutableList<String>,
//        foodImage: MutableList<String>,
//        foodQuantities: MutableList<Int>
//    ) {
//        if (isAdded && context!=null){
//            val intent = Intent(requireContext(),PayOutActivity::class.java)
//            intent.putExtra("FoodItemName",foodName as ArrayList<String>)
//            intent.putExtra("FoodItemPrice",foodPrice as ArrayList<String>)
//            intent.putExtra("FoodItemImage",foodImage as ArrayList<String>)
//            intent.putExtra("FoodItemQuantitiy",foodQuantities as ArrayList<Int>)
//            startActivity(intent)
//        }
//
//    }
//
//    private fun retrieveCartItems() {
//        database = FirebaseDatabase.getInstance()
//        val userId = auth.currentUser?.uid ?: return
//        val cartRef: DatabaseReference = database.reference.child("userdata").child(userId).child("CartItem")
//
//        foodNames = mutableListOf()
//        foodPrices = mutableListOf()
//        foodImageBase64 = mutableListOf()
//        quantities = mutableListOf()
//
//        cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (itemSnapshot in snapshot.children) {
//                    val cartItem = itemSnapshot.getValue(CartItems::class.java)
//                    if (cartItem != null) {
//                        cartItem.foodName?.let { foodNames.add(it) }
//                        cartItem.foodPrice?.let { foodPrices.add(it) }
//                        cartItem.foodImage?.let { foodImageBase64.add(it) } // Base64 string
//                        cartItem.foodQuantity?.let { quantities.add(it) }
//                    }
//                }
//                setAdapter()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun setAdapter() {
//        cartAdapter = CartAdapter(requireContext(), foodNames, foodPrices, foodImageBase64, quantities)
//        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.cartRecyclerView.adapter = cartAdapter
//     }
//}






//
//package com.example.raviproject.Fragment
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.raviproject.PayOutActivity
//import com.example.raviproject.adepter.CartAdapter
//import com.example.raviproject.databinding.FragmentCartBinding
//import com.example.raviproject.model.CartItems
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//class CartFragment : Fragment() {
//
//    private lateinit var userId: String
//    private lateinit var binding: FragmentCartBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var cartAdapter: CartAdapter
//    private lateinit var foodNames: MutableList<String>
//    private lateinit var foodPrices: MutableList<String>
//    private lateinit var foodImageBase64: MutableList<String>
//    private lateinit var quantities: MutableList<Int>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance()
//        userId = auth.currentUser?.uid ?: ""
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentCartBinding.inflate(inflater, container, false)
//
//        // Initialize empty lists before use
//        foodNames = mutableListOf()
//        foodPrices = mutableListOf()
//        foodImageBase64 = mutableListOf()
//        quantities = mutableListOf()
//
//        setupRecyclerView()
//        retrieveCartItems()
//
//        binding.ProceedButton.setOnClickListener {
//            getOrderItemsDetail()
//        }
//
//        return binding.root
//    }
//
//    private fun setupRecyclerView() {
//        cartAdapter = CartAdapter(requireContext(), foodNames, foodPrices, foodImageBase64, quantities)
//        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.cartRecyclerView.adapter = cartAdapter
//    }
//
//    private fun retrieveCartItems() {
//        if (userId.isEmpty()) return
//
//        val cartRef = database.reference.child("userdata").child(userId).child("CartItem")
//
//        cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                foodNames.clear()
//                foodPrices.clear()
//                foodImageBase64.clear()
//                quantities.clear()
//
//                for (itemSnapshot in snapshot.children) {
//                    val cartItem = itemSnapshot.getValue(CartItems::class.java)
//                    if (cartItem != null) {
//                        cartItem.foodName?.let { foodNames.add(it) }
//                        cartItem.foodPrice?.let { foodPrices.add(it) }
//                        cartItem.foodImage?.let { foodImageBase64.add(it) }
//                        cartItem.foodQuantity?.let { quantities.add(it) }
//                    }
//                }
//                cartAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Failed to fetch cart data", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun getOrderItemsDetail() {
//        val orderRef = database.reference.child("userdata").child(userId).child("CartItem")
//
//        val foodName = mutableListOf<String>()
//        val foodPrice = mutableListOf<String>()
//        val foodImage = mutableListOf<String>()
//        val foodQuantities = cartAdapter.getUpdatedItemsQuantities()
//
//        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (foodSnapshot in snapshot.children) {
//                    val orderItem = foodSnapshot.getValue(CartItems::class.java)
//                    orderItem?.foodName?.let { foodName.add(it) }
//                    orderItem?.foodPrice?.let { foodPrice.add(it) }
//                    orderItem?.foodImage?.let { foodImage.add(it) }
//                }
//                orderNow(foodName, foodPrice, foodImage, foodQuantities)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(), "Order creation failed", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun orderNow(
//        foodName: MutableList<String>,
//        foodPrice: MutableList<String>,
//        foodImage: MutableList<String>,
//        foodQuantities: MutableList<Int>
//    ) {
//        if (isAdded && context != null) {
//            val intent = Intent(requireContext(), PayOutActivity::class.java).apply {
//                putStringArrayListExtra("FoodItemName", ArrayList(foodName))
//                putStringArrayListExtra("FoodItemPrice", ArrayList(foodPrice))
//                putStringArrayListExtra("FoodItemImage", ArrayList(foodImage))
//                putIntegerArrayListExtra("FoodItemQuantitiy", ArrayList(foodQuantities))
//            }
//            startActivity(intent)
//        }
//    }
//}
