package com.wenger.common.models

import com.google.firebase.database.IgnoreExtraProperties

data class UserCredentials(
    val email: String? = null,
    val password: String? = null,
    val userName: String? = null
)
