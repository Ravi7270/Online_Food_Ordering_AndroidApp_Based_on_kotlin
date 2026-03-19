package com.example.raviproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.raviproject.R
import com.example.raviproject.adepter.menuadapter
import com.example.raviproject.databinding.FragmentSearchBinding

class searchfragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: menuadapter

    // Original Data
    private val originalMenuFoodNames = listOf("Burger", "Sandwich", "Pizza", "Momo", "Fried Rice", "Paneer Tikka", "Matar paneer", "Shahipaneer", "Idli")
    private val originalMenuItemPrices = listOf("Rs: 40", "Rs: 30", "Rs: 120", "Rs: 50", "Rs: 80", "Rs: 180", "Rs: 90", "Rs: 120", "Rs: 50")
    private val originalMenuImages = listOf(
        R.drawable.burger,
        R.drawable.sandwich,
        R.drawable.pizza,
        R.drawable.momo,
        R.drawable.friedrice,
        R.drawable.paneertikka,
        R.drawable.matarpaneer,
        R.drawable.shahipaneer,
        R.drawable.idli
    )

    // Filtered Data
    private val filteredFoodNames = mutableListOf<String>()
    private val filteredPrices = mutableListOf<String>()
    private val filteredImages = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Initialize adapter with filtered lists
//        adapter = MenuAdapter(filteredFoodNames, filteredPrices, filteredImages, requireContext())
//        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.menuRecyclerView.adapter = adapter

        setupSearchView()
        showAllMenu()

        return binding.root
    }

    private fun showAllMenu() {
        filteredFoodNames.clear()
        filteredPrices.clear()
        filteredImages.clear()

        filteredFoodNames.addAll(originalMenuFoodNames)
        filteredPrices.addAll(originalMenuItemPrices)
        filteredImages.addAll(originalMenuImages)

        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filteredFoodNames.clear()
        filteredPrices.clear()
        filteredImages.clear()

        originalMenuFoodNames.forEachIndexed { index, foodName ->
            if (foodName.contains(query, ignoreCase = true)) {
                filteredFoodNames.add(foodName)
                filteredPrices.add(originalMenuItemPrices[index])
                filteredImages.add(originalMenuImages[index])
            }
        }

        adapter.notifyDataSetChanged()
    }
}
