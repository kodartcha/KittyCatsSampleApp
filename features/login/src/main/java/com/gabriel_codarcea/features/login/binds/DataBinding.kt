package com.gabriel_codarcea.features.login.binds

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("addTextChangedListener")
fun addTextChangedListener(view: EditText, textWatcher: TextWatcher) {
    view.addTextChangedListener(textWatcher)
}
