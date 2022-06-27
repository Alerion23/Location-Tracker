package com.wenger.common.util

sealed class BaseResult<out T> {

    class Success<T>(val data: T) : BaseResult<T>()
    class Error(val exception: Exception) : BaseResult<Nothing>()
}
