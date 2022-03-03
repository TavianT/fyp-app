package com.example.finalyearproject.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentFlowerFinderBinding
import com.example.finalyearproject.databinding.FragmentResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var filePath : String = ""
    private var imageClass : String = ""
    private var bitmap : Bitmap? = null
    private lateinit var auth: FirebaseAuth
    private var defaultButtonTextColor : Int = 0
    private var defaultButtonBackgroundColor : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        filePath = arguments?.getString("filePath").toString()
        imageClass = arguments?.getString("class").toString()
        bitmap = BitmapFactory.decodeFile(filePath)
        auth = Firebase.auth

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_navigation_result_to_navigation_flower_finder)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.ResultImageView.setImageBitmap(bitmap)
        binding.ResultTextView.setText("This is an image of $imageClass")
        binding.SaveButton.setOnClickListener {
            disableButton(binding.SaveButton)
            binding.SavingInfoTextView.visibility = View.VISIBLE
            val uid = auth.currentUser?.uid
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val ts = System.currentTimeMillis().toString()
            val fileName = "$uid-$ts"
            val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
            var uploadTask = storageReference.putBytes(data)
            uploadTask.addOnFailureListener{ e ->
                //TODO: OUTPUT FAILURE TO THE USER
                Log.e("Image upload error", "Unable to upload image to cloud firestore", e)
            }.addOnSuccessListener { taskSnapshot ->
                val db = Firebase.firestore
                val imageData = hashMapOf(
                    "filePath" to "images/$fileName",
                    "timeStamp" to ts,
                    "class" to imageClass
                )
                db.document("$uid/$fileName").set(imageData)
                    .addOnSuccessListener {
                        Log.d("Success", "DocumentSnapshot successfully written!")
                        binding.SavingInfoTextView.setText("Image saved successfully")
                    }
                    .addOnFailureListener { e -> Log.e("Failure", "Error writing document", e) }
            }
        }
        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.result_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigate(R.id.action_navigation_result_to_navigation_flower_finder)
            return true
        }
        return false
    }

    fun disableButton(button: Button) {
        button.isEnabled = false
        button.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.GRAY,
            BlendModeCompat.SRC_ATOP)
    }
    fun enableButton(button: Button) {
        //button.isEnabled = true

    }
}