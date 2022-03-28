package com.example.finalyearproject

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.reset_password_dialog_layout, null)
            builder.setMessage("Reset Password")
                .setView(view)
                .setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        val editText: EditText = view.findViewById(R.id.resetPasswordEmailEditText)
                        val email: String = editText.text.toString().trim()
                        Firebase.auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(ContentValues.TAG, "Email sent.")
                                    Toast.makeText(view.context,"Password reset email sent!",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}