package com.rogergcc.techjobspotter.ui.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.core.Resource
import com.rogergcc.techjobspotter.domain.GetJobsUseCase
import com.rogergcc.techjobspotter.ui.utils.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

/**
 * Created on agosto.
 * year 2023 .
 */

class GetJobsViewModel(private val getJobsUseCase: GetJobsUseCase) : ViewModel() {
    private val _message = MutableLiveData<UiText>()
    val message: LiveData<UiText> get() = _message

    private val _errorMessage = MutableLiveData<UiText>()
    val errorMessage: LiveData<UiText> get() = _errorMessage

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _errorMessage.value = UiText.DynamicString(throwable.message ?: "Unknown error")
    }

    fun updateUserGreeting(name: String) {
        _message.value = UiText.DynamicString("Hola, $name")
    }

    private fun updateErrorMessageFromResource(@StringRes errorResId: Int, vararg args: Any) {
        _errorMessage.value = UiText.StringResource(errorResId, args.toList())
    }

    fun fetchJobs() =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO + coroutineExceptionHandler) {
            emit(Resource.Loading())
            try {
                delay(1000)
                emit(Resource.Success(getJobsUseCase()))
            } catch (e: Exception) {
                emit(Resource.Failure(e))

                updateErrorMessageFromResource(
                    R.string.error_message,
                    e.message ?: "Unknown error"
                )

            }
        }


}

class JobViewModelFactory(private val repo: GetJobsUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GetJobsUseCase::class.java).newInstance(repo)
    }
}