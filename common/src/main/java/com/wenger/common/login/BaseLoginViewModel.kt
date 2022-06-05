package com.wenger.common.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import com.wenger.common.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BaseLoginViewModel(
    private val repository: IAuthRepository
) : ViewModel() {

    private val _loginStatus = MutableSharedFlow<Result<AuthResult>>()
    val loginStatus = _loginStatus.asSharedFlow()

    private val emailFlow = MutableStateFlow("")

    private val passwordFlow = MutableStateFlow("")

    private val _loginFieldValid = emailFlow.combine(passwordFlow) { email, password ->
        var isEmailValid = false
        var isPasswordValid = false
        if (email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            isEmailValid = true
        }
        if (password.isNotEmpty() && password.length >= 8) {
            isPasswordValid = true
        }
        isEmailValid && isPasswordValid
    }

    private val _validState = MutableStateFlow(false)
    val validState = _validState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loginFieldValid.collectLatest {
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

    fun loginUser() {
        val email = emailFlow.value
        val password = passwordFlow.value
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.signIn(email, password)
            _loginStatus.emit(result)
        }
    }
}