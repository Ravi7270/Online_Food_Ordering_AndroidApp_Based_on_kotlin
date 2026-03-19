package com.example.raviproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raviproject.adepter.menuadapter
import com.example.raviproject.databinding.FragmentMenuBootomSheetBinding
import com.example.raviproject.model.menuitem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class menubootomsheetfragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBootomSheetBinding
    private lateinit var database: FirebaseDatabase
    private  lateinit var menuItems:MutableList<menuitem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBootomSheetBinding.inflate(inflater,container,false)
        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        retriveMenuItems()

        return  binding.root
    }

    private fun retriveMenuItems() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("food_items")
        menuItems= mutableListOf()

        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                for(foodSnapshot in snapshot.children){
                    Log.e("Suraj==>>", "onDataChange: "+ foodSnapshot.value )


                    val menuItem =foodSnapshot.getValue(menuitem::class.java)
                    menuItem?.let { (menuItems as MutableList<menuitem>).add(it) }
                }
                //once data recive ,set to adepter
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setAdapter() {

        val adapter = menuadapter(menuItems,requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }

    companion object {

    }
}