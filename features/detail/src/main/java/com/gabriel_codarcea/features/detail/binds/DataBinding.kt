package com.gabriel_codarcea.features.detail.binds

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    Glide.with(view).load(url).into(view)
}
