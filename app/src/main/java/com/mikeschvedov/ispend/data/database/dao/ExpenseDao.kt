package com.mikeschvedov.ispend.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikeschvedov.ispend.data.database.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addExpense(expense: Expense)

        @Query("SELECT * FROM expense WHERE" +
                " day LIKE '%' || :day || '%' AND" +
                " month LIKE '%' || :month || '%' AND" +
                " year LIKE '%' || :year || '%'")
        fun getExpensesByDate(day: Int, month: Int, year: Int): Flow<List<Expense>>


        // Get all expenses that have this years, this month, and one of the valid days
        @Query("SELECT * FROM expense WHERE" +
                " day IN (:validDays) AND" +
                " month LIKE '%' || :month || '%' AND" +
                " year LIKE '%' || :year || '%'")
        fun getExpensesByMonth(month: Int, year: Int, validDays: List<Int>): Flow<List<Expense>>


        @Query("SELECT * FROM expense WHERE" +
                " year LIKE '%' || :year || '%'")
        fun getExpensesByYear(year: Int) : Flow<List<Expense>>


//
//        @Query("SELECT * FROM launch WHERE name LIKE '%' || :searchQuery || '%' ORDER BY date_unix ASC")
//        fun getAllLaunchesOrderedByDateASC(searchQuery: String): Flow<List<Launch>>
//
//        @Query("SELECT * FROM launch WHERE name LIKE '%' || :searchQuery || '%' ORDER BY date_unix DESC")
//        fun getAllLaunchesOrderedByDateDESC(searchQuery: String): Flow<List<Launch>>
//
//        // Get all Launches the match the id's in the list
//        @Query("SELECT * FROM launch WHERE launchId IN (:launchIDs)")
//        fun getLaunchesByID(launchIDs: List<String>) : Flow<List<Launch>>
//
//        @Query("SELECT * FROM launch")
//        fun getAllLaunches() : Flow<List<Launch>>
//
//
//    @Query("SELECT * FROM ship ORDER BY name ASC ")
//    fun getAllShipsOrderedByTitle() : Flow<List<Ship>>
//
//    @Query("SELECT * FROM ship WHERE name LIKE '%' || :searchQuery || '%'")
//    fun getShipsByMatchingName(searchQuery:String) : Flow<List<Ship>>
//
//    // Get all Launches the match the id's in the list
//    @Query("SELECT * FROM ship WHERE shipId IN (:shipsIDs)")
//    fun getShipsByID(shipsIDs: List<String>) : Flow<List<Ship>>
}