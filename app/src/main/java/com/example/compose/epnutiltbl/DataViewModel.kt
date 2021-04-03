package com.example.compose.epnutiltbl

import androidx.compose.runtime.Stable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DataViewModel : ViewModel() {
    private var _uiResult = MutableLiveData<List<ResultState>>()
    val uiResult: LiveData<List<ResultState>>
        get() = _uiResult

    fun setResult(value:List<ResultState>) {
        _uiResult.value = value
    }
}

@Stable
data class ResultState(
    val selectIds: List<Int>,
    val selectStringIds:List<Int>
)

class DataViewModelFactory(
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}