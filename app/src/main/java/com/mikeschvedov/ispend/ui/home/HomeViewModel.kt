package com.mikeschvedov.ispend.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikeschvedov.ispend.data.Repository
import com.mikeschvedov.ispend.data.database.entities.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    fun saveNewExpenseInDB(expense: Expense){
        viewModelScope.launch {
            repository.saveNewExpenseInDB(expense)
        }
    }

}