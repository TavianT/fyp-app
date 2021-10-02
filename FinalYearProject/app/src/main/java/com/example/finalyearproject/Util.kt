package com.example.finalyearproject

import android.text.TextUtils
import java.util.regex.Pattern

class Util {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        fun isPasswordValid(password: String): Boolean {
            val passwordPattern =  "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
            val pattern = Pattern.compile(passwordPattern)
            val matcher = pattern.matcher(password)
            return matcher.matches()
        }
        fun passwordsMatch(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword
        }
    }
}