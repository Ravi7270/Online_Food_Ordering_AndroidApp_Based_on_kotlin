package com.example.raviproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.internal.toJetpackGetException
import com.example.raviproject.R
import com.example.raviproject.databinding.FragmentCongratsBottomSheetBinding
import com.example.raviproject.databinding.FragmentProfileBinding
import com.example.raviproject.model.usermodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profilefragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private  val databse = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        setUserdata()
        binding.seveButton.setOnClickListener {
            val name :String = binding.username.text.toString()
            val address :String = binding.address.text.toString()
            val email :String = binding.email.text.toString()
            val phone :String = binding.phone.text.toString()

        if (validateInputs(name,address,email,phone))
            updateUserData(name,address,email,phone)
        }
        return binding.root
    }

    private fun validateInputs(
        name: String,
        address: String,
        email: String,
        phone: String
    ): Boolean {
        if (name.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }
//        if (!isValidPhoneNumber(phone, "IN")) { // Change "IN" to required country code
//            Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show()
//            return false
//        }
        return true
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId :String? = auth.currentUser?.uid
        if (userId!=null){
            val userReference=databse.getReference("userdata").child(userId)

            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )

            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile update successfully", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Profile Update failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserdata() {
        val  userId:String?= auth.currentUser?.uid
        if(userId!=null){
            val userReferance = databse.getReference("userdata").child(userId)
            userReferance.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val UserProfile =snapshot.getValue(usermodel::class.java)
                        if(UserProfile!=null){
                            binding.username.setText(UserProfile.name)
                            binding.address.setText(UserProfile.address)
                            binding.email.setText(UserProfile.email)
                            binding.phone.setText(UserProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    companion object {
    }
}