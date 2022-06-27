package com.wenger.common.signup

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.wenger.common.IAuthRepository
import com.wenger.common.util.BaseResult
import com.wenger.common.util.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SignUpViewModel(
    private val repository: IAuthRepository
) : ViewModel() {

    private val _userRegistrationStatus = MutableStateFlow<ViewState<AuthResult>>(ViewState.Default)
    val userRegistrationStatus = _userRegistrationStatus.asStateFlow()

    private val emailFlow = MutableStateFlow("")

    private val passwordFlow = MutableStateFlow("")

    private val userNameFlow = MutableStateFlow("")

    val validState = combine(emailFlow, passwordFlow, userNameFlow) { email, password, userName ->
        val isEmailValid =
            email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty() && password.length >= 8
        val isUserNameValid = userName.isNotEmpty()
        isEmailValid && isPasswordValid && isUserNameValid
    }

    fun onEmailEntered(email: String) {
        emailFlow.tryEmit(email)
    }

    fun onPasswordEntered(password: String) {
        passwordFlow.tryEmit(password)
    }

    fun onUserNameEntered(userName: String) {
        userNameFlow.tryEmit(userName)
    }

    fun createNewUser() {
        val email = emailFlow.value
        val password = passwordFlow.value
        val userName = userNameFlow.value
        viewModelScope.launch(Dispatchers.IO) {
            _userRegistrationStatus.emit(ViewState.Loading)
            val result = repository.signUpNewUser(email, password, userName)
            _userRegistrationStatus.value = when (result) {
                is BaseResult.Success -> {
                    ViewState.Success(result.data)
                }
                is BaseResult.Error -> {
                    val error = result.exception.message
                    ViewState.Error(error)
                }
            }
        }
    }
}