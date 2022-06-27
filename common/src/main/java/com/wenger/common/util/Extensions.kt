package com.wenger.common.util

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import java.lang.Exception

inline fun <T> Flow<T>.collectWhenStarted(
    lifecycleScope: LifecycleCoroutineScope,
    crossinline body: (T) -> Unit
): Job = lifecycleScope.launchWhenStarted { collectLatest { body(it) } }

inline fun <T> safeCall(action: () -> BaseResult<T>): BaseResult<T> {
    return try {
        action()
    } catch (e: Exception) {
        BaseResult.Error(e)
    }
}

fun EditText.listenTextChange(): Flow<String> = callbackFlow {
    val textWatcher = doAfterTextChanged {
        if (it != null) {
            trySend(it.toString())
        }
    }
    addTextChangedListener(textWatcher)
    awaitClose {
        removeTextChangedListener(textWatcher)
    }
}
