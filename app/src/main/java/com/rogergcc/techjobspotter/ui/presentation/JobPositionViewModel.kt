package com.rogergcc.techjobspotter.ui.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionCacheUseCase
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.utils.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created on agosto.
 * year 2023 .
 */

class JobPositionViewModel(
    private val jobsCacheUseCase: JobsPositionCacheUseCase,
    private val jobsMapper: JobsMapperProvider,
) : ViewModel() {

//    private val _uiState = MutableStateFlow(UiState())
//    val uiState = _uiState.asStateFlow()

    sealed class DetailUiState {
        object Loading : DetailUiState()
        data class Success(
            val jobPositionDetailUi: JobPositionUi? = null,
            val jobPositionFavoriteUi: JobPositionUi? = null,

        ) : DetailUiState()
        data class Failure(val errorMessage: UiText) : DetailUiState()
    }
    private val _uiPositionDetailState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiPositionState: StateFlow<DetailUiState> get() = _uiPositionDetailState

//    private val _jobPositionDetail = MutableLiveData<Resource<JobPositionUi>>()
//    val jobPositionDetail: LiveData<Resource<JobPositionUi>> get() = _jobPositionDetail
//
//    private val _jobPositionFavorite = MutableLiveData<Resource<JobPositionUi>>()
//    val jobPositionFavorite: LiveData<Resource<JobPositionUi>> get() = _jobPositionFavorite
//
//    private val _errorMessage = MutableLiveData<UiText>()
//    val errorMessage: LiveData<UiText> get() = _errorMessage

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
//        _errorMessage.value = UiText.DynamicString(throwable.message ?: "Unknown error")
        showErrorState()
    }

    private fun showErrorState() {
        _uiPositionDetailState.value = DetailUiState.Failure(UiText.StringResource(R.string.error_message, listOf("Unknown error")))
    }

    fun checkJobMarked(jobPositionUi: JobPositionUi) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val jobPositionDomain = jobsMapper.provider().presentationToDomain(jobPositionUi)

                val jobPositionFound = jobsCacheUseCase.getJobByIdCache(jobPositionDomain.id ?: 0)


                if (jobPositionFound.id != jobPositionDomain.id) {
                //  _jobPositionDetail.postValue(Resource.Success(jobPositionUi ))
//                    _uiPositionDetailState.postValue(DetailUiState.Success(jobPositionDetailUi = jobPositionUi))
                    _uiPositionDetailState.value = DetailUiState.Success(
                        jobPositionDetailUi = jobPositionUi)
                    return@launch
                }

                val jobsFoundUi = jobsMapper.provider().domainToPresentation(jobPositionFound)
                jobsFoundUi.isMarked = true
                //_jobPositionDetail.postValue(Resource.Success(jobsFoundUi))
//                _uiPositionDetailState.postValue(DetailUiState.Success(jobPositionDetailUi = jobsFoundUi))
                _uiPositionDetailState.value = DetailUiState.Success(
                    jobPositionDetailUi = jobsFoundUi)

            } catch (e: Exception) {
                Log.e(TAG, "markFavoriteJobPosition: ${e.message}")
//                _errorMessage.postValue(UiText.StringResource(R.string.error_message, listOf(e.message ?: "Unknown error")))
                showErrorState()
            }
        }
    }

    fun markFavoriteJobPosition(jobPositionUi: JobPositionUi) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val jobPositionDomain = jobsMapper.provider().presentationToDomain(jobPositionUi)

                val jobPositionFound = jobsCacheUseCase.getJobByIdCache(jobPositionDomain.id ?: 0)
                if (jobPositionFound.id == jobPositionDomain.id) {
                    jobsCacheUseCase.deleteJobCache(jobPositionDomain)
                    val jobsFoundUi = jobsMapper.provider().domainToPresentation(jobPositionDomain)
                    jobsFoundUi.isMarked = false
//                    _jobPositionFavorite.postValue(Resource.Success(jobsFoundUi))
//                    _uiPositionDetailState.postValue(
//                        DetailUiState.Success(jobPositionFavoriteUi = jobsFoundUi))
                    _uiPositionDetailState.value = DetailUiState.Success(
                        jobPositionFavoriteUi = jobsFoundUi)
                    return@launch
                }

                jobsCacheUseCase.insertJobCache(jobPositionDomain)
                val jobsFoundUi = jobsMapper.provider().domainToPresentation(jobPositionDomain)
                jobsFoundUi.isMarked = true
//                _jobPositionFavorite.postValue(Resource.Success(jobsFoundUi))
//                _uiPositionDetailState.postValue(
//                    DetailUiState.Success(jobPositionFavoriteUi = jobsFoundUi))
                _uiPositionDetailState.value = DetailUiState.Success(
                    jobPositionFavoriteUi = jobsFoundUi)
            } catch (e: Exception) {
                Log.e(TAG, "markFavoriteJobPosition: ${e.message}")
//                _errorMessage.postValue(UiText.StringResource(R.string.error_message, listOf(e.message ?: "Unknown error")))
//                _uiPositionDetailState.postValue(DetailUiState.Failure(
//                    UiText.StringResource(
//                        R.string.error_message, listOf(e.message ?: "Unknown error"))) )
                _uiPositionDetailState.value = DetailUiState.Failure(
                    UiText.StringResource(
                        R.string.error_message, listOf(e.message ?: "Unknown error")))
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

