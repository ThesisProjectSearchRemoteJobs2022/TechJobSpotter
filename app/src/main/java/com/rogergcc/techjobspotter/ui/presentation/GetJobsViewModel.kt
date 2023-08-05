package com.rogergcc.techjobspotter.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.rogergcc.techjobspotter.core.Resource
import com.rogergcc.techjobspotter.domain.GetJobsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

/**
 * Created on agosto.
 * year 2023 .
 */

class GetJobsViewModel(private val getJobsUseCase: GetJobsUseCase): ViewModel() {


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->

        throwable.printStackTrace()
    }


    fun fetchJobs() = liveData(viewModelScope.coroutineContext + Dispatchers.IO + coroutineExceptionHandler) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(getJobsUseCase()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


}

class JobViewModelFactory(private val repo: GetJobsUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GetJobsUseCase::class.java).newInstance(repo)
    }
}