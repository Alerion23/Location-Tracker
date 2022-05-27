package com.wenger.common

import com.google.firebase.auth.AuthResult
import com.wenger.common.util.Resource

interface ILogSignUpRepository {

    suspend fun signUpNewUser(email: String, password: String, userName: String) : Resource<AuthResult>

    suspend fun signIn(email: String, password: String): Resource<AuthResult>
}