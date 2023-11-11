package com.example.mystore.sharedprefs

import android.content.Context
import com.example.mystore.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private val prefs = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )

    companion object {
        const val USER_TOKEN = "user_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    fun saveAuthToken(token: String) = editPrefs(USER_TOKEN, token)
    fun fetchAuthToken(): String? = prefs.getString(USER_TOKEN, null)

    fun saveRefreshToken(token: String) = editPrefs(REFRESH_TOKEN, token)
    fun fetchRefreshToken(): String? = prefs.getString(REFRESH_TOKEN, null)

    fun clearTokens() {
        editPrefs(USER_TOKEN, "")
        editPrefs(REFRESH_TOKEN, "")
    }

    private fun editPrefs(key: String, value: String) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
}
