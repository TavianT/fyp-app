package com.example.finalyearproject.ui.dashboard

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentFlowerFinderBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

class FlowerFinderFragment : Fragment() {

    private var _binding: FragmentFlowerFinderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var startForImageResult: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                //TODO: POST data to server - retrofit
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFlowerFinderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.CameraButton.setOnClickListener {
            Log.d(TAG, "Camera button clicked")
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .saveDir(File(root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "Final Year Project"))
                .createIntent { intent ->
                    startForImageResult.launch(intent)
                }
//                .start()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}