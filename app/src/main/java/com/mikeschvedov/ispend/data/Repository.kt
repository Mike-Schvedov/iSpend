package com.mikeschvedov.ispend.data

import com.mikeschvedov.ispend.data.database.dao.ExpenseDao
import com.mikeschvedov.ispend.data.database.entities.Expense
import kotlinx.coroutines.flow.Flow
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

    suspend fun getExpensesByDateFromDB(day: Int, month: Int, year:Int): Flow<List<Expense>> {
        return expenseDao.getExpensesByDate(day, month, year)
    }
}