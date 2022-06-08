package com.wenger.common.base

import androidx.core.util.PatternsCompat
import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import com.wenger.common.IAuthRepository
import com.wenger.common.util.BaseResult
import com.wenger.common.util.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class BaseLoginViewModel(
    private val repository: IAuthRepository
) : ViewModel() {

    private val _loginStatus = MutableSharedFlow<ViewState<AuthResult?>>()
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

    val validState = _loginFieldValid.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), false
    )

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
            _loginStatus.emit(ViewState.Loading())
            val result = repository.signIn(email, password)
            when(result) {
                is BaseResult.Success -> {
                    _loginStatus.emit(ViewState.Success(result.data))
                }
                is BaseResult.Error -> {
                    val error = result.exception.message
                    _loginStatus.emit(ViewState.Error(error))
                    Timber.e(error)
                }
            }

        }
    }
}