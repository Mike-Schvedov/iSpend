package com.mikeschvedov.ispend.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikeschvedov.ispend.data.database.dao.ExpenseDao
import com.mikeschvedov.ispend.data.database.entities.Expense

@Database(entities = [Expense::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}