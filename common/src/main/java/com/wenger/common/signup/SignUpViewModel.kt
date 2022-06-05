package com.wenger.common.signup

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenger.common.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: IAuthRepository
) : ViewModel() {

    private val _userRegistrationStatus = MutableSharedFlow<Result<Void>>()
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

    private val _validState = MutableStateFlow(false)
    val validState = _validState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpFieldValid.collectLatest {
                _validState.emit(it)
            }
        }
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
            val result = repository.signUpNewUser(email, password, userName)
            _userRegistrationStatus.emit(result)
        }
    }
}