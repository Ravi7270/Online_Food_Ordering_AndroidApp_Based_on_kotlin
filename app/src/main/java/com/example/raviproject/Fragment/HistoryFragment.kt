package com.example.raviproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raviproject.R
import com.example.raviproject.adepter.buyagainaadapter
import com.example.raviproject.databinding.FragmentHistoryBinding



class historyfragment : Fragment() {

    private lateinit var binding:FragmentHistoryBinding
private lateinit var buyAgainAdapter: buyagainaadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding= FragmentHistoryBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        setupRecyclerView()
        return binding.root
    }
private fun setupRecyclerView(){
    val AgainFoodName = arrayOf("Sandwich","Pizza","Momo")
    val AgainFoodPrice =  arrayOf("Rs: 30","Rs: 120","Rs: 50")
    val AgainFoodImage = arrayOf(
        R.drawable.sandwich,
        R.drawable.pizza,
        R.drawable.momo)
    buyAgainAdapter = buyagainaadapter(AgainFoodName,AgainFoodPrice,AgainFoodImage)
    binding.BuyAgainRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    binding.BuyAgainRecyclerView.adapter = buyAgainAdapter
}
    companion object {

    }
}