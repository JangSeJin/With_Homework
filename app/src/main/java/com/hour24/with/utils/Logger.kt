package com.hour24.with.utils

import android.util.Log

/**
 * @description Logger
 */
object Logger {

    // Black
    @JvmStatic
    fun v(LOG_TAG: String, msg: String?) {
        if (msg != null) {
            Log.v(LOG_TAG, msg)
        }
    }

    // Blue
    @JvmStatic
    fun d(LOG_TAG: String, msg: String?) {
        if (msg != null) {
            Log.d(LOG_TAG, msg)
        }
    }

    // Red
    @JvmStatic
    fun e(LOG_TAG: String, msg: String?) {
        if (msg != null) {
            Log.e(LOG_TAG, msg)
        }
    }

    // Green
    @JvmStatic
    fun i(LOG_TAG: String, msg: String?) {
        if (msg != null) {
            Log.i(LOG_TAG, msg)
        }
    }

    // Yellow
    @JvmStatic
    fun w(LOG_TAG: String, msg: String?) {
        if (msg != null) {
            Log.w(LOG_TAG, msg)
        }
    }
}