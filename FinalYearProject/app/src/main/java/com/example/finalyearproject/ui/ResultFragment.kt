package com.example.finalyearproject.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentFlowerFinderBinding
import com.example.finalyearproject.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var filePath : String = ""
    private var imageClass : String = ""
    private var bitmap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePath = arguments?.getString("filePath").toString()
        imageClass = arguments?.getString("class").toString()
        bitmap = BitmapFactory.decodeFile(filePath)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.ResultImageView.setImageBitmap(bitmap)
        binding.ResultTextView.setText("This is an image of $imageClass")
        return root
    }
}