package com.mikeschvedov.ispend.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikeschvedov.ispend.data.Repository
import com.mikeschvedov.ispend.models.Report
import com.mikeschvedov.ispend.utils.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var adapter = ReportsRecyclerAdapter()

    fun getRecyclerAdapter(): ReportsRecyclerAdapter {
        return adapter
    }

    fun createReport(reportID: Int) {
        when (reportID) {
            0 -> getExpensesByYear(2022)
            1 -> getExpensesByMonths(firstMonth = 8, secondMonth = 9, 2022) // August - September
            2 -> getExpensesByMonths(firstMonth = 9, secondMonth = 10, 2022) // September - October
        }
    }


    private fun getExpensesByYear(year: Int) {
        viewModelScope.launch {
            repository.getExpensesByYearFromDB(year).collect { listOfExpenses ->
                println("These are the expenses by year $year : $listOfExpenses")
            }
        }
    }

    private fun getExpensesByMonths(firstMonth: Int, secondMonth: Int, year: Int) {
        viewModelScope.launch {
            repository.getExpensesBetweenTwoMonths(firstMonth, secondMonth, year)
                .collect { listOfExpenses ->
                    // Holder for all categories in these dates
                    val listOfExistingCategories = mutableSetOf<Category>()
                    // Getting all categories
                    listOfExpenses.forEach {
                        listOfExistingCategories.add(it.category)
                    }

                    println("These are the categories: $listOfExistingCategories \n")

                    // All Report Objects
                    val listOfReports = mutableListOf<Report>()
                    var totalSpent = 0

                    // ---- Creating a Report for each category ---- //
                    listOfExistingCategories.forEach { category->
                        var amountSpent = 0
                        // Sum all the money spend in this category
                        listOfExpenses.forEach {
                            if(it.category == category){
                                amountSpent += it.amountSpent
                                totalSpent += it.amountSpent
                            }
                        }

                        val newReport = Report(
                            category = category.hebrew,
                            totalSpent = amountSpent,
                            0
                        )

                        listOfReports.add(newReport)
                    }

                    // Adding the TOTAL report
                    listOfReports.sortByDescending { it.totalSpent }

                    listOfReports.add(Report(
                        "סה״כ",
                        totalSpent,
                        0
                    ))

                    // Populating the recycler list
                    adapter.setNewData(listOfReports)
                }
        }
    }

}