package com.example.finalyearproject.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.finalyearproject.MainActivity
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentRegisterBinding
import com.example.finalyearproject.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val root: View = binding.root
        binding.registerButton.setOnClickListener {
            val emailAddress: String = binding.editTextRegisterEmailAddress.toString().trim()
            val password: String = binding.editTextRegisterPassword.toString().trim()
            val confirmPassword: String = binding.editTextRegisterConfirmPassword.toString().trim()
            if (!Util.isEmailValid(emailAddress)) {
                Toast.makeText(requireContext(), "Email address is invalid", Toast.LENGTH_LONG).show()
            } else if (!Util.isPasswordValid(password)) {
                Toast.makeText(requireContext(), "Password is invalid, see rules above", Toast.LENGTH_LONG).show()
            } else if(!Util.passwordsMatch(password, confirmPassword)) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(requireActivity()) {task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            root.findNavController().navigate(R.id.action_registerFragment_to_logInFragment)
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "INTERNAL SERVER ERROR: Unable to create account at this time!", Toast.LENGTH_LONG).show()
                        }
                    }
            }


        }
        return root
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null) {
            val intent: Intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }
}