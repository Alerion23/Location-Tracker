package com.wenger.common.util

sealed class BaseResult<T>() {

    class Success<T>(val data: T) : BaseResult<T>()
    class Error<T>(val exception: Exception) : BaseResult<T>()
}
