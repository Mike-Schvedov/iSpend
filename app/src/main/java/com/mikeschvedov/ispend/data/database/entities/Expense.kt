package com.mikeschvedov.ispend.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikeschvedov.ispend.utils.Category
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Expense(
    @PrimaryKey
    val shipId: String = UUID.randomUUID().toString(),
    val description: String,
    val amountSpent: Int,
    val category: Category,
    val hour: Int,
    val day: Int,
    val month: Int,
    val year: Int
) : Parcelable