package com.mikeschvedov.ispend.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikeschvedov.ispend.data.Repository
import com.mikeschvedov.ispend.data.database.entities.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _isListEmpty = MutableLiveData<Boolean>()
    val isListEmpty: LiveData<Boolean> get() = _isListEmpty

    private var adapter = HomeRecyclerAdapter(){
        println("viewmodel isEmpty: $it")
        _isListEmpty.postValue(it)
    }

    fun getRecyclerAdapter(): HomeRecyclerAdapter {
        return adapter
    }

    fun saveNewExpenseInDB(expense: Expense){
        viewModelScope.launch {
            repository.saveNewExpenseInDB(expense)
        }
    }

    fun populateRecyclerList(day: Int, month: Int, year: Int){
        viewModelScope.launch {
            repository.getExpensesByDateFromDB(day, month, year).collect{ listOfExpenses ->
              adapter.setNewData(listOfExpenses)
            }
        }
    }
}