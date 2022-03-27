package com.example.finalyearproject.ui

import android.content.res.Configuration
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.finalyearproject.GlideApp
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentInformationBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!
    var filePath : String = ""
    var imageClass: String = ""
    var singularName: String = "Singular name: "
    var pluralName: String = "Plural name: "
    var binomialName: String = "Binomial Name: "
    var information: String = ""
    var placeholder : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filePath = arguments?.getString("filePath").toString()
            imageClass = arguments?.getString("class").toString()
        }

        val db = Firebase.firestore
        val flowerDocumentReference = db.collection("flower-information").document(imageClass)
        flowerDocumentReference.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("doc ref", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("doc ref", "No such document")
                }
                Log.d("docref data", document.data!!["binomial name"] as String)
                singularName += document.data!!["singular name"]
                pluralName += document.data!!["plural name"]
                binomialName += document.data!!["binomial name"]
                information = document.data!!["information"] as String

                binding.InformationTitleTextView.text = document.data!!["plural name"].toString()
                binding.singularNameTextView.text = singularName
                binding.pluralNameTextView.text = pluralName
                binding.binomialNameTextView.text = binomialName
                binding.InformationTextView.text = information
                binding.InformationTextView.movementMethod = ScrollingMovementMethod.getInstance()
            }.addOnFailureListener { exception ->
                Log.e("Error", "get failed with ", exception)
            }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_navigation_information_to_navigation_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        when (requireContext().resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {placeholder = R.drawable.ic_baseline_filter_vintage_100_white}
            Configuration.UI_MODE_NIGHT_NO -> {placeholder = R.drawable.ic_baseline_filter_vintage_100_black}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {placeholder = R.drawable.ic_baseline_filter_vintage_100_black}
        }
        val storageReference = Firebase.storage.reference.child(filePath)
        GlideApp.with(requireContext()).load(storageReference).placeholder(placeholder).into(binding.informationImageView)
        Log.d("debug", binomialName)

        binding.InformationTextView.movementMethod = ScrollingMovementMethod()

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigate(R.id.action_navigation_information_to_navigation_home)
            return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.information_menu, menu)
    }
}