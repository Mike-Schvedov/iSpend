package com.mikeschvedov.ispend.data

import com.mikeschvedov.ispend.data.database.dao.ExpenseDao
import com.mikeschvedov.ispend.data.database.entities.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    // ------- DATABASE ------- //
     suspend fun saveNewExpenseInDB(expenseData: Expense){
         expenseDao.addExpense(expenseData)
    }

    fun getExpensesByDateFromDB(day: Int, month: Int, year:Int): Flow<List<Expense>> {
        return expenseDao.getExpensesByDate(day, month, year)
    }

    fun getExpensesByYearFromDB(year:Int): Flow<List<Expense>> {
        return expenseDao.getExpensesByYear(year)
    }

    fun getExpensesBetweenTwoMonths(firstMonth: Int, secondMonth: Int, year: Int) = flow {

        val daysOfFirstMonth = listOf<Int>(15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31)
        val daysOfSecondMonth = listOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14)

        // Getting expenses for both months
        val firstMonthExpenses = expenseDao.getExpensesByMonth(firstMonth, year, daysOfFirstMonth)
        val secondMonthExpenses = expenseDao.getExpensesByMonth(secondMonth, year, daysOfSecondMonth)

        // Combining all the expenses (half of first month, and half of second)
        val result = firstMonthExpenses.combine(secondMonthExpenses) { first, second ->
            val newList = mutableListOf<Expense>()
            newList.addAll(first)
            newList.addAll(second)
            return@combine newList
        }

        result.collect{
            emit(it)
        }
    }

}