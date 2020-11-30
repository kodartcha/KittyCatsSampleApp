package com.gabriel_codarcea.core.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.gabriel_codarcea.core.data.R

open class SharedPreferencesManager(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        context.resources.getString(R.string.shared_preferences),
        Context.MODE_PRIVATE
    )

    fun setLoggedIn(value: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(context.resources.getString(R.string.sp_user_logged_in), value)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = sharedPref.getBoolean(
        context.resources.getString(R.string.sp_user_logged_in),
        false
    )

    fun didDownloadData(): Boolean = sharedPref.getBoolean(
        context.resources.getString(R.string.sp_did_download_data),
        false
    )

    fun setDataDownloaded(value: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(context.resources.getString(R.string.sp_did_download_data), value)
            apply()
        }
    }

    fun saveAuthToken(value: String) {
        with(sharedPref.edit()) {
            putString(context.resources.getString(R.string.sp_auth_token), value)
            apply()
        }
    }

    fun saveRefreshToken(value: String) {
        with(sharedPref.edit()) {
            putString(context.resources.getString(R.string.sp_refresh_token), value)
            apply()
        }
    }

    fun saveUser(firstName: String, lastName: String, id: Int, email: String) {
        with(sharedPref.edit()) {
            putString(context.resources.getString(R.string.sp_user_first_name), firstName)
            putString(context.resources.getString(R.string.sp_user_last_name), lastName)
            putString(context.resources.getString(R.string.sp_user_email), email)
            putInt(context.resources.getString(R.string.sp_user_id), id)
            apply()
        }
    }

    fun getAuthToken(): String? = sharedPref.getString(
        context.resources.getString(R.string.sp_auth_token),
        null
    )

    fun getRefreshToken(): String? = sharedPref.getString(
        context.resources.getString(R.string.sp_refresh_token),
        null
    )

    fun getUserFirstName(): String? =
        sharedPref.getString(
            context.resources.getString(R.string.sp_user_first_name),
            null
        )

    fun getUserLastName(): String? =
        sharedPref.getString(
            context.resources.getString(R.string.sp_user_last_name),
            null
        )

    fun getUserEmail(): String? =
        sharedPref.getString(
            context.resources.getString(R.string.sp_user_email),
            null
        )

    fun getUserID(): Int =
        sharedPref.getInt(
            context.resources.getString(R.string.sp_user_id),
            -1
        )
}
