package com.example.mustmarket.features.auth.presentation.signup.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.core.util.parsedErrorMessage
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.presentation.signup.SignUpViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: UseCases
) : ViewModel() {
    private val viewModelState = MutableStateFlow(SignUpViewModelState())

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun onNameInputChanged(nameInput: String) {
        viewModelState.update {
            it.copy(nameInput = nameInput)
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

    fun onPasswordConfirmInputChanged(pwdConfirmInput: String) {
        viewModelState.update {
            it.copy(passwordConfirmInput = pwdConfirmInput)
        }
    }

    fun toggleShowPassword(show: Boolean){
        viewModelState.update {
            it.copy(showPassword = true)
        }
    }

    fun signUp(signUp: SignUpUser) {
        viewModelScope.launch {
            authUseCase.signUpUseCase(signUpUser = signUp).onEach { result ->
                viewModelState.update { state ->
                    when (result) {
                        is Resource.Success -> state.copy(
                            result = result.data?.name ?: "Some result",
                            errorMessage = "",
                            isLoading = false,
                        )

                        is Resource.Error -> {
                            state.copy(
                                errorMessage = parsedErrorMessage(result.message ?: "Error"),
                                isLoading = false,
                                result = "",
                            )
                        }

                        is Resource.Loading -> state.copy(
                            isLoading = true,
                            errorMessage = "",
                            result = ""
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}