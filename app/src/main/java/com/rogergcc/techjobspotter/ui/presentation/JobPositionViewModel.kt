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
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionCacheUseCase
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.utils.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created on agosto.
 * year 2023 .
 */

class JobPositionViewModel(
    private val jobsCacheUsecase: JobsPositionCacheUseCase,
    private val jobsMapper: JobsMapperProvider,
) : ViewModel() {

//    private val _uiState = MutableStateFlow(UiState())
//    val uiState = _uiState.asStateFlow()

    private val _markedJobPosition = MutableLiveData<Resource<JobPositionUi>>()
    val markedJobPosition: LiveData<Resource<JobPositionUi>> get() = _markedJobPosition

    private val _errorMessage = MutableLiveData<UiText>()
    val errorMessage: LiveData<UiText> get() = _errorMessage

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _errorMessage.value = UiText.DynamicString(throwable.message ?: "Unknown error")
    }

    fun checkJobMarked(jobPositionUi: JobPositionUi) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val jobPositionDomain = jobsMapper.provider().presentationToDomain(jobPositionUi)

                val jobPositionFound = jobsCacheUsecase.getJobByIdCache(jobPositionDomain.id ?: 0)


                if (jobPositionFound.id == jobPositionDomain.id) {
                    val jobsFoundUi = jobsMapper.provider().domainToPresentation(jobPositionFound)
                    jobsFoundUi.isMarked = true
                    _markedJobPosition.postValue(Resource.Success(jobsFoundUi))
                } else {
                    _markedJobPosition.postValue(Resource.Success(jobPositionUi ))
                }

            } catch (e: Exception) {
                Log.e(TAG, "markFavoriteJobPosition: ${e.message}")
                _errorMessage.postValue(UiText.StringResource(R.string.error_message, listOf(e.message ?: "Unknown error")))
            }
        }
    }



    companion object {
        private const val TAG = "JobPositionViewModel"
    }

}
class JobPositionViewModelFactory(
    private val repo: JobsPositionCacheUseCase,
    private val jobsMapperProvider: JobsMapperProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(JobsPositionCacheUseCase::class.java,
            JobsMapperProvider::class.java)
            .newInstance(repo, jobsMapperProvider)
    }
}

