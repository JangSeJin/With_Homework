package com.hour24.with.utils

import android.databinding.BindingAdapter
import android.support.v7.widget.Toolbar


object BindingAdapterUtils {

    var TAG = BindingAdapterUtils::class.java.name

    /**
     * Toolbar Title Setting
     */
    @JvmStatic
    @BindingAdapter("toolbarTitle")
    fun setToolbarTitle(view: Toolbar, title: String) {

        try {
            view.title = title
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
