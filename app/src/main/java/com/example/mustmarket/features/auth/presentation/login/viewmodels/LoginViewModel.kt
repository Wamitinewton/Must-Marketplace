package com.example.mustmarket.features.auth.presentation.login.viewmodels

<<<<<<< HEAD
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.core.coroutineLogger.CoroutineDebugger
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.database.dao.CategoryDao
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.database.dao.UserDao
import com.example.mustmarket.features.auth.data.datastore.SessionManager
import com.example.mustmarket.features.auth.data.tokenHolder.AuthTokenHolder
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.state.LoginState
import com.example.mustmarket.usecase.UseCases
=======
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.usecase.UseCases
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.core.coroutineLogger.CoroutineDebugger
import com.example.mustmarket.features.auth.data.datastore.SessionManager
import com.example.mustmarket.features.auth.data.datastore.UserStoreManager
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.state.LoginState
import com.example.mustmarket.database.dao.CategoryDao
import com.example.mustmarket.database.dao.ProductDao
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
<<<<<<< HEAD
import kotlinx.coroutines.launch
import timber.log.Timber
=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

    private val authUseCase: UseCases,
<<<<<<< HEAD
    private val sessionManager: SessionManager,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val userDao: UserDao,
) : ViewModel() {

    private val _loggedInUser = MutableStateFlow<AuthedUser?>(null)
    val loggedInUser: StateFlow<AuthedUser?> get() = _loggedInUser
=======

    private val sessionManager: SessionManager,
    private val userStoreManager: UserStoreManager,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao

) : ViewModel() {
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

    private val coroutineDebugger = CoroutineDebugger.getInstance()

    private val _navigateToHome = Channel<Unit>()

    val navigateToHome = _navigateToHome.receiveAsFlow()

    private val _navigateToLogin = Channel<Unit>()
    val navigateToLogin = _navigateToLogin.receiveAsFlow()

<<<<<<< HEAD
=======
    private val _uiEvent = Channel<LoginEvent>()

    val uiEvent = _uiEvent.receiveAsFlow()
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

    private val _authUiState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())

    val authUiState: StateFlow<LoginState> get() = _authUiState

<<<<<<< HEAD
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> get() = _isUserLoggedIn


    init {
        viewModelScope.launch {
            val token = AuthTokenHolder.accessToken ?: authUseCase.authUseCase.getAccessToken()
            Timber.d("You are using token $token")
            _isUserLoggedIn.value = !token.isNullOrEmpty()
            getLoggedInUser()
        }
    }

    // this will help us proceed to home in-case user is authed
    fun isUserLoggedIn(): State<Boolean> {
        val token = AuthTokenHolder.accessToken ?: authUseCase.authUseCase.getAccessToken()
        return mutableStateOf(!token.isNullOrEmpty())
    }

    private suspend fun getLoggedInUser() {
        val user = authUseCase.authUseCase.getLoggedInUser()
        _loggedInUser.value = user
=======
    private val _isLoggedIn = MutableStateFlow(false)

    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {

        checkSession()

    }

    private fun checkSession() {

        _isLoggedIn.value = sessionManager.isSessionValid()

>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
    }

    override fun onCleared() {

        super.onCleared()

        coroutineDebugger.cancelAllCoroutines()

        val activeCoroutines = coroutineDebugger.getActiveCoroutinesInfo()

        if (activeCoroutines.isNotEmpty()) {

<<<<<<< HEAD
            Timber.tag("register")
                .d("⚠️ Warning: ${activeCoroutines.size} coroutines were still active when ViewModel was cleared:")
            activeCoroutines.forEach { info ->

                Timber.tag("register")
                    .d("📌 Coroutine ${info.id} (${info.tag}) - Running for ${info.duration}ms")
=======
            Log.d(

                "register",

                "⚠️ Warning: ${activeCoroutines.size} coroutines were still active when ViewModel was cleared:"

            )
            activeCoroutines.forEach { info ->

                Log.d(

                    "register",

                    "📌 Coroutine ${info.id} (${info.tag}) - Running for ${info.duration}ms"

                )
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
            }
        }
    }

    private fun logOut() {
        coroutineDebugger.launchTracked(
            scope = viewModelScope,
            tag = "Logout_process"
        ) {
            try {
                sessionManager.clearTokens()
<<<<<<< HEAD
                clearUserData()
                // Navigate to login screen
                _navigateToLogin.send(Unit)

                Timber.tag("Logout").d("Logout process completed successfully.")
            } catch (e: Exception) {
                Timber.tag("Logout").e(e, "Error during logout: ${e.localizedMessage}")
=======

                clearUserData()

                _isLoggedIn.value = false

                // Navigate to login screen
                _navigateToLogin.send(Unit)

                Log.d("Logout", "Logout process completed successfully.")
            } catch (e: Exception) {
                Log.e("Logout", "Error during logout: ${e.localizedMessage}", e)
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
            }
        }
    }

    private fun logInWithEmailAndPassword() {

        val email = _authUiState.value.emailInput

        val password = _authUiState.value.passwordInput


        coroutineDebugger.launchTracked(

            scope = viewModelScope,

            tag = "Login_process"

        ) {
            if (email.isEmpty() || password.isEmpty()) {

                _authUiState.value = _authUiState.value.copy(
                    isLoading = false,

                    errorMessage = "All Inputs Are Required"

                )

                return@launchTracked

            }
            coroutineDebugger.launchTracked(

                scope = viewModelScope,

                tag = "login_NetworkRequest"

            ) {
                val result = authUseCase.authUseCase.loginUseCase(

                    LoginRequest(

                        email = email,

                        password = password

                    )
                )
                result.collectLatest { loginResult ->

                    when (loginResult) {

                        is Resource.Loading -> {

                            _authUiState.value = _authUiState.value.copy(

                                isLoading = true,

                                errorMessage = null
                            )
                        }

                        is Resource.Error -> {

                            _authUiState.value = _authUiState.value.copy(

                                isLoading = false,

                                errorMessage = loginResult.message ?: "Unknown error occurred"

                            )
                        }

                        is Resource.Success -> {
<<<<<<< HEAD
                            val accessToken = loginResult.data!!.data.token
                            val refreshToken = loginResult.data.data.refreshToken
                            val user = loginResult.data.data.user
                            authUseCase.authUseCase.storeTokens(accessToken, refreshToken)
                            authUseCase.authUseCase.storeLoggedInUser(user)
                            _authUiState.value = _authUiState.value.copy(
                                isLoading = false,
=======

                            _authUiState.value = _authUiState.value.copy(

                                isLoading = false,

>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
                                errorMessage = null

                            )

                            _navigateToHome.send(Unit)

                        }
                    }
                }
            }

        }
    }

    fun onEvent(event: LoginEvent) {

        when (event) {

            is LoginEvent.EmailChanged -> {

                coroutineDebugger.launchTracked(

                    scope = viewModelScope,

                    tag = "EmailValidation"

                ) {
                    val emailError =

                        if (event.email.isNotEmpty() && !EMAIL_REGEX.matches(event.email)) {

                            "Invalid email format"

                        } else ""

                    _authUiState.value = _authUiState.value.copy(

                        emailInput = event.email,

                        emailError = emailError
<<<<<<< HEAD
=======

>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
                    )
                }
            }

            LoginEvent.ClearError -> {

                _authUiState.value = _authUiState.value.copy(

                    errorMessage = ""

                )
            }

            LoginEvent.Login -> {

                logInWithEmailAndPassword()

            }

            is LoginEvent.PasswordChanged -> {

                coroutineDebugger.launchTracked(
<<<<<<< HEAD
                    scope = viewModelScope,
                    tag = "PasswordValidation"
                ) {
                    val passwordError =
                        if (event.password.isNotEmpty() && !PASSWORD_REGEX.matches(event.password)) {
                            "Invalid password format"
                        } else ""
=======

                    scope = viewModelScope,

                    tag = "PasswordValidation"

                ) {
                    val passwordError =
                        if (event.password.isNotEmpty() && !PASSWORD_REGEX.matches(event.password)) {

                            "Invalid password format"

                        } else ""

>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
                    _authUiState.value = _authUiState.value.copy(

                        passwordInput = event.password,

                        passwordError = passwordError

                    )
                }
            }

            is LoginEvent.TogglePasswordVisibility -> {

                _authUiState.value = _authUiState.value.copy(

                    showPassword = event.show

                )
            }

            LoginEvent.Logout -> {
                logOut()
            }
        }
    }

    private suspend fun clearUserData() {
        try {
            productDao.clearAllProducts()
            categoryDao.clearAllCategory()
<<<<<<< HEAD
            userDao.deleteUser()
            Timber.tag("Logout").d("User data cleared successfully.")
        } catch (e: Exception) {
            Timber.tag("Logout").e(e, "Failed to clear user data: ${e.localizedMessage}")
=======
            userStoreManager.clearUserData()
            Log.d("Logout", "User data cleared successfully.")
        } catch (e: Exception) {
            Log.e("Logout", "Failed to clear user data: ${e.localizedMessage}", e)
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
            throw e
        }
    }

}