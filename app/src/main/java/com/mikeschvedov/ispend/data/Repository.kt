package com.mikeschvedov.ispend.data

import com.mikeschvedov.ispend.data.database.dao.ExpenseDao
import com.mikeschvedov.ispend.data.database.entities.Expense
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    // ------- DATABASE ------- //
     suspend fun saveNewExpenseInDB(expenseData: Expense){
        println("REPOSITORY")
         expenseDao.addExpense(expenseData)
    }
}