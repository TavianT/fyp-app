package com.example.finalyearproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentFlowerFinderBinding
import com.example.finalyearproject.databinding.FragmentLogInBinding
import com.google.android.gms.common.SignInButton


class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)

        val root: View = binding.root
        binding.googleSignInButton.setSize(SignInButton.SIZE_STANDARD)
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}