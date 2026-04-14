package com.example.smsspoof.util

import android.content.Context
import android.content.SharedPreferences
import com.example.smsspoof.util.Constants

object PrefsUtils {
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Constants.PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        getPrefs(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getPrefs(context).getBoolean(key, defaultValue)
    }

    fun putString(context: Context, key: String, value: String) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String): String {
        return getPrefs(context).getString(key, defaultValue) ?: defaultValue
    }

    fun remove(context: Context, key: String) {
        getPrefs(context).edit().remove(key).apply()
    }

    fun clearAll(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
