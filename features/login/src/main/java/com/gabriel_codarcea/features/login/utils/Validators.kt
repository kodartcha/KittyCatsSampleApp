package com.gabriel_codarcea.features.login.utils

import android.text.TextUtils
import android.util.Patterns

fun isValidEmail(target: CharSequence): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}

fun isValidPassword(target: CharSequence): Boolean {
    return !(target.isEmpty() || target.length < 5)
}
