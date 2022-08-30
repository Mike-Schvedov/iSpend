package com.mikeschvedov.ispend.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikeschvedov.ispend.utils.Category
import kotlinx.parcelize.Parcelize
import java.util.*


data class Report(
    val category: String,
    val totalSpent: Int,
    val percentOutOfAll: Int
)