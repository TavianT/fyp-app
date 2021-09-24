package com.example.finalyearproject.ui.dashboard

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentFlowerFinderBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

class FlowerFinderFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentFlowerFinderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentFlowerFinderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.CameraButton.setOnClickListener {
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
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}