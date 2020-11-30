package com.gabriel_codarcea.features.login.utils

import androidx.core.util.PatternsCompat

fun isValidEmail(target: CharSequence): Boolean {
    return if (target.isEmpty()) {
        false
    } else {
        PatternsCompat.EMAIL_ADDRESS.matcher(target).matches()
    }
}

fun isValidPassword(target: CharSequence): Boolean {
    return !(target.isEmpty() || target.length < 5)
}

