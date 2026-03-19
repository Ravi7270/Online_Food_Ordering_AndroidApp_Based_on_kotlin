package com.example.raviproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raviproject.menubootomsheetfragment
import com.example.raviproject.adepter.menuadapter
//import com.example.raviproject.adepter.menuadapter
import com.example.raviproject.databinding.FragmentHomeBinding
//import com.example.raviproject.model.MenuItem
import com.example.raviproject.model.menuitem
import com.google.firebase.database.*

class homefragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<menuitem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = menubootomsheetfragment()
            bottomSheetDialog.show(parentFragmentManager, "MenuBottomSheet")
        }

        retrieveAndDisplayPopularItems()

        return binding.root
    }

    private fun retrieveAndDisplayPopularItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("food_items")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(menuitem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                displayRandomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    private fun displayRandomPopularItems() {
        val shuffledItems = menuItems.shuffled().take(6)
        setPopularItemsAdapter(shuffledItems)
    }

    private fun setPopularItemsAdapter(items: List<menuitem>) {
        val adapter = menuadapter(items, requireContext())
        binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopulerRecyclerView.adapter = adapter
    }
}
