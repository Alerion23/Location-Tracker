package com.wenger.common.util

import java.lang.Exception

inline fun <T> safeCall(action: () -> Resource<T>) : Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}