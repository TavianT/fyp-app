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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.finalyearproject.RetrofitInterface
import com.example.finalyearproject.ImageClassification
import com.example.finalyearproject.R
import com.example.finalyearproject.ServiceBuilder
import com.example.finalyearproject.databinding.FragmentFlowerFinderBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                var fileName: String = ""
                fileUri.let { returnUri ->
                    requireContext().contentResolver.query(returnUri, null, null, null, null)
                }
                fileName = fileUri.lastPathSegment.toString()
//                    ?.use { cursor ->
//                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                    cursor.moveToFirst()
//                    fileName = cursor.getString(nameIndex)
//                }
                Log.d(TAG, "File name from cursor: $fileName")
                Log.d(TAG, "File uri: $fileUri")
                //TODO: POST data to server - retrofit
                val inputStream = requireContext().contentResolver.openInputStream(fileUri)
                val imageBytes = getBytes(inputStream!!)
                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes)
                val body =  MultipartBody.Part.createFormData("file", fileName, requestFile)
                val request = ServiceBuilder.buildService(RetrofitInterface::class.java)
                val call = request.uploadImage(body)
                binding.progressBar.visibility = View.VISIBLE
                binding.CameraButton.visibility = View.INVISIBLE
                binding.GalleryButton.visibility = View.INVISIBLE
                call.enqueue(object : Callback<ImageClassification>{
                    override fun onResponse(call: Call<ImageClassification>, response: Response<ImageClassification>) {
                        if (response.isSuccessful) {
                            //Log.d(TAG, "response:" + response.)
                            Log.d(TAG, "flower: " + response.body()!!.flower)
                            val file = File(fileUri.path!!)
                            val split = file.path.split(":")
                            val filePath = split[0]
                            val bundle : Bundle = bundleOf(
                                "filePath" to filePath,
                                "class" to response.body()!!.flower
                            )
                            findNavController().navigate(R.id.action_navigation_flower_finder_to_resultFragment, bundle)
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.CameraButton.visibility = View.VISIBLE
                            binding.GalleryButton.visibility = View.VISIBLE
                        } else {
                            Log.e(TAG, "Error on response")
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.CameraButton.visibility = View.VISIBLE
                            binding.GalleryButton.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<ImageClassification>, t: Throwable) {
                        Log.e(TAG, "Could not get file details", t)
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.CameraButton.visibility = View.VISIBLE
                        binding.GalleryButton.visibility = View.VISIBLE
                    }
                })
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
        Log.d("DIRECTORY", root.context.filesDir.toString())
        binding.CameraButton.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/jpg",
                        "image/jpeg",
                        "image/png"
                    )
                )
                .saveDir(File(root.context.filesDir, "images"))
                .createIntent { intent ->
                    startForImageResult.launch(intent)
                }
//                .start()
        }

        binding.GalleryButton.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/jpg",
                        "image/jpeg",
                        "image/png"
                    )
                )
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

    @Throws(IOException::class)
    fun getBytes(`is`: InputStream): ByteArray? {
        val byteBuff = ByteArrayOutputStream()
        val buffSize = 1024
        val buff = ByteArray(buffSize)
        var len = 0
        while (`is`.read(buff).also { len = it } != -1) {
            byteBuff.write(buff, 0, len)
        }
        return byteBuff.toByteArray()
    }
}