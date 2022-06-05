package com.wenger.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenger.common.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class BaseLauncherViewModel(
    private val repository: IAuthRepository
) : ViewModel() {

    private val _userExist = MutableStateFlow(false)
    val userExist = _userExist.asStateFlow()

    fun checkAuthState() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.checkAuth()
            result
                .onSuccess {
                    if (it != null) {
                        _userExist.emit(true)
                    } else {
                        _userExist.emit(false)
                    }
                }
                .onFailure {
                    Timber.e(it.message)
                }
        }
    }

}