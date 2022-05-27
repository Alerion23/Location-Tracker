package com.wenger.common.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.wenger.common.ILogSignUpRepository
import com.wenger.common.R
import com.wenger.common.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(
    private val repository: ILogSignUpRepository
) : ViewModel() {

    private val _loginStatus = MutableLiveData<Resource<AuthResult>>()
    val loginStatus: LiveData<Resource<AuthResult>>
        get() = _loginStatus

    private val _errorEmail = MutableLiveData<Int>()
    val errorEmail: LiveData<Int>
        get() = _errorEmail

    private val _errorPassword = MutableLiveData<Int>()
    val errorPassword: LiveData<Int>
        get() = _errorPassword

    fun loginUser(email: String, password: String) {
        if (email.isEmpty()) {
            _errorEmail.value = R.string.email_required
            return
        }

        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorEmail.value = R.string.provide_valid_email
            return
        }

        if (password.isEmpty()) {
            _errorPassword.value = R.string.password_required
            return
        }

        if (password.length < 8) {
            _errorPassword.value = R.string.min_8_characters
            return
        }

        _loginStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val result = repository.signIn(email, password)
                _loginStatus.postValue(result)
            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
            }
        }
    }

}