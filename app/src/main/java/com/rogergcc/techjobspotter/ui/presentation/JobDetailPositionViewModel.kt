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

class JobDetailPositionViewModel(
    private val jobsCacheUseCase: JobsPositionCacheUseCase,
    private val jobsMapper: JobsMapperProvider,
) : ViewModel() {

    sealed class DetailUiState {
        object Loading : DetailUiState()
        data class Success(
            val jobPositionDetailUi: JobPositionUi? = null,
            val jobPositionFavoriteUi: JobPositionUi? = null

        ) : DetailUiState()
        data class Failure(val errorMessage: UiText) : DetailUiState()
    }
    private val _uiPositionDetailState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiPositionState: StateFlow<DetailUiState> get() = _uiPositionDetailState


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
                    _uiPositionDetailState.value = DetailUiState.Success(
                        jobPositionDetailUi = jobPositionUi)
                    return@launch
                }

                val jobsFoundUi = jobsMapper.provider().domainToPresentation(jobPositionFound)
                _uiPositionDetailState.value = DetailUiState.Success(
                    jobPositionDetailUi = jobsFoundUi.copy(isMarked = true))

            } catch (e: Exception) {
                Log.e(TAG, "markFavoriteJobPosition: ${e.message}")
                showErrorState()
            }
        }
    }

    fun markFavoriteJobPosition(jobPositionUi: JobPositionUi) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val jobPositionDomain = jobsMapper.provider().presentationToDomain(jobPositionUi)
                val jobPositionFound = jobsCacheUseCase.getJobByIdCache(jobPositionDomain.id ?: 0)

                val jobsFoundUi = jobsMapper.provider().domainToPresentation(jobPositionDomain)

                if (jobPositionFound.id == jobPositionDomain.id) {
                    jobsCacheUseCase.deleteJobCache(jobPositionDomain)

                    _uiPositionDetailState.value = DetailUiState.Success(
                        jobPositionFavoriteUi = jobsFoundUi.copy(isMarked = false))
                    return@launch
                }

                jobsCacheUseCase.insertJobCache(jobPositionDomain)
                _uiPositionDetailState.value = DetailUiState.Success(
                    jobPositionFavoriteUi = jobsFoundUi.copy(isMarked = true))
            } catch (e: Exception) {
                Log.e(TAG, "markFavoriteJobPosition: ${e.message}")
                _uiPositionDetailState.value = DetailUiState.Failure(
                    UiText.StringResource(
                        R.string.error_message, listOf(e.message ?: "Unknown error")))
            }
        }
    }



    companion object {
        private const val TAG = "DetailPositionViewModel"
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

