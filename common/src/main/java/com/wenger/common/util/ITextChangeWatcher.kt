package com.wenger.common.util

import android.text.Editable
import android.text.TextWatcher

interface ITextChangeWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // empty
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // empty
    }
}