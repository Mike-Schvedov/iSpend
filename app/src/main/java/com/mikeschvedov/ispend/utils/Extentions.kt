package com.mikeschvedov.ispend.utils

import android.content.res.Resources
import com.mikeschvedov.ispend.R
import com.mikeschvedov.ispend.application.App

fun Int.resourceToString(): String {
    return App.getRes()?.getString(this) ?: "Not Found"
}