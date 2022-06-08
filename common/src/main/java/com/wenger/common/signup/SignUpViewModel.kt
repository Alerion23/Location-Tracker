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

    private val _userRegistrationStatus = MutableSharedFlow<ViewState<AuthResult>>()
    val userRegistrationStatus = _userRegistrationStatus.asSharedFlow()

    private val emailFlow = MutableStateFlow("")

    private val passwordFlow = MutableStateFlow("")

    private val userNameFlow = MutableStateFlow("")

    private val _signUpFieldValid = combine(emailFlow, passwordFlow, userNameFlow) {
        email, password, userName ->
        var isEmailValid = false
        var isPasswordValid = false
        var isUserNameValid = false
        if (email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            isEmailValid = true
        }
        if (password.isNotEmpty() && password.length >= 8) {
            isPasswordValid = true
        }
        if (userName.isNotEmpty()) {
            isUserNameValid = true
        }
        isEmailValid && isPasswordValid && isUserNameValid
    }

    val validState = _signUpFieldValid.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), false)

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
            _userRegistrationStatus.emit(ViewState.Loading())
            val result = repository.signUpNewUser(email, password, userName)
            when (result) {
                is BaseResult.Success -> {
                    _userRegistrationStatus.emit(ViewState.Success(result.data))
                }
                is BaseResult.Error -> {
                    val error = result.exception.message
                    _userRegistrationStatus.emit(ViewState.Error(error))
                    Timber.e(error)
                }
            }
        }
    }
}