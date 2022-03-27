package com.example.finalyearproject.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalyearproject.R
import com.example.finalyearproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var titles: MutableList<String> = ArrayList<String>()
    private var dates: MutableList<String> = ArrayList<String>()
    private var ids: MutableList<String> = ArrayList<String>()
    private var images: MutableList<StorageReference> = ArrayList<StorageReference>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventChangeListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeRecyclerAdapter(images, titles,dates,ids,requireContext())
        binding.homeRecyclerView.adapter = adapter
        binding.homeRecyclerView.layoutManager = layoutManager
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun eventChangeListener() {
        val c : Calendar = Calendar.getInstance()
        val db = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val uid = auth.currentUser!!.uid
        Log.d("Users ID", uid)
        db.collection(uid)
            .orderBy("timeStamp",Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Toast.makeText(requireContext(), "Unable to get new results from server", Toast.LENGTH_LONG).show()
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (documentChange : DocumentChange in value?.documentChanges!!) {
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            val document = documentChange.document
                            ids.add(document.id)
                            titles.add(document.getString("class")!!)
                            c.timeInMillis = document.getString("timeStamp")!!.toLong()
                            val date =
                                c[Calendar.YEAR].toString() + "-" + c[Calendar.MONTH] + "-" + c[Calendar.DAY_OF_MONTH]
                            val time =
                                c[Calendar.HOUR_OF_DAY].toString() + ":" + c[Calendar.MINUTE]
                            dates.add("$date $time")
                            val storageReference = Firebase.storage.reference.child(document.getString("filePath")!!)
                            images.add(storageReference)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

            })
    }
}