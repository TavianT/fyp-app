package com.example.finalyearproject.ui.notifications

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.finalyearproject.AccountAcitivity
import com.example.finalyearproject.R

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val logOutPreference = Preference(context)
        logOutPreference.key = "logout"
        logOutPreference.title = "Log Out"
        logOutPreference.setOnPreferenceClickListener {
            Firebase.auth.signOut()
            val intent: Intent = Intent(requireContext(), AccountAcitivity::class.java)
            startActivity(intent)
            true

        }

        val helpPreference = Preference(context)
        helpPreference.key = "help"
        helpPreference.title = "Help"
        helpPreference.summary = "Help with using the app"
        helpPreference.setOnPreferenceClickListener {
            findNavController().navigate(R.id.navigation_help)
            true
        }

//        val accountPreference = Preference(context)
//        accountPreference.key = "account"
//        accountPreference.title = "Account Information"
//
//        screen.addPreference(accountPreference)
        screen.addPreference(helpPreference)
        screen.addPreference(logOutPreference)

        preferenceScreen = screen
    }

}