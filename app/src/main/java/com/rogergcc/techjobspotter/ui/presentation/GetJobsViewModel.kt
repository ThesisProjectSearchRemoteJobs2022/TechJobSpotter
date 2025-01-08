package com.rogergcc.techjobspotter.ui.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.core.Resource
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionUseCase
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.utils.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created on agosto.
 * year 2023 .
 */

class GetJobsViewModel(
    private val jobsPositionUseCase: JobsPositionUseCase,
    private val jobsMapperProvider: JobsMapperProvider,
) : ViewModel() {
    private val _resourceJobs = MutableLiveData<Resource<List<JobPositionUi>>>()
    val resourceJobs: LiveData<Resource<List<JobPositionUi>>> get() = _resourceJobs

    private val _errorMessage = MutableLiveData<UiText>()
    val errorMessage: LiveData<UiText> get() = _errorMessage

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _errorMessage.value = UiText.DynamicString(throwable.message ?: "Unknown error")
    }


    fun fetchJobs() {
        _resourceJobs.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                delay(1000)
                val jobsPosition = jobsPositionUseCase.execute()
                val jobsListUi = jobsPosition.map {
                    jobsMapperProvider.getJobsMapper().domainToPresentation(it)
                }
                _resourceJobs.postValue(Resource.Success(jobsListUi))
            } catch (e: Exception) {
//                _resourceJobs.value = Resource.Failure(e)
                Log.e(TAG, "fetchJobs: ${e.message}" )
                _resourceJobs.postValue(Resource.Failure(e))
                if (e is IllegalArgumentException) {
                    _errorMessage.postValue(UiText.StringResource(R.string.error_message, listOf(e.message ?: "Unknown error")))
//                    _errorMessage.value = UiText.StringResource(errorResId, args.toList())
                } else {
                    _errorMessage.postValue(UiText.StringResource(R.string.error_message_no_data, listOf(e.message ?: "Unknown error")))
                }

            }
        }
    }

    companion object {
        private const val TAG = "GetJobsViewModel"
    }

}
class JobViewModelFactory(
    private val repo: JobsPositionUseCase,
    private val jobsMapperProvider: JobsMapperProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(JobsPositionUseCase::class.java, JobsMapperProvider::class.java).newInstance(repo, jobsMapperProvider)
    }
}


//class JobViewModelFactory(private val repo: JobsPositionUseCase) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(JobsPositionUseCase::class.java).newInstance(repo)
//    }
//}