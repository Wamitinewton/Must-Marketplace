package com.example.mustmarket.features.auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.LoadingState
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.FinalUser
import com.example.mustmarket.features.auth.domain.model.LoginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: UseCases
) : ViewModel() {

    private val viewModelState = MutableStateFlow(LoginViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private var _idToken = MutableLiveData("")
    // we are using live data to listen to token changes
    val idToken: LiveData<String>
        get() = _idToken

    private var _fUser = MutableLiveData<FinalUser>()
    val fUser = _fUser




    fun loginUser(loginCredentials: LoginUser){
        viewModelScope.launch {
            authUseCase.loginUseCase(loginUser = loginCredentials).onEach { result ->
                viewModelState.update { state ->
                    when(result){
                        is Resource.Success -> state.copy(
                            isLoading = false,
                            result = result.data?.user?.name ?: "Some result",
                            errorMessage = ""
                        )
                        is Resource.Error -> {
                            state.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Some error",
                                result = ""
                            )
                        }
                        is Resource.Loading -> state.copy(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)
        }
    }
    fun onEmailInputChanged(emailInput: String) {
        viewModelState.update {
            it.copy(emailInput = emailInput)
        }
    }

    fun onPasswordInputChanged(passwordInput: String) {
        viewModelState.update {
            it.copy(passwordInput = passwordInput)
        }
    }

    fun toggleShowPassword(show: Boolean) {
        viewModelState.update {
            it.copy(showPassword = show)
        }
    }
}