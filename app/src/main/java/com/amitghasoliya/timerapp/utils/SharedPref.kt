package com.amitghasoliya.timerapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref {
    companion object {
        private const val PREF_NAME = "sharedcheckLogin"
        private const val UserType = "User"
        private lateinit var sharedPreferences: SharedPreferences

        fun initialize(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun setUserType(name: String) {
            val editor = sharedPreferences.edit()
            editor.putString(UserType, name)
            editor.apply()
            editor.commit()
        }

        fun getUserType(): String? {
            return sharedPreferences.getString(UserType, "User")
        }

        fun logoutUser(context:Context) {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }
}