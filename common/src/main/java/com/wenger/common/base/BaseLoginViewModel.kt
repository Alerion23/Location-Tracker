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

    private val _loginStatus = MutableStateFlow<ViewState<AuthResult>>(ViewState.Default)
    val loginStatus = _loginStatus.asStateFlow()

    private val emailFlow = MutableStateFlow("")

    private val passwordFlow = MutableStateFlow("")

    val validState = emailFlow.combine(passwordFlow) { email, password ->
        val isEmailValid =
            email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty() && password.length >= 8
        isEmailValid && isPasswordValid
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
            _loginStatus.emit(ViewState.Loading)
            val result = repository.signIn(email, password)
            _loginStatus.value = when (result) {
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