package com.wenger.common.util

sealed class ViewState<T> {

    class Success<T>(val data: T) : ViewState<T>()
    class Loading<T>() : ViewState<T>()
    class Error<T>(val message: String?) : ViewState<T>()

}
