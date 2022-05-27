package com.wenger.common.signup

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

class SignUpViewModel(
    private val repository: ILogSignUpRepository
) : ViewModel() {

    private val _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>()
    val userRegistrationStatus: LiveData<Resource<AuthResult>>
        get() = _userRegistrationStatus

    private val _errorEmail = MutableLiveData<Int>()
    val errorEmail: LiveData<Int>
        get() = _errorEmail

    private val _errorPassword = MutableLiveData<Int>()
    val errorPassword: LiveData<Int>
        get() = _errorPassword

    private val _errorUserName = MutableLiveData<Int>()
    val errorUserName: LiveData<Int>
        get() = _errorUserName

    fun createNewUser(email: String, password: String, userName: String) {
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

        if (userName.isEmpty()) {
            _errorUserName.value = R.string.username_required
            return
        }

        _userRegistrationStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.signUpNewUser(email, password, userName)
            _userRegistrationStatus.postValue(result)
        }
    }
}